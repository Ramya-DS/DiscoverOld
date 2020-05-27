package com.example.discover.dataModel.multiSearch

import com.example.discover.dataModel.genre.Genres
import com.example.discover.dataModel.multiSearch.KnownFor
import com.google.gson.annotations.SerializedName

data class MultiSearch(
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("media_type") val media_type: String,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val vote_average: Float,
    @SerializedName("first_air_date") val first_air_date: String,
    @SerializedName("profile_path") val profile_path: String,
    @SerializedName("known_for") val known_for: List<KnownFor>,
    @SerializedName("name") val name: String
)