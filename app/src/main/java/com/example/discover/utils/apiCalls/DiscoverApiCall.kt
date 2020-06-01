package com.example.discover.utils.apiCalls

import com.example.discover.dataModel.genre.GenreResult
import com.example.discover.dataModel.moviePreview.MoviesList
import com.example.discover.dataModel.ShowPreview.ShowsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DiscoverApiCall {

    @GET("discover/movie")
    fun discoverMovies(@QueryMap parameters: Map<String, String>): Call<MoviesList>

    @GET("discover/tv")
    fun discoverShows(@QueryMap parameters: Map<String, String>): Call<ShowsList>

    @GET("genre/movie/list")
    fun movieGenres():Call<GenreResult>

    @GET("genre/tv/list")
    fun tvGenres():Call<GenreResult>
}