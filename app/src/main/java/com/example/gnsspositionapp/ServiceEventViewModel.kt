package com.example.gnsspositionapp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServiceEventViewModel
    @ViewModelInject constructor(): ViewModel() {

    val measureStartEvent = MutableLiveData<Unit>()

    val measureEndEvent = MutableLiveData<Unit>()

    fun measureStart() {
        measureStartEvent.value = Unit
    }

    fun measureEnd() {
        measureEndEvent.value = Unit
    }

}