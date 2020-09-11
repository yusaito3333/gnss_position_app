package com.example.gnsspositionapp.usecase.send

import com.example.gnsspositionapp.data.SlackApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*

interface SlackApiService {
    @Multipart
    @Headers("Content-Type: multipart/form-data;")
    @POST("/api/files.upload")
    suspend fun uploadCSVFiles(
        @Part("token") token : RequestBody,
        @Part("channels") channel : RequestBody,
        @Part body : MultipartBody.Part) : ResponseBody
}