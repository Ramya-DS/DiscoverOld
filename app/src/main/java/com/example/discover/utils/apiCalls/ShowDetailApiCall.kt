package com.example.discover.utils.apiCalls

import com.example.discover.dataModel.ShowPreview.ShowsList
import com.example.discover.dataModel.credits.Credit
import com.example.discover.dataModel.externalId.ExternalID
import com.example.discover.dataModel.keywords.KeywordResult
import com.example.discover.dataModel.reviews.ReviewList
import com.example.discover.dataModel.showDetail.ShowDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowDetailApiCall {

    @GET("tv/{tv_id}")
    fun showDetails(
        @Path("tv_id") id: Int, @Query("append_to_response") value: String = "images", @Query(
            "include_image_language"
        ) values: String = "en,null"
    ): Call<ShowDetails>

    @GET("tv/{tv_id}/credits")
    fun getCredits(@Path("tv_id") id: Int): Call<Credit>

    @GET("tv/{tv_id}/keywords")
    fun getExternalIds(@Path("tv_id") id: Int): Call<ExternalID>

    @GET("tv/{tv_id}/keywords")
    fun getKeywords(@Path("tv_id") id: Int): Call<KeywordResult>

    @GET("tv/{tv_id}/recommendations")
    fun getRecommendations(@Path("tv_id") id: Int): Call<ShowsList>

    @GET("tv/{tv_id}/reviews")
    fun getReviews(@Path("tv_id") id: Int): Call<ReviewList>

    @GET("tv/{tv_id}/similar")
    fun getSimilarShows(@Path("tv_id") id: Int): Call<ShowsList>


}