package com.example.gnsspositionapp.di

import android.app.Service
import android.content.Context
import com.example.gnsspositionapp.usecase.send.SlackApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ActivityContext
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(ServiceComponent::class)
@Module
object ServiceModule {

    @Provides
    fun provideFusedLocationProviderClient(service: Service) : FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(service)
    }

    @Provides
    fun provideLocationRequest() : LocationRequest {
        return LocationRequest()
            .setInterval(1000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }
}