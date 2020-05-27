package com.example.discover.dataModel.multiSearch

import com.google.gson.annotations.SerializedName

data class KnownFor(
    @SerializedName("original_title") val original_title: String,
    @SerializedName("media_type") val media_type: String,
    @SerializedName("original_name") val original_name: String
)