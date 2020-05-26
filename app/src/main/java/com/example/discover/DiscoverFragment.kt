package com.example.discover


import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.dataModel.genre.Genres
import com.example.discover.discover.DiscoverActivity
import com.example.discover.discover.DiscoverViewModel
import com.example.discover.section.SectionListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import java.lang.ref.WeakReference

class DiscoverFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val queryMap = HashMap<String, String>().apply {
        put("media", "movie")
    }
    private var yearList = createYearList()
    private lateinit var viewModel: DiscoverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_discover, container, false)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        ).get(DiscoverViewModel::class.java)

//        viewModel.getMoviesGenres()

        //media selection
        val movieButton = rootView.findViewById<RadioButton>(R.id.movies)
        val tvButton: RadioButton = rootView.findViewById(R.id.shows)

        //chipGroup for genre
//        val chipGroup: ChipGroup = rootView.findViewById(R.id.genre_group)

        movieButton.setOnClickListener {
            queryMap["media"] = "movie"
//            chipGroup.removeAllViews()
//            viewModel.getMoviesGenres()
        }

        tvButton.setOnClickListener {
            queryMap["media"] = "tv"
//            chipGroup.removeAllViews()
//            viewModel.getShowsGenres()
        }

//        viewModel.genres.observe(this, Observer {
//            Log.d("genres", it.toString())
//            generateGenreChips(chipGroup, it)
//        })

        //info button
        val releaseInfo: ImageButton = rootView.findViewById(R.id.releaseYearLimit)
        val voteAverageInfo: ImageButton = rootView.findViewById(R.id.voteAverageInfo)
        val runtimeInfo: ImageButton = rootView.findViewById(R.id.runtimeInfo)
        val yearInfo: ImageButton = rootView.findViewById(R.id.yearInfo)

        releaseInfo.setOnClickListener {
            setToolTip(it, "Release Info", Gravity.END)
        }

        voteAverageInfo.setOnClickListener {
            setToolTip(it, "Vote Average Info", Gravity.START)
        }

        runtimeInfo.setOnClickListener {
            setToolTip(it, "Run time info", Gravity.START)
        }

        yearInfo.setOnClickListener {
            setToolTip(it, "Year Info", Gravity.START)
        }

        //spinner
        val fromSpinner = rootView.findViewById<Spinner>(R.id.from)
        val toSpinner = rootView.findViewById<Spinner>(R.id.to)

        yearList = createYearList()
        val dataAdapter =
            ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_dropdown_item, yearList)
        fromSpinner.adapter = dataAdapter
        toSpinner.adapter = dataAdapter

        fromSpinner.onItemSelectedListener = this
        toSpinner.onItemSelectedListener = this

        //edit text
        val voteAverageText: EditText = rootView.findViewById(R.id.voteAverage)
        val runtimeText: EditText = rootView.findViewById(R.id.runtime)
        val yearText: EditText = rootView.findViewById(R.id.year)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.mediaList)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 3)


        //filter button
        val filter: MaterialButton = rootView.findViewById(R.id.doFilter)
        filter.setOnClickListener {
            if (yearText.text.toString().trim().isNotEmpty())
                queryMap["year"] = yearText.text.toString().trim()
            if (voteAverageText.text.trim().isNotEmpty())
                queryMap["vote_average.gte"] = voteAverageText.text.toString()
            if (runtimeText.text.trim().isNotEmpty())
                queryMap["with_runtime.gte"] = runtimeText.text.toString()
            Log.d("values", queryMap.toString())

            val isMovie = queryMap["media"] == "movie"
            val adapter = SectionListAdapter(isMovie, WeakReference(activity!!), false)
            recyclerView.adapter = adapter
            infiniteRecyclerView(recyclerView, adapter)
            fillMedia(adapter, isMovie)
            (activity!! as DiscoverActivity).toggleFilters()
        }




        return rootView
    }

    private fun createYearList(): List<String> {
        val years = ArrayList<String>()
        years.add("Year")
        for (i in 1900..2020)
            years.add(i.toString())

        return years
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("spinner", "${parent!!.id}, ${view!!.id}, $id")
        Log.d("from", "${R.id.from == parent.id}")
        Log.d("to", "${R.id.to == parent.id}")
        if (position == 0)
            return

        if (parent.id == R.id.from) {
            queryMap["release_date.gte"] = yearList[position].toString()
        } else if (parent.id == R.id.to) {
            queryMap["release_date.lte"] = yearList[position].toString()
        }
    }

    private fun setToolTip(view: View, text: String, gravity: Int) {
        SimpleTooltip.Builder(context)
            .anchorView(view)
            .text(text)
            .gravity(gravity)
            .animated(true)
            .transparentOverlay(false)
            .build()
            .show()
    }

    private fun generateGenreChips(chipGroup: ChipGroup, genres: List<Genres>) {
        Log.d("main", genres.toString())

        for (i in genres) {
            val chip = layoutInflater.inflate(R.layout.chip_layout, chipGroup, false) as Chip
            chip.text = (i.name)
            chipGroup.addView(chip)
        }

    }

    private fun fillMedia(adapter: SectionListAdapter, isMovie: Boolean) {
        queryMap["page"] = "1"
        if (isMovie)
            viewModel.discoverMovies(queryMap, adapter)
        else
            viewModel.discoverTv(queryMap, adapter)
    }

    private fun infiniteRecyclerView(recyclerView: RecyclerView, adapter: SectionListAdapter) {
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
                        queryMap["page"] = (queryMap["page"]!!.toInt() + 1).toString()
                        if (queryMap["media"] == "movie") {
                            viewModel.discoverMovies(queryMap, adapter)
                        }

                        page++
                    }
                }

            }
        })
    }

}