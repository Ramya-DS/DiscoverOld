package com.example.discover.dataModel.showDetail

import com.example.discover.dataModel.credits.Crew
import com.example.discover.dataModel.genre.Genres
import com.example.discover.dataModel.images.Images
import com.google.gson.annotations.SerializedName

data class ShowDetails(
    @SerializedName("backdrop_path") val backdrop_path: String?,
    @SerializedName("created_by") val created_by: List<Crew>,
    @SerializedName("episode_run_time") val episode_run_time: List<Int>,
    @SerializedName("first_air_date") val first_air_date: String,
    @SerializedName("genres") val genres: List<Genres>,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("id") val id: Int,
    @SerializedName("in_production") val in_production: Boolean,
    @SerializedName("languages") val languages: List<String>,
    @SerializedName("last_air_date") val last_air_date: String,
    @SerializedName("last_episode_to_air") val last_episode_to_air: Episode,
    @SerializedName("name") val name: String,
    @SerializedName("next_episode_to_air") val next_episode_to_air: Episode?,
    @SerializedName("networks") val networks: List<Network>,
    @SerializedName("number_of_episodes") val number_of_episodes: Int,
    @SerializedName("number_of_seasons") val number_of_seasons: Int,
    @SerializedName("origin_country") val origin_country: List<String>,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("original_name") val original_name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("seasons") val seasons: List<Season>,
    @SerializedName("status") val status: String,
    @SerializedName("type") val type: String,
    @SerializedName("vote_average") val vote_average: Double,
    @SerializedName("vote_count") val vote_count: Int,
    @SerializedName("images") val images: Images?
)