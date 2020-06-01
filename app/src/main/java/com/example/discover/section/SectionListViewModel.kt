package com.example.discover.section

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.discover.DiscoverApplication
import com.example.discover.dataModel.moviePreview.MoviesList
import com.example.discover.dataModel.ShowPreview.ShowsList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SectionListViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    fun setTrendingShows(page: Int = 1, adapter: SectionListAdapter) {
        val call = getTvApiCall().getTrendingShows(page)
        sendTvRequestCall(call, page, adapter)
    }

    fun setOnAirShows(page: Int = 1, adapter: SectionListAdapter) {
        val call = getTvApiCall().getOnAirShows(page)
        sendTvRequestCall(call, page, adapter)
    }

    fun setPopularShows(page: Int = 1, adapter: SectionListAdapter) {
        val call = getTvApiCall().getPopularShows(page)
        sendTvRequestCall(call, page, adapter)
    }

    fun setTopRatedShows(page: Int = 1, adapter: SectionListAdapter) {
        val call = getTvApiCall().getTopRatedShows(page)
        sendTvRequestCall(call, page, adapter)
    }

    fun setAiringTodayShows(page: Int = 1, adapter: SectionListAdapter) {
        val call = getTvApiCall().getAiringTodayShows(page)
        sendTvRequestCall(call, page, adapter)

    }

    private fun getTvApiCall() = (mApplication as DiscoverApplication).tvApiCall

    fun setTrendingMovies(page: Int = 1, adapter: SectionListAdapter) {
        val call = getMovieApiCall().getTrendingMovies(page)
        sendRequestCall(call, page, adapter)
    }

    private fun sendTvRequestCall(call: Call<ShowsList>, page: Int, adapter: SectionListAdapter) {
        call.enqueue(object : Callback<ShowsList> {
            override fun onFailure(call: Call<ShowsList>, t: Throwable) {
                Log.d("MovieMain", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<ShowsList>, response: Response<ShowsList>) {
                if (response.isSuccessful) {
                    Log.d("result", response.body()!!.results.size.toString())
                    if (page == 1)
                        adapter.setTvSectionList(response.body()!!.results)
                    else
                        adapter.appendTvSectionList(response.body()!!.results)
                } else {
                    Log.d("MovieMain", "Error" + fetchErrorMessage(response.errorBody()!!))
                }
            }
        })
    }


    fun setNowPlayingMovies(page: Int = 1, adapter: SectionListAdapter) {
        val call = getMovieApiCall().getNowPlayingMovies(page)
        sendRequestCall(call, page, adapter)
    }

    fun setPopularMovies(page: Int = 1, adapter: SectionListAdapter) {
        val call = getMovieApiCall().getPopularMovies(page)
        sendRequestCall(call, page, adapter)
    }

    fun setTopRatedMovies(page: Int = 1, adapter: SectionListAdapter) {
        val call = getMovieApiCall().getTopRatedMovies(page)
        sendRequestCall(call, page, adapter)
    }

    fun setUpcomingMovies(page: Int = 1, adapter: SectionListAdapter) {
        val call = getMovieApiCall().getUpcomingMovies(page)
        sendRequestCall(call, page, adapter)

    }

    private fun sendRequestCall(call: Call<MoviesList>, page: Int, adapter: SectionListAdapter) {
        call.enqueue(object : Callback<MoviesList> {
            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.d("MovieMain", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.isSuccessful) {
                    Log.d("result", response.body()!!.results.size.toString())
                    if (page == 1)
                        adapter.setMovieSectionList(response.body()!!.results)
                    else
                        adapter.appendMovieSectionList(response.body()!!.results)
//
                } else {
                    Log.d("MovieMain", "Error" + fetchErrorMessage(response.errorBody()!!))
                }
            }
        })
    }

    private fun getMovieApiCall() = (mApplication as DiscoverApplication).movieApiCall


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
}