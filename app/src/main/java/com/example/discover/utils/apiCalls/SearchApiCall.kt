package com.example.discover.utils.apiCalls

import com.example.discover.dataModel.collection.CollectionResult
import com.example.discover.dataModel.multiSearch.MultiSearchResult
import com.example.discover.dataModel.moviePreview.MovieResult
import com.example.discover.dataModel.tvPreview.TvResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface SearchApiCall {

    @GET("search/multi")
    fun multiSearch(@QueryMap parameters: Map<String, String>): Call<MultiSearchResult>

    @GET("search/movie")
    fun movieSearch(@QueryMap parameters: Map<String, String>): Call<MovieResult>

    @GET("search/tv")
    fun showsSearch(@QueryMap parameters: Map<String, String>): Call<TvResult>

    @GET("search/collection")
    fun collectionsSearch(@QueryMap parameters: Map<String, String>): Call<CollectionResult>
}