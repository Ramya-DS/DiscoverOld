package com.example.discover.dataModel.moviePreview

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class MoviePreview(
    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    val adult: Boolean,

    @ColumnInfo(name = "backdrop_path")
    @SerializedName(
        "backdrop_path"
    ) val backdrop_path: String,

    @ColumnInfo(name = "genre_ids")
    @SerializedName("genre_ids")
    val genre_ids: List<Int>,

    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    val original_language: String,

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    val original_title: String,

    @ColumnInfo(name = "adult")
    @SerializedName("overview")
    val overview: String,

    @ColumnInfo(name = "overview")
    @SerializedName("poster_path")
    val poster_path: String?,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    val release_date: String,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,

    @ColumnInfo(name = "video")
    @SerializedName("video")
    val video: Boolean,

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    val vote_average: Double,

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    val vote_count: Int,

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    val popularity: Double

) : Serializable, Parcelable

