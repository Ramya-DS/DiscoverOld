package com.example.discover.dataModel.showDetail

import com.example.discover.dataModel.credits.Cast
import com.example.discover.dataModel.credits.Crew
import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("air_date") val air_date: String,
    @SerializedName("episode_number") val episode_number: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("season_number") val season_number: Int,
    @SerializedName("still_path") val still_path: String?,
    @SerializedName("vote_average") val vote_average: Double,
    @SerializedName("vote_count") val vote_count: Int,
    @SerializedName("crew") val crew: List<Crew>,
    @SerializedName("guest_stars") val guest_stars: List<Cast>
)