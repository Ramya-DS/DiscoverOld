package com.example.discover.dataModel.tvPreview

import com.example.discover.dataModel.tvPreview.TvPreview
import com.google.gson.annotations.SerializedName

data class TvResult(

    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<TvPreview>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)