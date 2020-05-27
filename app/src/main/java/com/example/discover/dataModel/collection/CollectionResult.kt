package com.example.discover.dataModel.collection

import com.example.discover.dataModel.collection.Collection
import com.google.gson.annotations.SerializedName

data class CollectionResult(

    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Collection>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)