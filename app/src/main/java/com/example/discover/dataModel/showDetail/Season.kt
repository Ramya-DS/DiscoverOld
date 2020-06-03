package com.example.discover.dataModel.showDetail

import com.example.discover.dataModel.images.Images
import com.google.gson.annotations.SerializedName

data class Season(
    @SerializedName("air_date") val air_date: String,
    @SerializedName("episode_count") val episode_count: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("season_number") val season_number: Int,
    @SerializedName("episodes") val episodes: List<Episode>,
    @SerializedName("images") val images: Images?
)