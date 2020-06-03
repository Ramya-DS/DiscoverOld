package com.example.discover.dataModel.genre

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Genre")
data class Genres(
    @PrimaryKey
    @ColumnInfo(name = "genre_id")
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)