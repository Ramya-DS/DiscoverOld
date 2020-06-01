package com.example.discover.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import com.example.discover.dataModel.genre.Genres
import com.example.discover.dataModel.keywords.Keyword
import com.google.android.material.chip.Chip

class ChipAdapter(private val isGenre: Boolean) :
    RecyclerView.Adapter<ChipAdapter.GenreViewHolder>() {
    var genresList = emptyList<Genres>()
    var keywordList = emptyList<Keyword>()

    class GenreViewHolder(genreView: View) : RecyclerView.ViewHolder(genreView) {
        val text: Chip = genreView.findViewById(R.id.genre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GenreViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.chip_layout,
            parent,
            false
        )
    )

    override fun getItemCount() = if (isGenre) genresList.size else keywordList.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        if (isGenre)
            holder.text.text = genresList[position].name
        else
            holder.text.text = keywordList[position].name

    }
}