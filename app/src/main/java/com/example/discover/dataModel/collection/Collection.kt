package com.example.discover.dataModel.collection

import com.google.gson.annotations.SerializedName

data class Collection(

    @SerializedName("id") val id: Int,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("name") val name: String,
    @SerializedName("poster_path") val poster_path: String
)