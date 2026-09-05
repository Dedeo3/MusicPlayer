package com.app.musicplayer.data.remote.config

import com.app.musicplayer.data.remote.service.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val networkModule = module {
        val url = "https://itunes.apple.com/"
        single {
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        }
        single {
            OkHttpClient.Builder().addInterceptor(get<HttpLoggingInterceptor>()).build()
        }
        single {
            Retrofit.Builder()
                .baseUrl(url)
                .client(get())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}