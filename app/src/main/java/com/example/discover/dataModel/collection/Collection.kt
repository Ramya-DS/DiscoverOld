package com.example.discover.dataModel.collection

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Collection(

    @SerializedName("id") val id: Int,
    @SerializedName("backdrop_path") val backdrop_path: String,
    @SerializedName("name") val name: String,
    @SerializedName("poster_path") val poster_path: String
) : Parcelable