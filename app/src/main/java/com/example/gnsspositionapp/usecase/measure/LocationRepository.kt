package com.example.gnsspositionapp.usecase.measure

import android.location.Location
import com.example.gnsspositionapp.data.LocationInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class LocationRepository
    @Inject constructor(){

    private val _locationCountChannel = BroadcastChannel<Int>(Channel.BUFFERED)

    @OptIn(FlowPreview::class)
    val locationCountChannel = _locationCountChannel.asFlow()

    private val _locationChannel = BroadcastChannel<Location>(Channel.BUFFERED)

    @OptIn(FlowPreview::class)
    val locationChannel = _locationChannel.asFlow()

    private var getLocationCount = 0

    val locationInfoList = mutableListOf<LocationInfo>()

    val notSavedLocations = LinkedList<LocationInfo>()

    private var prevMinAccuracyLocation : Location? = null

    private var minAccuracyLocation : Location? = null

    suspend fun addLocation(location: Location){
        getLocationCount += 1

        _locationCountChannel.send(getLocationCount)
        _locationChannel.send(location)

        //minAccuracyLocation がnullなら Float.MAX_VALUEとみなす
        if(minAccuracyLocation?.accuracy ?: Float.MAX_VALUE > location.accuracy){
            Timber.d("addLocation")
            minAccuracyLocation = location
        }

        Timber.d("${location.accuracy}")
    }

    fun addLocationInfo(locationName : String) {
        minAccuracyLocation?.let {
            Timber.d("addLocationInfo")

            val newInfo = LocationInfo(
                name = locationName,
                longitude = it.longitude,
                latitude = it.latitude,
                accuracy = it.accuracy,
                altitude = it.altitude,
                date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.time), ZoneId.systemDefault()),
                diff = prevMinAccuracyLocation?.distanceTo(it)
            )

            locationInfoList.add(newInfo)

            prevMinAccuracyLocation = it

            notSavedLocations.add(newInfo)
        }
    }

    suspend fun clearLocations() {
        getLocationCount = 0
        minAccuracyLocation = null
        _locationCountChannel.send(0)
    }

}