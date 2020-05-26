package com.example.discover.mediaList

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import com.example.discover.section.SectionAdapter
import com.example.discover.section.SectionListAdapter
import com.example.discover.dataModel.moviePreview.MoviePreview
import com.example.discover.dataModel.tvPreview.TvPreview
import com.example.discover.section.SectionListViewModel
import java.lang.ref.WeakReference
import kotlin.math.roundToInt


class MediaListActivity : AppCompatActivity() {

    private lateinit var moviesList: List<MoviePreview>
    private lateinit var tvList: List<TvPreview>
    var isMovie = true
    lateinit var section: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        intentData()

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(SectionListViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.movieList)
        val spanCount: Int

        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        Log.d("dimensions", dpWidth.toString())

        spanCount = (dpWidth / 120).roundToInt()

        recyclerView.layoutManager =
            GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false)
        val adapter = SectionListAdapter(isMovie, WeakReference(this),false)

        if (isMovie)
            adapter.setMovieSectionList(moviesList)
        else
            adapter.setTvSectionList(tvList)

        recyclerView.adapter = adapter

        var page = 2
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleThreshold = 2
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    val currentTotalCount = layoutManager.itemCount
                    if (currentTotalCount <= lastItem + visibleThreshold) {
                        if (isMovie) {
                            when (section) {
                                SectionAdapter.MOVIE_SECTIONS[0] -> viewModel.setTrendingMovies(
                                    page,
                                    adapter
                                )
                                SectionAdapter.MOVIE_SECTIONS[1] -> viewModel.setNowPlayingMovies(
                                    page,
                                    adapter
                                )
                                SectionAdapter.MOVIE_SECTIONS[2] -> viewModel.setPopularMovies(
                                    page,
                                    adapter
                                )
                                SectionAdapter.MOVIE_SECTIONS[3] -> viewModel.setUpcomingMovies(
                                    page,
                                    adapter
                                )
                                SectionAdapter.MOVIE_SECTIONS[4] -> viewModel.setTopRatedMovies(
                                    page,
                                    adapter
                                )
                            }
                        } else {
                            when (section) {
                                SectionAdapter.TV_SECTIONS[0] -> viewModel.setTrendingShows(
                                    page,
                                    adapter
                                )
                                SectionAdapter.TV_SECTIONS[1] -> viewModel.setOnAirShows(
                                    page,
                                    adapter
                                )
                                SectionAdapter.TV_SECTIONS[2] -> viewModel.setPopularShows(
                                    page,
                                    adapter
                                )
                                SectionAdapter.TV_SECTIONS[3] -> viewModel.setAiringTodayShows(
                                    page,
                                    adapter
                                )
                                SectionAdapter.TV_SECTIONS[4] -> viewModel.setTopRatedShows(
                                    page,
                                    adapter
                                )
                            }
                        }
                        page++
                    }
                }

            }
        })
    }

    private fun intentData() {
        isMovie = intent.getBooleanExtra("isMovie", true)
        Log.d("next", isMovie.toString())
        if (isMovie) {
            val list = intent.getSerializableExtra("list") as List<MoviePreview>
            moviesList = list
        } else {
            val list = intent.getSerializableExtra("list") as List<TvPreview>
            tvList = list
            Log.d("tv", tvList.size.toString())
        }

        section = intent.getStringExtra("section")!!

    }
}
