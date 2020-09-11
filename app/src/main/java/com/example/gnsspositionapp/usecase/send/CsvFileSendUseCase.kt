package com.example.gnsspositionapp.usecase.send

import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class CsvFileSendUseCase
    @Inject constructor(
    private val service: SlackApiService
) : BaseUseCase<List<File>,Unit>(Dispatchers.IO) {

    companion object {
        const val MEDIA_TYPE_CSV = "text/csv"
    }

    override fun execute(parameters: List<File>) = flow {

        val token = RequestBody.create(MediaType.parse("multipart/form-data"),"")

        val channel = RequestBody.create(
            MediaType.parse("multipart/form-data"), "test_channel")

        val parts = parameters.forEach {
            val part = MultipartBody.Part.createFormData(
                "file",
                it.name,
                RequestBody.create(MediaType.parse(MEDIA_TYPE_CSV),it)
            )
            val result = service.uploadCSVFiles(token,channel,part)

            Timber.d("${result.string()}")
        }

        emit(Result.Success(Unit))
    }
}