package com.example.discover.movie

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.discover.DiscoverApplication
import com.example.discover.dataModel.credits.Credit
import com.example.discover.dataModel.externalId.ExternalID
import com.example.discover.dataModel.keywords.KeywordResult
import com.example.discover.dataModel.movieDetail.MovieDetails
import com.example.discover.dataModel.moviePreview.MoviesList
import com.example.discover.dataModel.reviews.ReviewList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MovieViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    fun fetchCredits(id: Int, activity: MovieActivity) {
        val call = getMovieApiCall().creditDetails(id)
        sendCreditsRequest(call, activity)
    }

    private fun sendCreditsRequest(call: Call<Credit>, activity: MovieActivity) {
        call.enqueue(object : Callback<Credit> {
            override fun onFailure(call: Call<Credit>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<Credit>, response: Response<Credit>) {
                if (response.isSuccessful) {
                    activity.setCredits(response.body()!!)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun movieDetails(id: Int, activity: MovieActivity) {
        val call = getMovieApiCall().movieDetails(id)
        sendDetailsRequest(call, activity)
    }

    private fun sendDetailsRequest(call: Call<MovieDetails>, activity: MovieActivity) {
        call.enqueue(object : Callback<MovieDetails> {
            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.isSuccessful) {
                    activity.setMovieDetails(response.body()!!)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun getKeywords(id: Int, activity: MovieActivity) {
        val call = getMovieApiCall().getKeywords(id)
        sendKeywordRequest(call, activity)
    }

    private fun sendKeywordRequest(call: Call<KeywordResult>, activity: MovieActivity) {
        call.enqueue(object : Callback<KeywordResult> {
            override fun onFailure(call: Call<KeywordResult>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<KeywordResult>, response: Response<KeywordResult>) {
                if (response.isSuccessful) {
                    activity.setKeywords(response.body()!!.keywords)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun fetchRecommendations(id: Int, activity: MovieActivity) {
        val call = getMovieApiCall().getRecommendations(id)
        sendRecommendationsRequest(call, activity)
    }

    private fun sendRecommendationsRequest(call: Call<MoviesList>, activity: MovieActivity) {
        call.enqueue(object : Callback<MoviesList> {
            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.isSuccessful) {
                    Log.d("recommendations", response.body()!!.results.toString())
                    activity.setRecommendations(response.body()!!.results)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun fetchSimilarMovies(id: Int, activity: MovieActivity) {
        val call = getMovieApiCall().getSimilar(id)
        sendSimilarMoviesRequest(call, activity)
    }

    private fun sendSimilarMoviesRequest(call: Call<MoviesList>, activity: MovieActivity) {
        call.enqueue(object : Callback<MoviesList> {
            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.isSuccessful) {
                    Log.d("similar", response.body()!!.results.toString())
                    activity.setSimilarMovies(response.body()!!.results)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun fetchReviews(id: Int, activity: MovieActivity) {
        val call = getMovieApiCall().getReviews(id)
        sendReviewsRequest(call, activity)
    }

    private fun sendReviewsRequest(call: Call<ReviewList>, activity: MovieActivity) {
        call.enqueue(object : Callback<ReviewList> {
            override fun onFailure(call: Call<ReviewList>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<ReviewList>, response: Response<ReviewList>) {
                if (response.isSuccessful) {
                    Log.d("reviews", response.body()!!.results.toString())
                    activity.setReviews(response.body()!!.results)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun fetchExternalIds(id: Int, activity: MovieActivity) {
        val call = getMovieApiCall().externalIds(id)
        sendExternalIdRequest(call, activity)
    }

    private fun sendExternalIdRequest(call: Call<ExternalID>, activity: MovieActivity) {
        call.enqueue(object : Callback<ExternalID> {
            override fun onFailure(call: Call<ExternalID>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<ExternalID>, response: Response<ExternalID>) {
                if (response.isSuccessful) {
                    Log.d("reviews", response.body()!!.toString())
                    activity.setExternalIds(response.body()!!)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
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

    private fun getMovieApiCall() = (mApplication as DiscoverApplication).movieDetailApiCall
}