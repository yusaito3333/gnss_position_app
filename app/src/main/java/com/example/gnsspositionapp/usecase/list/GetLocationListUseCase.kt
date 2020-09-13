package com.example.gnsspositionapp.usecase.list

import com.example.gnsspositionapp.data.LocationInfo
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import com.example.gnsspositionapp.usecase.measure.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetLocationListUseCase
    @Inject constructor(private val repository: LocationRepository)
    : BaseUseCase<Unit,List<LocationInfo>>(Dispatchers.Main) {
    override fun execute(parameters: Unit) = flow {
        Timber.d("locationInfoList ${repository.locationInfoList}")
        emit(
            Result.Success(
                repository.locationInfoList.asReversed()
            )
        )
    }
}