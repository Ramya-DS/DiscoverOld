package com.example.discover.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.discover.R
import com.example.discover.dataModel.collection.Collection
import com.example.discover.dataModel.moviePreview.MoviePreview
import com.example.discover.dataModel.multiSearch.MultiSearch
import com.example.discover.dataModel.ShowPreview.ShowPreview
import com.example.discover.utils.LoadingFragment
import com.example.discover.utils.NoMatchFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchActivity : AppCompatActivity(), DrawerLayout.DrawerListener {

    lateinit var viewModel: SearchViewModel
    private var type = "multi-search"
    private var displayFragment: SearchResultFragment? = null
    lateinit var textView: TextView
    lateinit var filterIcon: FloatingActionButton
    private var mQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModelInitialisation()

        filterIcon = findViewById(R.id.filter_search)

        textView = findViewById(R.id.resultText)
        val navigationIcon: ImageView = findViewById(R.id.back)

        navigationIcon.setOnClickListener {
            finish()
        }
        initializeSearchView()
    }

    private fun viewModelInitialisation() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(SearchViewModel::class.java)
    }

    private fun initializeSearchView() {
        val searchView: SearchView = findViewById(R.id.searchBox)
        searchView.isFocusable = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        initiateSearch(it.trim())
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    mQuery = it
                }
                return false
            }
        })

        sideSheetInitialisation(searchView)

    }

    fun onFilterClick(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            if (checked) {
                when (view.id) {
                    R.id.movieFilter -> {
                        type = "movies"
                    }
                    R.id.showsFilter -> {
                        type = "shows"
                    }
                    R.id.collectionsFilter -> {
                        type = "collections"
                    }

                    R.id.multiFilter -> {
                        type = "multi-search"
                    }

                }
            }
        }
    }

    private fun sideSheetInitialisation(searchView: SearchView) {

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        searchView.clearFocus()
        val filterButton: MaterialButton = findViewById(R.id.filterButton)

        filterButton.setOnClickListener {
            viewModel.type = type
            if (mQuery != "") {
                initiateSearch(mQuery)
                Toast.makeText(this, "send", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.END)

        }

        filterIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        drawerLayout.setDrawerListener(this)
    }

    private fun displayResultFragment() {
        displayFragment = SearchResultFragment.newInstance(viewModel.type)

        supportFragmentManager.beginTransaction()
            .replace(R.id.search_container, displayFragment!!, "RESULT").commit()

        supportFragmentManager.executePendingTransactions()
    }

    private fun displayLoadingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.search_container, LoadingFragment(), "LOADING").commit()
    }

    private fun removeLoadingFragment() {
        supportFragmentManager.findFragmentByTag("LOADING")?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    fun displayNoResultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.search_container, NoMatchFragment(), "NO MATCH")
            .commit()
    }

    private fun removeNoResultFragment() {
        supportFragmentManager.findFragmentByTag("NO MATCH")?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    private fun removeResultFragment() {
        supportFragmentManager.findFragmentByTag("RESULT")?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
        displayFragment = null
    }

    fun firstPageOfMovies(results: List<MoviePreview>) {
        removeNoResultFragment()
        removeLoadingFragment()
        removeResultFragment()
        displayResultFragment()

        displayFragment!!.adapter.setMovies(results)

    }

    fun firstPageOfShows(results: List<ShowPreview>) {
        removeNoResultFragment()
        removeLoadingFragment()
        removeResultFragment()
        displayResultFragment()

        Log.d("shows page 1", results.toString())

        displayFragment!!.adapter.setShows(results)

    }

    fun firstPageOfCollections(results: List<Collection>) {
        removeNoResultFragment()
        removeLoadingFragment()
        removeResultFragment()
        displayResultFragment()

        Log.d("collections page 1", results.toString())

        displayFragment!!.adapter.setCollections(results)

    }

    fun firstPageOfMultiSearch(results: List<MultiSearch>) {
        removeNoResultFragment()
        removeLoadingFragment()
        removeResultFragment()
        displayResultFragment()

        Log.d("multi search page 1", results.toString())

        displayFragment!!.adapter.setMix(results)

    }

    fun displayRestMovies(results: List<MoviePreview>) {
        Log.d("movies page rest", results.toString())
        displayFragment?.adapter?.appendMovies(results)
    }

    fun displayRestShows(results: List<ShowPreview>) {
        Log.d("shows page rest", results.toString())
        displayFragment?.adapter?.appendShows(results)
    }

    fun displayRestCollections(results: List<Collection>) {
        Log.d("collections page rest", results.toString())
        displayFragment?.adapter?.appendCollections(results)
    }

    fun displayRestMultiSearch(results: List<MultiSearch>) {
        Log.d("multi search page rest", results.toString())
        displayFragment?.adapter?.appendMix(results)
    }

    private fun initiateSearch(query: String) {
        displayLoadingFragment()
        viewModel.queryMap["query"] = query
        viewModel.queryMap["page"]="1"
        Log.d("submit", "${viewModel.queryMap["query"]} ${viewModel.type}")
        typeCall()
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerOpened(drawerView: View) {
        when (viewModel.type) {
            "multi-search" -> drawerView.findViewById<RadioButton>(R.id.multiFilter).isChecked =
                true
            "movies" -> drawerView.findViewById<RadioButton>(R.id.movieFilter).isChecked = true
            "shows" -> drawerView.findViewById<RadioButton>(R.id.showsFilter).isChecked = true
            "collections" -> drawerView.findViewById<RadioButton>(R.id.collectionsFilter)
                .isChecked = true
        }
    }

    fun fetchMore() {
        viewModel.queryMap["page"] = (viewModel.queryMap["page"]!!.toInt() + 1).toString()
        typeCall()
    }

    private fun typeCall() {
        when (viewModel.type) {
            "multi-search" -> viewModel.getMultiSearchResult(this)
            "movies" -> viewModel.getSearchedMovies(this)
            "shows" -> viewModel.getSearchedShows(this)
            "collections" -> viewModel.getSearchedCollections(this)
        }
    }
}
