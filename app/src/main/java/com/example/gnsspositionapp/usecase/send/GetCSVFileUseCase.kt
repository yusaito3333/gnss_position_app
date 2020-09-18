package com.example.gnsspositionapp.usecase.send

import android.content.Context
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class GetCSVFileUseCase
    @Inject constructor(@ApplicationContext private val context : Context,
    private val fileRepository: LocationFileRepository)
    : BaseUseCase<Unit,List<File>> (Dispatchers.IO){
    override fun execute(parameters: Unit) : Flow<Result<List<File>>> {
        fileRepository.loadCSVFiles(context)
        return fileRepository
            .fileFlow
            .map { Result.Success(it) }
    }
}