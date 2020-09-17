package com.example.gnsspositionapp.usecase.measure

import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CalculateAverageUseCase @Inject constructor(
    private val repository: LocationRepository
) : BaseUseCase<Unit,Float>(Dispatchers.Default) {

    companion object {
        const val BUFFER = 5
    }

    private val locationQueue = LinkedList<Float>()

    private var sum = 0F

    override fun execute(parameters: Unit): Flow<Result<Float>> = repository
        .locationChannel
        .map {
            if(locationQueue.size == BUFFER){
                sum = sum - (locationQueue.poll()) + it.accuracy

            }else{
                sum += it.accuracy
            }

            locationQueue.add(it.accuracy)

            Result.Success(sum/locationQueue.size)
        }
}