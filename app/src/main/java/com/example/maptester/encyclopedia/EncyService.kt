package com.example.maptester.encyclopedia

import com.example.maptester.NaverInformation
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EncyResultService {
    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val clientBuilder = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    )

    val retrofit = Retrofit.Builder()
        .baseUrl(NaverInformation.BASE_URI)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(clientBuilder.build())
        .build()

    val encyInterface = retrofit.create(EncyInterface::class.java)
}