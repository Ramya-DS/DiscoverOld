package com.example.discover.section

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import java.lang.ref.WeakReference

class SectionAdapter(
    private val isMovie: Boolean,
    private val viewModel: SectionListViewModel,
    private val activity: WeakReference<Activity>
) :
    RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    companion object {
        val MOVIE_SECTIONS = listOf(
            "Trending Today",
            "Now Playing in Theaters",
            "Popular",
            "Coming Soon",
            "Top-rated"
        )
        val TV_SECTIONS = listOf(
            "Trending Today",
            "On Air",
            "Popular",
            "Airing Today",
            "Top-rated"
        )
    }

    inner class SectionViewHolder(sectionView: View) : RecyclerView.ViewHolder(sectionView) {
        val sectionTitle: TextView = sectionView.findViewById(R.id.sectionTitle)
        val sectionList: RecyclerView = sectionView.findViewById(R.id.sectionList)


        init {
            sectionList.layoutManager =
                GridLayoutManager(sectionView.context, 1, GridLayoutManager.HORIZONTAL, false)
            sectionList.setHasFixedSize(true)
            sectionList.adapter = SectionListAdapter(isMovie, activity, true)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SectionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.section,
                parent,
                false
            )
        )

    override fun getItemCount() = if (isMovie) MOVIE_SECTIONS.size else TV_SECTIONS.size

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = if (isMovie) MOVIE_SECTIONS[position] else TV_SECTIONS[position]
        holder.sectionTitle.text = section
        bindRecyclerView(holder.sectionList.adapter as SectionListAdapter, position, section)
    }

    private fun bindRecyclerView(adapter: SectionListAdapter, position: Int, section: String) {
        adapter.section = section

        if (isMovie) {
            viewModel.apply {
                when (position) {
                    0 -> setTrendingMovies(adapter = adapter)
                    1 -> setNowPlayingMovies(adapter = adapter)
                    2 -> setPopularMovies(adapter = adapter)
                    3 -> setUpcomingMovies(adapter = adapter)
                    4 -> setTopRatedMovies(adapter = adapter)
                }
            }
        } else {
            viewModel.apply {
                when (position) {
                    0 -> setTrendingShows(adapter = adapter)
                    1 -> setOnAirShows(adapter = adapter)
                    2 -> setPopularShows(adapter = adapter)
                    3 -> setAiringTodayShows(adapter = adapter)
                    4 -> setTopRatedShows(adapter = adapter)
                }
            }
        }
    }
}