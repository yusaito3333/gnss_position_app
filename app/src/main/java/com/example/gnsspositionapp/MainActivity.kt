package com.example.gnsspositionapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.gnsspositionapp.data.EventObserver
import com.example.gnsspositionapp.databinding.ActivityMainBinding
import com.example.gnsspositionapp.ui.measure.OnBackPressHandler
import com.example.gnsspositionapp.ui.showIndefiniteSnackBar
import com.example.gnsspositionapp.ui.showShortSnackBar
import com.example.gnsspositionapp.usecase.measure.GetLocationService
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val serviceEventViewModel : ServiceEventViewModel by viewModels()

    private var mService : GetLocationService? = null

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val binder = iBinder as (GetLocationService.LocalBinder)
            Timber.d("bind")
            mService = binder.getService()
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            mService = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setUpToolbar()

        serviceEventViewModel.measureStartEvent.observe(this,EventObserver{
            mService!!.requestLocationUpdates()
        })

        serviceEventViewModel.measureEndEvent.observe(this,EventObserver{
            mService!!.removeLocationUpdates()
        })
    }

    override fun onStart() {
        super.onStart()

        bindService(
            Intent(this,GetLocationService::class.java),mServiceConnection,
            Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {

        mService?.let{
            unbindService(mServiceConnection)
        }
        super.onStop()
    }

    private fun setUpToolbar() {

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        val navController = navHostFragment.navController

        val appBarConf = AppBarConfiguration(setOf(R.id.measure_fragment,R.id.start_fragment))

        binding.toolbar.setupWithNavController(navController,appBarConf)
    }


    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        val target = navHost.childFragmentManager.findFragmentById(R.id.nav_host_fragment_container)

        if (target is OnBackPressHandler) {

            if (target.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }
}