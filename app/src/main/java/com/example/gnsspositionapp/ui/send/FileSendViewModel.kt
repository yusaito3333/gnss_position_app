package com.example.gnsspositionapp.ui.send

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.data.data
import com.example.gnsspositionapp.usecase.send.CsvFileSendUseCase
import com.example.gnsspositionapp.usecase.send.GetCSVFileUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class FileSendViewModel
    @ViewModelInject constructor(
        private val getCSVFileUseCase: GetCSVFileUseCase,
        private val csvFileSendUseCase: CsvFileSendUseCase
    ): ViewModel() {


    val fileLists = getCSVFileUseCase(Unit)
        .map { it.data }
        .asLiveData()

    val selectedFileLists = arrayListOf<File>()

    fun sendCSVFiles() {
        viewModelScope.launch {
            csvFileSendUseCase(fileLists.value!!)
                .collect { if( it is Result.Error) Timber.e(it.exception) }
        }
    }
}