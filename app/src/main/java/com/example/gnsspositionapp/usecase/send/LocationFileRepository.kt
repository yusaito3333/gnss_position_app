package com.example.gnsspositionapp.usecase.send

import android.content.Context
import android.net.Uri
import com.example.gnsspositionapp.data.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationFileRepository
    @Inject constructor(){

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _filesFlow = MutableStateFlow<List<File>>(listOf())

    val fileFlow = _filesFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadCSVFiles(context: Context) {
        val dir = context.getExternalFilesDir(null)
            ?: throw FileNotFoundException("External Storage is not found")

        val files = dir.listFiles() ?: throw IOException("didn't get files")

        Timber.d("$files")

        _filesFlow.value = files.toList()
    }
}