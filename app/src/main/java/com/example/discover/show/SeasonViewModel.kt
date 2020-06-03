package com.example.discover.show

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.discover.DiscoverApplication
import com.example.discover.dataModel.credits.Credit
import com.example.discover.dataModel.showDetail.Season
import com.example.discover.dataModel.showDetail.ShowDetails
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SeasonViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    fun fetchCreditDetails(showId: Int, seasonNumber: Int, activity: SeasonActivity) {
        val call = getSeasonApiCall().credits(showId, seasonNumber)
        sendCreditsRequest(call, activity)
    }

    private fun sendCreditsRequest(call: Call<Credit>, activity: SeasonActivity) {
        call.enqueue(object : Callback<Credit> {
            override fun onFailure(call: Call<Credit>, t: Throwable) {
                Log.d("ShowDetail", "Failure ${t.message} ${fetchErrorMessage(t.stackTrace)}")
            }

            override fun onResponse(call: Call<Credit>, response: Response<Credit>) {
                if (response.isSuccessful) {
                    activity.setCreditDetails(response.body()!!)
                } else Log.d("ShowDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
            }

        })
    }

    fun seasonDetails(showId: Int, seasonNumber: Int, activity: SeasonActivity) {
        val call = getSeasonApiCall().seasonDetails(showId, seasonNumber)
        sendDetailsRequest(call, activity)
    }

    private fun sendDetailsRequest(call: Call<Season>, activity: SeasonActivity) {
        call.enqueue(object : Callback<Season> {
            override fun onFailure(call: Call<Season>, t: Throwable) {
                Log.d("ShowDetail", "Failure ${t.message} ${fetchErrorMessage(t.stackTrace)}")
            }

            override fun onResponse(call: Call<Season>, response: Response<Season>) {
                if (response.isSuccessful) {
                    activity.setSeasonDetails(response.body()!!)
                } else Log.d("ShowDetail", "Error ${fetchErrorMessage(response.errorBody()!!)}")
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

    private fun getSeasonApiCall() = (mApplication as DiscoverApplication).seasonApiCall
}
