package com.example.discover.utils

import com.example.discover.dataModel.tvPreview.TvResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TvApiCall {

    @GET("trending/tv/day")
    fun getTrendingShows(@Query("page") page: Int = 1): Call<TvResult>

    @GET("tv/on_the_air")
    fun getOnAirShows(@Query("page") page: Int = 1): Call<TvResult>

    @GET("tv/popular")
    fun getPopularShows(@Query("page") page: Int = 1): Call<TvResult>

    @GET("tv/airing_today")
    fun getAiringTodayShows(@Query("page") page: Int = 1): Call<TvResult>

    @GET("tv/top_rated")
    fun getTopRatedShows(@Query("page") page: Int = 1): Call<TvResult>
}