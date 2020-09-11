package com.example.gnsspositionapp.usecase.send

import android.content.Context
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

class GetCSVFileUseCase
    @Inject constructor(@ApplicationContext private val context : Context)
    : BaseUseCase<Unit,List<File>> (Dispatchers.IO){
    override fun execute(parameters: Unit): Flow<Result<List<File>>> = flow {
        val dir = context.getExternalFilesDir(null)
            ?: throw FileNotFoundException("External Storage is not found")

        val files = dir.listFiles() ?: throw IOException("didn't get files")

        Timber.d("$files")

        emit(
            Result.Success(
                files.toList()
            )
        )
    }
}