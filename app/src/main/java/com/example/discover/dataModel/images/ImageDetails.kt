package com.example.discover.dataModel.images

import com.google.gson.annotations.SerializedName

data class ImageDetails(

    @SerializedName("aspect_ratio") val aspect_ratio: Double,
    @SerializedName("file_path") val file_path: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int
)
