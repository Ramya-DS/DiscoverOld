package com.example.discover.dataModel.images

import com.google.gson.annotations.SerializedName

data class Images (

    @SerializedName("backdrops") val backdrops : List<ImageDetails>?,
    @SerializedName("posters") val posters : List<ImageDetails>
)