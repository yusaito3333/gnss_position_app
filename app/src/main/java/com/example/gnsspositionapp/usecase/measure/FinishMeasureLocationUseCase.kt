package com.example.gnsspositionapp.usecase.measure

import com.example.gnsspositionapp.usecase.SuspendUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class FinishMeasureLocationUseCase
    @Inject
    constructor(
    private val repository: LocationRepository
) : SuspendUseCase<String?, Unit>(Dispatchers.Default) {
    override suspend fun execute(parameters: String?){

        parameters?.let {
            repository.addLocationInfo(it)
        }

        repository.clearLocations()
    }
}