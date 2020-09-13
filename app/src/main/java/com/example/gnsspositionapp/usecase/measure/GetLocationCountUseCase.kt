package com.example.gnsspositionapp.usecase.measure


import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocationCountUseCase
    @Inject constructor(
    private val repository: LocationRepository
) : BaseUseCase<Unit,Int>(Dispatchers.Default){

    override fun execute(parameters: Unit): Flow<Result<Int>> = repository.locationCountChannel
        .map{ Result.Success(it)}
}