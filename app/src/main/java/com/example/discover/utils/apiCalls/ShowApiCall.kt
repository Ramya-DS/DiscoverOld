package com.example.discover.utils.apiCalls

import com.example.discover.dataModel.ShowPreview.ShowsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowApiCall {

    @GET("trending/tv/day")
    fun getTrendingShows(@Query("page") page: Int = 1): Call<ShowsList>

    @GET("tv/on_the_air")
    fun getOnAirShows(@Query("page") page: Int = 1): Call<ShowsList>

    @GET("tv/popular")
    fun getPopularShows(@Query("page") page: Int = 1): Call<ShowsList>

    @GET("tv/airing_today")
    fun getAiringTodayShows(@Query("page") page: Int = 1): Call<ShowsList>

    @GET("tv/top_rated")
    fun getTopRatedShows(@Query("page") page: Int = 1): Call<ShowsList>
}