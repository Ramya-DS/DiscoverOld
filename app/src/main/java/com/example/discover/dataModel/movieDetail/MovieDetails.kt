package com.example.discover.dataModel.movieDetail

import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.discover.dataModel.genre.Genres
import com.example.discover.dataModel.images.Images
import com.example.discover.database.MovieGenreCrossRef
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Movie")
data class MovieDetails(
    @SerializedName("adult")
    val adult: Boolean,

    @SerializedName("backdrop_path")
    val backdrop_path: String,

//    @SerializedName("belongs_to_collection") val belongs_to_collection: BelongToCollection,
    @SerializedName("budget")
    val budget: Int,

    @Relation(
        parentColumn = "ownerId",
        entityColumn = "dogId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    @SerializedName("genres")
    val genres: List<Genres>,

    @SerializedName("homepage")
    val homepage: String,

    @PrimaryKey
    @SerializedName("id") val movie_id: Int,

    @SerializedName("imdb_id")
    val imdb_id: String,

    @SerializedName("original_language")
    val original_language: String,

    @SerializedName("original_title")
    val original_title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("poster_path")
    val poster_path: String,

    @SerializedName("release_date")
    val release_date: String,

    @SerializedName("revenue")
    val revenue: Int,

    @SerializedName("runtime")
    val runtime: Int,

//    @SerializedName("spoken_languages")
//    val spoken_languages: List<SpokenLanguages>,

    @SerializedName("status")
    val status: String,

    @SerializedName("tagline")
    val tagline: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("video")
    val video: Boolean,

    @SerializedName("vote_average")
    val vote_average: Double,

    @SerializedName("vote_count")
    val vote_count: Int,

    @SerializedName("images")
    val images: Images?
)

