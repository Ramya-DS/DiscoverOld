package com.example.discover.dataModel.ShowPreview

import com.google.gson.annotations.SerializedName

data class ShowsList(

    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<ShowPreview>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)