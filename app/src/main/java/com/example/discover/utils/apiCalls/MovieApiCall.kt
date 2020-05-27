package com.example.discover.utils.apiCalls

import com.example.discover.dataModel.moviePreview.MovieResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiCall {

    @GET("trending/movie/day")
    fun getTrendingMovies(@Query("page") page: Int): Call<MovieResult>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int): Call<MovieResult>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Call<MovieResult>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int): Call<MovieResult>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Call<MovieResult>

}