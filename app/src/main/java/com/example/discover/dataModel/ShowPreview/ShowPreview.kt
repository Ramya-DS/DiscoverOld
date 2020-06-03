package com.example.discover.dataModel.ShowPreview

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ShowPreview(
    @ColumnInfo(name = "original_name") @SerializedName("original_name") val original_name: String,
    @ColumnInfo(name = "id") @SerializedName("id") val id: Int,
    @ColumnInfo(name = "name") @SerializedName("name") val name: String,
    @ColumnInfo(name = "vote_count") @SerializedName("vote_count") val vote_count: Int,
    @ColumnInfo(name = "vote_average") @SerializedName("vote_average") val vote_average: Double,
    @ColumnInfo(name = "first_air_date") @SerializedName("first_air_date") val first_air_date: String,
    @ColumnInfo(name = "poster_path") @SerializedName("poster_path") val poster_path: String?,
    @ColumnInfo(name = "genre_ids") @SerializedName("genre_ids") val genre_ids: List<Int>,
    @ColumnInfo(name = "original_language") @SerializedName("original_language") val original_language: String,
    @ColumnInfo(name = "backdrop_path") @SerializedName("backdrop_path") val backdrop_path: String?,
    @ColumnInfo(name = "overview") @SerializedName("overview") val overview: String,
    @ColumnInfo(name = "origin_country") @SerializedName("origin_country") val origin_country: List<String>,
    @ColumnInfo(name = "popularity") @SerializedName("popularity") val popularity: Double
) : Serializable, Parcelable