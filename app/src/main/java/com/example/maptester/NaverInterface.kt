package com.example.maptester

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverInterface {
    @GET("v1/search/local.json")
    fun getPlaceInformation(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int = 5,
    ): Call<PlaceResponse>
}