package com.example.discover.utils.apiCalls

import com.example.discover.dataModel.genre.GenreResult
import com.example.discover.dataModel.moviePreview.MovieResult
import com.example.discover.dataModel.tvPreview.TvResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DiscoverApiCall {

    @GET("discover/movie")
    fun discoverMovies(@QueryMap parameters: Map<String, String>): Call<MovieResult>

    @GET("discover/tv")
    fun discoverShows(@QueryMap parameters: Map<String, String>): Call<TvResult>

    @GET("genre/movie/list")
    fun movieGenres():Call<GenreResult>

    @GET("genre/tv/list")
    fun tvGenres():Call<GenreResult>
}