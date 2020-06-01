package com.example.discover.dataModel.externalId

import com.google.gson.annotations.SerializedName

data class ExternalID(

    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("facebook_id") val facebookId: String,
    @SerializedName("instagram_id") val instagramId: String,
    @SerializedName("twitter_id") val twitterId: String,
    @SerializedName("id") val id: Int
)