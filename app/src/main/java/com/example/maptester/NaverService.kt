package com.example.maptester

import com.example.maptester.NaverInformation.Companion.BASE_URI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NaverService {
    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val clientBuilder = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    )

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URI)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(clientBuilder.build())
        .build()

    val naverInterface = retrofit.create(NaverInterface::class.java)
}