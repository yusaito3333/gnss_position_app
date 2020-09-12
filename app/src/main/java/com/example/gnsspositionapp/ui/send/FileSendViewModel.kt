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

class FileSendViewModel
    @ViewModelInject constructor(
        private val getCSVFileUseCase: GetCSVFileUseCase,
        private val csvFileSendUseCase: CsvFileSendUseCase
    ): ViewModel() {


    val fileLists = getCSVFileUseCase(Unit)
        .map { it.data }
        .asLiveData()

    private val selectedFileIndices = hashSetOf<Int>()

    fun sendCSVFiles() {
        viewModelScope.launch {
            csvFileSendUseCase(fileLists.value!!.filterIndexed { index, _ -> index in selectedFileIndices })
                .collect { if( it is Result.Error) Timber.e(it.exception) }
        }
    }

    fun addPosition(position : Int){
        selectedFileIndices.add(position)
    }

    fun removePosition(position: Int){
        selectedFileIndices.remove(position)
    }
}