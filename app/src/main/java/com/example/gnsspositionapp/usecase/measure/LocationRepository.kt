package com.example.gnsspositionapp.usecase.measure

import android.location.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class LocationRepository
    @Inject constructor(){

    private val _channel = BroadcastChannel<Location>(Channel.BUFFERED)

    @OptIn(FlowPreview::class)
    val channel = _channel.asFlow()

    suspend fun addLocation(location: Location){
        _channel.send(location)
        Timber.tag("locationRepository").d("${location.accuracy}")
    }
}