package com.example.discover.dataModel.reviews

import com.google.gson.annotations.SerializedName

data class ReviewList(

    @SerializedName("results") val results: List<Review>
)