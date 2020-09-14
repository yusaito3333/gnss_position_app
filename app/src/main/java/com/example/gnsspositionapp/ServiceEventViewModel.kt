package com.example.gnsspositionapp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gnsspositionapp.data.Event

class ServiceEventViewModel
    @ViewModelInject constructor(): ViewModel() {

    val measureStartEvent = MutableLiveData<Event<Unit>>()

    val measureEndEvent = MutableLiveData<Event<Unit>>()

    fun measureStart() {
        measureStartEvent.value = Event(Unit)
    }

    fun measureEnd() {
        measureEndEvent.value = Event(Unit)
    }
}