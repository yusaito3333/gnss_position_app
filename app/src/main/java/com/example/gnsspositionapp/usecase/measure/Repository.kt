package com.example.gnsspositionapp.usecase.measure

import android.location.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class Repository
    @Inject constructor(){

    private val _channel = BroadcastChannel<Location>(10)

    @OptIn(FlowPreview::class)
    val channel = _channel.asFlow()

    suspend fun addLocation(location: Location){
        _channel.send(location)
    }

}