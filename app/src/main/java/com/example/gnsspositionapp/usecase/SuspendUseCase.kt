package com.example.gnsspositionapp.usecase

import com.example.gnsspositionapp.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in P,R>(private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters : P) : Result<R>{
        return try {
            withContext(dispatcher) {
                execute(parameters).let {
                    Result.Success(it)
                }
            }
        }catch (ex : RuntimeException){
            Result.Error(ex)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}