package com.example.gnsspositionapp.ui.measure

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gnsspositionapp.data.LocationInfo
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.data.data
import com.example.gnsspositionapp.usecase.measure.GetLocationUseCase
import com.example.gnsspositionapp.usecase.measure.SaveLocationUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import timber.log.Timber
import java.util.*

class MeasureViewModel
    @ViewModelInject constructor(
    private val locationUseCase: GetLocationUseCase,
    private val saveLocationUseCase: SaveLocationUseCase
) : ViewModel() {

    val saveFinishedEvent = MutableLiveData<Unit>()

    val savingEvent = MutableLiveData<Unit>()

    val unitIsYard = MutableLiveData(false)

    val locationName = MutableLiveData("")

    val locations = MutableLiveData<List<LocationInfo>>(listOf())

    private var tempLocations = mutableListOf<Location>()

    val tempLocationsCount = MutableLiveData(0)

    private val notSavedLocations = LinkedList<LocationInfo>()

    private var prevHorizontalAccuracy : Location? = null

    private var measureJob : Job? = null

    fun startMeasuringLocation() {

        measureJob?.cancel()

        measureJob = viewModelScope.launch {

            locationUseCase(Unit)
                .collect{result ->
                    result.data?.let{
                        tempLocations.add(it)
                        tempLocationsCount.value = tempLocationsCount.value!! + 1
                    }
                }
        }
    }
    //情報を更新せずに終了
    fun forceToFinishMeasuringLocation() {
        measureJob?.cancel()
        init()
    }

    fun finishMeasuringLocation() {
        viewModelScope.launch {

            //位置情報取得の終了
            measureJob?.cancel()

            //精度が最も高いものを選択
            calcMinAccuracy()

            //採取数と採取した位置情報をクリア
            init()
        }
    }

    fun saveLocations()  {
        viewModelScope.launch {
            saveLocationUseCase(notSavedLocations)
                .collect {
                    when(it){
                        is Result.Success -> saveFinishedEvent.value = Unit
                        is Result.Loading -> savingEvent.value = Unit
                        is Result.Error -> Timber.e(it.exception)
                    }
                }
        }
    }

    //ここで精度が最も大きいものを選択し、前回採取点と置き換える
    private suspend fun calcMinAccuracy() = withContext(Dispatchers.Default){
        val minAccuracyInfo = tempLocations.minByOrNull { it.accuracy }

        minAccuracyInfo?.let {

            val newInfo = LocationInfo(
                locationName.value!!,
                date = LocalDateTime.ofInstant(Instant.ofEpochMilli(minAccuracyInfo.time), ZoneId.systemDefault()),
                longitude = minAccuracyInfo.longitude,
                latitude = minAccuracyInfo.latitude,
                accuracy = minAccuracyInfo.accuracy,
                diff = prevHorizontalAccuracy?.distanceTo(minAccuracyInfo),
                altitude = minAccuracyInfo.altitude
            )

            locations.postValue(locations.value?.plus(newInfo))

            notSavedLocations.add(newInfo)

            Timber.d("$prevHorizontalAccuracy")
            Timber.d("$minAccuracyInfo")
            Timber.d("$newInfo")

            //置き換え
            prevHorizontalAccuracy = minAccuracyInfo
        }
    }

    private fun init() {
        tempLocationsCount.value = 0
        //取得したものを捨てる
        tempLocations.clear()
    }
}