package com.example.gnsspositionapp.usecase.measure

import android.app.*
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import androidx.core.app.NotificationCompat
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.ui.measure.LocationCallbackWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GetLocationService : Service() {

    companion object {
        private const val PACKAGE_NAME = "com.example.gnsspositionapp.usecase.measure"

        private val TAG = GetLocationService::class.simpleName!!

        private const val CHANNEL_ID = "location_channel"

        private const val EXTRA_STARTED_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"

        private const val NOTIFICATION_ID = 10000
    }

    private val mBinder = LocalBinder()

    private var mChangeConf = false

    @Inject lateinit var repository: LocationRepository

    private val job = Job()

    private val serviceScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var mNotificationManager : NotificationManager

    @Inject lateinit var mLocationRequest : LocationRequest

    @Inject lateinit var mFusedLocationClient : FusedLocationProviderClient

    private lateinit var mLocationCallbackWrapper: LocationCallbackWrapper

    private lateinit var mServiceHandler : Handler

    override fun onCreate() {

        mLocationCallbackWrapper = LocationCallbackWrapper(object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.let {
                    Timber.tag("locationService").d("${it.lastLocation.accuracy}")
                    onNewLocation(locationResult.lastLocation)
                }
            }
        })

        val handlerThread = HandlerThread(TAG)

        handlerThread.start()

        mServiceHandler = Handler(handlerThread.looper)

        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = getString(R.string.notification_channel_name)

            val mChannel = NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            mNotificationManager.createNotificationChannel(mChannel)
        }


        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val startedFromNotification = intent?.getBooleanExtra(EXTRA_STARTED_NOTIFICATION, false) ?: false

        if(startedFromNotification){
            Timber.d("stop service")
            removeLocationUpdates()
            stopSelf()
        }

        return START_NOT_STICKY
    }

    fun requestLocationUpdates() {
        startService(Intent(applicationContext, GetLocationService::class.java))

        try{
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallbackWrapper, Looper.myLooper()
            )
        }catch (ex: SecurityException){
            Timber.e("Lost location permission. $ex")
        }
    }

    fun removeLocationUpdates() {

        stopService(Intent(applicationContext, GetLocationService::class.java))

        try{
            mFusedLocationClient.removeLocationUpdates(mLocationCallbackWrapper)
        }catch (ex: SecurityException){
            Timber.e("Lost location permission. $ex")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Timber.d("Configuration changed")
        mChangeConf = true
    }

    private fun onNewLocation(lastLocation: Location) {
        serviceScope.launch {
            repository.addLocation(lastLocation)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        stopForeground(true)

        mChangeConf = false

        return mBinder
    }

    override fun onRebind(intent: Intent?) {
        stopForeground(true)

        mChangeConf = false

        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {

        if(!mChangeConf){
            startForeground(NOTIFICATION_ID, getNotification())
        }

        return true
    }

    override fun onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null)
    }

    private fun getNotification(): Notification {

        val intent = Intent(this, GetLocationService::class.java)

        intent.putExtra(EXTRA_STARTED_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .addAction(
                R.drawable.ic_baseline_location_on_24,
                getString(R.string.notification_remove),
                servicePendingIntent
            )
            .setContentText(getString(R.string.notification_content))
            .setContentTitle(getString(R.string.notification_title))
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)

        return builder.build()
    }

    inner class LocalBinder : Binder(){
        fun getService() = this@GetLocationService
    }
}