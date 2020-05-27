package com.example.discover.discover

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.discover.DiscoverApplication
import com.example.discover.dataModel.genre.GenreResult
import com.example.discover.dataModel.genre.Genres
import com.example.discover.dataModel.moviePreview.MovieResult
import com.example.discover.dataModel.tvPreview.TvResult
import com.example.discover.section.SectionListAdapter
import com.example.discover.utils.apiCalls.DiscoverApiCall
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class DiscoverViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    var genres = MutableLiveData<List<Genres>>()

    fun getMoviesGenres() {
        val call = getDiscoverApiCall().movieGenres()
        sendRequestCall(call, true)
    }

    fun getShowsGenres() {
        val call = getDiscoverApiCall().tvGenres()
        sendRequestCall(call, false)
    }

    private fun sendRequestCall(call: Call<GenreResult>, isMovie: Boolean) {
        call.enqueue(object : Callback<GenreResult> {
            override fun onFailure(call: Call<GenreResult>, t: Throwable) {
                Log.d("Discover VM", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<GenreResult>, response: Response<GenreResult>) {
                if (response.isSuccessful) {
//                    if (isMovie) {
//                        genresMovies.value = response.body()!!.genres
////                        Log.d("genres movies", list.toString())
//                    } else {
//                        genresShows.value = response.body()!!.genres
////                        Log.d("genres shows", list.toString())
//               }
                    genres.value = response.body()!!.genres
                } else
                    Log.d("Discover VM", "Error" + fetchErrorMessage(response.errorBody()!!))
            }
        })
    }

    fun discoverMovies(parameter: Map<String, String>, adapter: SectionListAdapter) {
        val call = getDiscoverApiCall().discoverMovies(parameter)
        sendDiscoverMoviesRequestCall(call, parameter.getValue("page").toInt(), adapter)
    }

    fun discoverTv(parameter: Map<String, String>, adapter: SectionListAdapter) {
        val call = getDiscoverApiCall().discoverShows(parameter)
        sendDiscoverTvRequestCall(call, parameter.getValue("page").toInt(), adapter)
    }

    private fun sendDiscoverMoviesRequestCall(
        call: Call<MovieResult>,
        page: Int,
        adapter: SectionListAdapter
    ) {
        call.enqueue(object : Callback<MovieResult> {
            override fun onFailure(call: Call<MovieResult>, t: Throwable) {
                Log.d("MovieMain", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<MovieResult>, response: Response<MovieResult>) {
                if (response.isSuccessful) {
                    Log.d("result", response.body()!!.results.size.toString())
                    if (page == 1)
                        adapter.setMovieSectionList(response.body()!!.results)
                    else
                        adapter.appendMovieSectionList(response.body()!!.results)
                } else {
                    Log.d("MovieMain", "Error" + fetchErrorMessage(response.errorBody()!!))
                }
            }
        })
    }

    private fun sendDiscoverTvRequestCall(
        call: Call<TvResult>,
        page: Int,
        adapter: SectionListAdapter
    ) {
        call.enqueue(object : Callback<TvResult> {
            override fun onFailure(call: Call<TvResult>, t: Throwable) {
                Log.d("MovieMain", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<TvResult>, response: Response<TvResult>) {
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

    private fun getDiscoverApiCall(): DiscoverApiCall {
        return (mApplication as DiscoverApplication).discoverApiCall
    }
}