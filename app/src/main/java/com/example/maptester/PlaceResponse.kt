package com.example.maptester

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("items") val items: List<SearchItem>
)

data class SearchItem(
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("address") val address: String,
    @SerializedName("roadAddress") val roadAddress: String,
    @SerializedName("mapx") val mapx: Int,
    @SerializedName("mapy") val mapy: Int
)



