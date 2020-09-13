package com.example.gnsspositionapp.usecase.measure

import android.location.Location
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocationUseCase
    @Inject constructor(
    private val repository: LocationRepository
) : BaseUseCase<Unit,Location>(Dispatchers.Default){

    override fun execute(parameters: Unit): Flow<Result<Location>> = repository.channel
        .map { Result.Success(it) }
}