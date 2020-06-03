package com.example.discover.dataModel.credits

import com.google.gson.annotations.SerializedName

data class Credit(

    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<Cast>,
    @SerializedName("crew") val crew: List<Crew>
)