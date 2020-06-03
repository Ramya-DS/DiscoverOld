package com.example.discover.show

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.discover.DiscoverApplication
import com.example.discover.dataModel.ShowPreview.ShowsList
import com.example.discover.dataModel.credits.Credit
import com.example.discover.dataModel.externalId.ExternalID
import com.example.discover.dataModel.keywords.KeywordResult
import com.example.discover.dataModel.reviews.ReviewList
import com.example.discover.dataModel.showDetail.ShowDetails
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ShowViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    fun showDetails(id: Int, activity: ShowActivity) {
        val call = getShowApiCall().showDetails(id)
        sendDetailsRequest(call, activity)
    }

    private fun sendDetailsRequest(call: Call<ShowDetails>, activity: ShowActivity) {
        call.enqueue(object : Callback<ShowDetails> {
            override fun onFailure(call: Call<ShowDetails>, t: Throwable) {
                Log.d("ShowDetail", "Failure ${t.message} ${fetchErrorMessage(t.stackTrace)}")
            }

            override fun onResponse(call: Call<ShowDetails>, response: Response<ShowDetails>) {
                if (response.isSuccessful) {
                    activity.setShowDetails(response.body()!!)
                } else Log.d("ShowDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun fetchCredits(id: Int, activity: ShowActivity) {
        val call = getShowApiCall().getCredits(id)
        sendCreditsRequest(call, activity)
    }

    private fun sendCreditsRequest(call: Call<Credit>, activity: ShowActivity) {
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

    fun fetchKeywords(id: Int, activity: ShowActivity) {
        val call = getShowApiCall().getKeywords(id)
        sendKeywordRequest(call, activity)
    }

    private fun sendKeywordRequest(call: Call<KeywordResult>, activity: ShowActivity) {
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

    fun fetchExternalIds(id: Int, activity: ShowActivity) {
        val call = getShowApiCall().getExternalIds(id)
        sendExternalIdsRequest(call, activity)
    }

    private fun sendExternalIdsRequest(call: Call<ExternalID>, activity: ShowActivity) {
        call.enqueue(object : Callback<ExternalID> {
            override fun onFailure(call: Call<ExternalID>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<ExternalID>, response: Response<ExternalID>) {
                if (response.isSuccessful) {
                    activity.setExternalIds(response.body()!!)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun fetchRecommendations(id: Int, activity: ShowActivity) {
        val call = getShowApiCall().getRecommendations(id)
        sendRecommendationsRequest(call, activity)
    }

    private fun sendRecommendationsRequest(call: Call<ShowsList>, activity: ShowActivity) {
        call.enqueue(object : Callback<ShowsList> {
            override fun onFailure(call: Call<ShowsList>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<ShowsList>, response: Response<ShowsList>) {
                if (response.isSuccessful) {
                    activity.setRecommendations(response.body()!!.results)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun fetchSimilarShows(id: Int, activity: ShowActivity) {
        val call = getShowApiCall().getSimilarShows(id)
        sendSimilarShowsRequest(call, activity)
    }

    private fun sendSimilarShowsRequest(call: Call<ShowsList>, activity: ShowActivity) {
        call.enqueue(object : Callback<ShowsList> {
            override fun onFailure(call: Call<ShowsList>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<ShowsList>, response: Response<ShowsList>) {
                if (response.isSuccessful) {
                    activity.setSimilarShows(response.body()!!.results)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun fetchReviews(id: Int, activity: ShowActivity) {
        val call = getShowApiCall().getReviews(id)
        sendReviewRequest(call, activity)
    }

    private fun sendReviewRequest(call: Call<ReviewList>, activity: ShowActivity) {
        call.enqueue(object : Callback<ReviewList> {
            override fun onFailure(call: Call<ReviewList>, t: Throwable) {
                Log.d("MovieDetail", "Failure ${t.message}")
            }

            override fun onResponse(call: Call<ReviewList>, response: Response<ReviewList>) {
                if (response.isSuccessful) {
                    activity.setReviews(response.body()!!.results)
                } else Log.d("MovieDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }


    private fun fetchErrorMessage(error: ResponseBody): String {
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

    private fun fetchErrorMessage(error: Array<StackTraceElement>): String {
        var sb = " "
        for (i in error)
            sb += "${i.className} ${i.fileName} ${i.isNativeMethod} ${i.methodName}\n"
        return sb
    }

    private fun getShowApiCall() = (mApplication as DiscoverApplication).showDetailApiCall
}