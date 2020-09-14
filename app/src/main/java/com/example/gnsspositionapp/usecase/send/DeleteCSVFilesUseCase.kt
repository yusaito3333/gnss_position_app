package com.example.gnsspositionapp.usecase.send

import android.content.Context
import com.example.gnsspositionapp.usecase.SuspendUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

class DeleteCSVFilesUseCase
    @Inject
    constructor(@ApplicationContext private val context : Context,
        private val fileRepository: LocationFileRepository) : SuspendUseCase<List<File>,Unit>(Dispatchers.IO) {
    override suspend fun execute(parameters: List<File>) {
        parameters.forEach {
            it.delete()
        }

        fileRepository.loadCSVFiles(context)
    }
}