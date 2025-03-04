package com.seifmortada.applications.aroundegypt.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.seifmortada.applications.aroundegypt.core.data.network.api.ApiConstant.BASE_URL
import com.seifmortada.applications.aroundegypt.core.data.network.api.ExperienceService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val networkModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }
    single {
        get<Retrofit>().create(ExperienceService::class.java)
    }

}