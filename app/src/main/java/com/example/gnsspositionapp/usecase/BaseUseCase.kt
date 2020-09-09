package com.example.gnsspositionapp.usecase

import com.example.gnsspositionapp.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class BaseUseCase<in P,R>(private val dispatcher : CoroutineDispatcher) {

    operator fun invoke(parameters : P) : Flow<Result<R>> = execute(parameters)
        .catch { e -> emit(Result.Error(Exception(e))) }
        .flowOn(dispatcher)

    protected abstract fun execute(parameters: P): Flow<Result<R>>
}