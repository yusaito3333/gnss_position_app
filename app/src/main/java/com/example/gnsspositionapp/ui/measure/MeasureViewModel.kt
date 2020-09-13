package com.example.gnsspositionapp.ui.measure

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.data.data
import com.example.gnsspositionapp.usecase.measure.FinishMeasureLocationUseCase
import com.example.gnsspositionapp.usecase.measure.GetLocationCountUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class MeasureViewModel
    @ViewModelInject constructor(
        locationCountUseCase: GetLocationCountUseCase,
        private val finishMeasuringUseCase : FinishMeasureLocationUseCase
) : ViewModel() {

    val locationName = MutableLiveData("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val tempLocationsCount = locationCountUseCase(Unit)
        .onStart { emit(Result.Success(0)) }
        .map { it.data }
        .asLiveData()

    //情報を更新せずに終了
    fun forceToFinishMeasuringLocation() {
        viewModelScope.launch {
            finishMeasuringUseCase(null)
        }
    }

    fun finishMeasuringLocation() {
        viewModelScope.launch {
            Timber.d("${locationName.value}")
            finishMeasuringUseCase(locationName.value)
        }
    }
}