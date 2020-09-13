package com.example.gnsspositionapp.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gnsspositionapp.data.Event
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.data.data
import com.example.gnsspositionapp.usecase.list.GetLocationListUseCase
import com.example.gnsspositionapp.usecase.list.SaveLocationUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LocationListViewModel
    @ViewModelInject constructor(
        private val saveLocationUseCase: SaveLocationUseCase,
        getLocationListUseCase: GetLocationListUseCase
    ) : ViewModel(){

    val saveFinishedEvent = MutableLiveData<Event<Unit>>()

    val saveStartEvent = MutableLiveData<Event<Unit>>()

    val unitIsYard = MutableLiveData(false)

    val locations = getLocationListUseCase(Unit)
        .map { it.data }
        .asLiveData()

    fun saveLocations() {
        viewModelScope.launch{
            saveLocationUseCase(Unit)
                .collect {
                    when(it){
                        is Result.Success -> saveFinishedEvent.value = Event(Unit)
                        is Result.Loading -> saveStartEvent.value = Event(Unit)
                    }
                }
        }
    }
}