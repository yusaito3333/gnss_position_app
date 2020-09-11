package com.example.gnsspositionapp.usecase.measure

import android.location.Location
import android.os.Looper
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.ui.measure.LocationCallbackWrapper
import com.example.gnsspositionapp.usecase.BaseUseCase
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetLocationUseCase
    @Inject constructor(
    /*private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest*/
    private val repository: Repository
) : BaseUseCase<Unit,Location>(Dispatchers.Default){

    /*@OptIn(ExperimentalCoroutinesApi::class)
    @Throws(SecurityException::class)
    override fun execute(parameters : Unit) = callbackFlow<Result<Location>> {

        val callback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {

                if(locationResult == null) return

                val location = locationResult.lastLocation

                Timber.d("${location.accuracy}")

                offer(
                    Result.Success(
                        location
                    )
                )

            }
        }
        val wrapper = LocationCallbackWrapper(callback)

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,wrapper, Looper.getMainLooper())

        awaitClose { fusedLocationProviderClient.removeLocationUpdates(wrapper) }
    }*/

    override fun execute(parameters: Unit): Flow<Result<Location>> = repository.channel
        .map { Result.Success(it) }
}