package com.example.gnsspositionapp.ui.send

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gnsspositionapp.data.Event
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.data.data
import com.example.gnsspositionapp.usecase.send.CsvFileSendUseCase
import com.example.gnsspositionapp.usecase.send.DeleteCSVFilesUseCase
import com.example.gnsspositionapp.usecase.send.GetCSVFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

class FileSendViewModel
    @ViewModelInject constructor(
        getCSVFileUseCase: GetCSVFileUseCase,
        private val csvFileSendUseCase: CsvFileSendUseCase,
        private val deleteCSVFilesUseCase: DeleteCSVFilesUseCase
    ): ViewModel() {

    val sendFinishedEvent = MutableLiveData<Event<Unit>>()

    val sendErrorEvent = MutableLiveData<Event<Unit>>()

    val sharedFile = MutableLiveData<Event<File>>()

    val fileLists = getCSVFileUseCase(Unit)
        .map { it.data }
        .asLiveData()

    private val selectedFileIndices = hashSetOf<Int>()

    fun sendCSVFiles() {
        viewModelScope.launch {
            csvFileSendUseCase(fileLists.value!!.filterIndexed { index, _ -> index in selectedFileIndices })
                .collect {
                    when(it) {
                        is Result.Success -> sendFinishedEvent.value = Event(Unit)

                        is Result.Error -> {
                            sendErrorEvent.value = Event(Unit)
                            Timber.e(it.exception)
                        }
                    }

                }
        }
    }

    fun deleteCSVFiles() {
        viewModelScope.launch {
            deleteCSVFilesUseCase(fileLists.value!!.filterIndexed{index, _ -> index in selectedFileIndices })
            selectedFileIndices.clear()
        }
    }

    fun shareCSVFiles() {
        viewModelScope.launch {
            val file = withContext(Dispatchers.Default){
                fileLists.value?.filterIndexed { index, _ -> index in selectedFileIndices }
                    ?.firstOrNull()
            }

            file?.let{
                sharedFile.value = Event(file)
            }
        }
    }

    fun addPosition(position : Int){
        selectedFileIndices.add(position)
    }

    fun removePosition(position: Int){
        selectedFileIndices.remove(position)
    }
}