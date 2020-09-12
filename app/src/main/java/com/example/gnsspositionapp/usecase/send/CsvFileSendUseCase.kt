package com.example.gnsspositionapp.usecase.send

import com.example.gnsspositionapp.BuildConfig
import com.example.gnsspositionapp.data.Result
import com.example.gnsspositionapp.usecase.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class CsvFileSendUseCase
    @Inject constructor(
    private val service: SlackApiService
) : BaseUseCase<List<File>,Unit>(Dispatchers.IO) {

    companion object {
        const val MEDIA_TYPE_CSV = "text/csv"
        const val MEDIA_TYPE_MULTI_PART = "multipart/form-data"
        const val CHANNEL_NAME = "test_channel"

    }

    override fun execute(parameters: List<File>) = flow {

        emit(Result.Loading)

        val token = BuildConfig.API_KEY.toRequestBody(MEDIA_TYPE_MULTI_PART.toMediaType())

        val channels = CHANNEL_NAME.toRequestBody(MEDIA_TYPE_MULTI_PART.toMediaType())

        parameters.forEach {

            val file = MultipartBody.Part.createFormData(
                "file",
                it.name,
                it.asRequestBody(MEDIA_TYPE_CSV.toMediaType())
            )

            val filename = it.name.toRequestBody(MEDIA_TYPE_MULTI_PART.toMediaType())

            service.uploadCSVFiles(token, channels,file,filename)
        }

        emit(Result.Success(Unit))
    }
}