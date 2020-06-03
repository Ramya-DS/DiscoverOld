package com.example.discover.dataModel.showDetail

import com.google.gson.annotations.SerializedName

data class Network(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("logo_path") val logo_path: String?,
    @SerializedName("origin_country") val origin_country: String
)