package com.example.discover.dataModel

import com.google.gson.annotations.SerializedName

data class BelongToCollection(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("backdrop_path") val backdrop_path: String
)

