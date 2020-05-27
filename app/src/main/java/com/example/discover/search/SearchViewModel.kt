package com.example.discover.search

import android.app.Application
import android.util.Log
import android.widget.Adapter
import androidx.lifecycle.AndroidViewModel
import com.example.discover.DiscoverApplication
import com.example.discover.dataModel.collection.CollectionResult
import com.example.discover.dataModel.moviePreview.MovieResult
import com.example.discover.dataModel.multiSearch.MultiSearchResult
import com.example.discover.dataModel.tvPreview.TvResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SearchViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {


    val queryMap = HashMap<String, String>().apply { put("page", "1") }
    var type = "multi-search"

    fun getSearchedMovies(activity: SearchActivity) {
        val call = getSearchApiCall().movieSearch(queryMap)
        sendMovieRequestCall(call, activity)
    }

    fun getSearchedShows(activity: SearchActivity) {
        val call = getSearchApiCall().showsSearch(queryMap)
        sendShowsRequestCall(call, activity)
    }

    fun getSearchedCollections(activity: SearchActivity) {
        val call = getSearchApiCall().collectionsSearch(queryMap)
        sendCollectionRequestCall(call, activity)
    }

    fun getMultiSearchResult(activity: SearchActivity) {
        val call = getSearchApiCall().multiSearch(queryMap)
        sendMultiSearchRequestCall(call, activity)
    }

    private fun sendMovieRequestCall(call: Call<MovieResult>, activity: SearchActivity) {
        call.enqueue(object : Callback<MovieResult> {
            override fun onFailure(call: Call<MovieResult>, t: Throwable) {
                Log.d("SearchActivity", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<MovieResult>, response: Response<MovieResult>) {
                if (response.isSuccessful) {
                    val movieResult = response.body()!!
                    if (movieResult.total_results == 0)
                        activity.displayNoResultFragment()
                    else if (movieResult.page == 1)
                        activity.firstPageOfMovies(movieResult.results)
                    else
                        activity.displayRestMovies(movieResult.results)
                } else
                    Log.d("Search", "Error" + fetchErrorMessage(response.errorBody()!!))
            }

        })

    }

    private fun sendShowsRequestCall(call: Call<TvResult>, activity: SearchActivity) {
        call.enqueue(object : Callback<TvResult> {
            override fun onFailure(call: Call<TvResult>, t: Throwable) {
                Log.d("SearchActivity", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<TvResult>, response: Response<TvResult>) {
                if (response.isSuccessful) {
                    val tvResult = response.body()!!
                    if (tvResult.total_results == 0)
                        activity.displayNoResultFragment()
                    else if (tvResult.page == 1)
                        activity.firstPageOfShows(tvResult.results)
                    else
                        activity.displayRestShows(tvResult.results)
                } else
                    Log.d("Search", "Error" + fetchErrorMessage(response.errorBody()!!))
            }

        })

    }

    private fun sendCollectionRequestCall(call: Call<CollectionResult>, activity: SearchActivity) {
        call.enqueue(object : Callback<CollectionResult> {
            override fun onFailure(call: Call<CollectionResult>, t: Throwable) {
                Log.d("SearchActivity", "Failure ${t.message}")
            }

            override fun onResponse(
                call: Call<CollectionResult>,
                response: Response<CollectionResult>
            ) {
                if (response.isSuccessful) {
                    val collectionResult = response.body()!!
                    if (collectionResult.total_results == 0)
                        activity.displayNoResultFragment()
                    else if (collectionResult.page == 1)
                        activity.firstPageOfCollections(collectionResult.results)
                    else
                        activity.displayRestCollections(collectionResult.results)
                } else
                    Log.d("Search", "Error" + fetchErrorMessage(response.errorBody()!!))
            }

        })

    }

    private fun sendMultiSearchRequestCall(
        call: Call<MultiSearchResult>,
        activity: SearchActivity
    ) {
        call.enqueue(object : Callback<MultiSearchResult> {
            override fun onFailure(call: Call<MultiSearchResult>, t: Throwable) {
                Log.d("SearchActivity", "Failure ${t.message}")
            }

            override fun onResponse(
                call: Call<MultiSearchResult>,
                response: Response<MultiSearchResult>
            ) {
                if (response.isSuccessful) {
                    val multiSearchResult = response.body()!!
                    if (multiSearchResult.total_results == 0)
                        activity.displayNoResultFragment()
                    else if (multiSearchResult.page == 1)
                        activity.firstPageOfMultiSearch(multiSearchResult.results)
                    else
                        activity.displayRestMultiSearch(multiSearchResult.results)
                } else
                    Log.d("Search", "Error" + fetchErrorMessage(response.errorBody()!!))
            }

        })

    }

    fun fetchErrorMessage(error: ResponseBody): String {
        var reader: BufferedReader? = null
        val sb = StringBuilder()
        try {
            reader =
                BufferedReader(InputStreamReader(error.byteStream()))
            var line: String? = ""
            try {
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return sb.toString()
    }

    private fun getSearchApiCall() = (mApplication as DiscoverApplication).searchApiCall
}