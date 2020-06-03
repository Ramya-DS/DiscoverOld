package com.example.discover.database

import androidx.room.Entity

@Entity(primaryKeys = ["movie_id", "genre_id"])
data class MovieGenreCrossRef(
    val genre_id: Int,
    val movie_id: Int
)