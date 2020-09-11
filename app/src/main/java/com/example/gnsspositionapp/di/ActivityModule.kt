package com.example.gnsspositionapp.di

import android.content.Context
import com.example.gnsspositionapp.usecase.send.SlackApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@InstallIn(ActivityComponent::class)
@Module
object ActivityModule {

    @Provides
    fun provideFusedLocationProviderClient(@ActivityContext context: Context) : FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideLocationRequest() : LocationRequest {
        return LocationRequest()
            .setInterval(1000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    @Provides
    fun provideRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl("")
            .build()
    }

    @Provides
    fun provideSlackApiService(retrofit: Retrofit) : SlackApiService {
        return retrofit.create(SlackApiService::class.java)
    }
}