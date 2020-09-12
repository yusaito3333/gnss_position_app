package com.example.gnsspositionapp.usecase.send

import okhttp3.*
import retrofit2.http.*

interface SlackApiService {
    @Multipart
    @POST("/api/files.upload")
    suspend fun uploadCSVFiles(
        @Part("token") token : RequestBody,
        @Part("channels") channel : RequestBody,
        @Part file : MultipartBody.Part,
        @Part("filename") filename : RequestBody) : ResponseBody
}