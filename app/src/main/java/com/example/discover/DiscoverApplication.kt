package com.example.discover

import android.app.Application
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.example.discover.utils.apiCalls.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiscoverApplication : Application() {

    lateinit var movieApiCall: MovieApiCall
    lateinit var showApiCall: ShowApiCall
    lateinit var discoverApiCall: DiscoverApiCall
    lateinit var searchApiCall: SearchApiCall
    lateinit var movieDetailApiCall: MovieDetailApiCall
    lateinit var showDetailApiCall: ShowDetailApiCall
    lateinit var seasonApiCall: SeasonApiCall
    lateinit var memoryCache: LruCache<String, Bitmap>

    override fun onCreate() {
        super.onCreate()

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3YWQ1NzE0ZTA2OGVhMmE0ZjJhNmRjMDVjMGViODdiNSIsInN1YiI6IjVlYmU2YTA0YmM4YWJjMDAyMWMzMTdhYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.9Ar7uY2Ktj8uEMQUrRHKXrkXGIbqlDtRoFSq_Rbw1vo"
                )
                .build()
            chain.proceed(newRequest)
        }.build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieApiCall = retrofit.create(MovieApiCall::class.java)
        showApiCall = retrofit.create(ShowApiCall::class.java)
        discoverApiCall = retrofit.create(DiscoverApiCall::class.java)
        searchApiCall = retrofit.create(SearchApiCall::class.java)
        movieDetailApiCall = retrofit.create(MovieDetailApiCall::class.java)
        showDetailApiCall = retrofit.create(ShowDetailApiCall::class.java)
        seasonApiCall = retrofit.create(SeasonApiCall::class.java)

        memoryCache = accessCache()
    }

    private fun accessCache(): LruCache<String, Bitmap> {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        return object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

}