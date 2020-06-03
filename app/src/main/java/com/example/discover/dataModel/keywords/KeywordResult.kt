package com.example.discover.dataModel.keywords

import com.google.gson.annotations.SerializedName

data class KeywordResult(
    @SerializedName("keywords") val keywords: List<Keyword>
)