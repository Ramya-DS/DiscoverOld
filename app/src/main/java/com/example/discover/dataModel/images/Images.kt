package com.example.discover.dataModel.images

import com.google.gson.annotations.SerializedName

data class Images(
    val id: Int,
    @SerializedName("backdrops") val backdrops: List<ImageDetails>?,
    @SerializedName("posters") val posters: List<ImageDetails>?
)