package com.example.discover.dataModel.moviePreview

import com.google.gson.annotations.SerializedName

data class MovieResult(

    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MoviePreview>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)