package com.example.discover.utils.apiCalls

import com.example.discover.dataModel.credits.Credit
import com.example.discover.dataModel.externalId.ExternalID
import com.example.discover.dataModel.showDetail.Season
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeasonApiCall {

    @GET("tv/{tv_id}/season/{season_number}")
    fun seasonDetails(
        @Path("tv_id") showId: Int, @Path("season_number") seasonNumber: Int, @Query("append_to_response") value: String = "images", @Query(
            "include_image_language"
        ) values: String = "en,null"
    ): Call<Season>

    @GET("tv/{tv_id}/season/{season_number}/credits")
    fun credits(@Path("tv_id") showId: Int, @Path("season_number") seasonNumber: Int): Call<Credit>

//    @GET("tv/{tv_id}/season/{season_number}/external_ids")
//    fun externalIds(@Path("tv_id") showId: Int, @Path("season_number") seasonNumber: Int): Call<ExternalID>
}