package com.example.discover.movie

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.discover.R
import com.example.discover.dataModel.credits.Credit
import com.example.discover.dataModel.externalId.ExternalID
import com.example.discover.dataModel.genre.Genres
import com.example.discover.dataModel.images.ImageDetails
import com.example.discover.dataModel.keywords.Keyword
import com.example.discover.dataModel.movieDetail.MovieDetails
import com.example.discover.dataModel.moviePreview.MoviePreview
import com.example.discover.dataModel.reviews.Review
import com.example.discover.section.SectionListAdapter
import com.example.discover.utils.LoadPosterImage
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.ref.WeakReference
import kotlin.math.abs


class MovieActivity : AppCompatActivity() {

    private var id = 0

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var movieName: String
    private lateinit var backdropImage: ViewPager2
    private lateinit var posterImage: ImageView
    private lateinit var rating: TextView
    private lateinit var title: TextView
    private lateinit var tagLine: TextView
    private lateinit var runtime: TextView
    private lateinit var overview: TextView
    private lateinit var status: TextView
    private lateinit var infoButton: ImageButton


    private lateinit var cast: RecyclerView
    private lateinit var crew: RecyclerView


    private var infoDialog: InfoDialogFragment? = null
    private var linkDialog: InfoDialogFragment? = null
    lateinit var viewModel: MovieViewModel

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trial_movie)

        fetchIntentData()
        bindActivity()

        appBarLayout = findViewById(R.id.appbar_movie_detail)
        collapsingToolbarLayout = findViewById(R.id.collapsing_movie_detail)
        toolbar = findViewById(R.id.toolbar_movie)
        toolbar.setNavigationOnClickListener {
            this.finish()
        }
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.movie_detail_menu, menu)
        collapsingToolbarChanges()
        return true
    }

    private fun collapsingToolbarChanges() {
        val icon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val infoItem = menu?.findItem(R.id.info_menu)
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                collapsingToolbarLayout.title = movieName
                infoItem?.isVisible = true
            } else if (verticalOffset == 0) {
                collapsingToolbarLayout.title = " "
                infoItem?.isVisible = false
            }
        })
    }

    private fun fetchIntentData() {
        id = intent.getIntExtra("id", 0)
        movieName = intent.getStringExtra("name")!!
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.info_menu) {
            displayInfoDialog(infoDialog!!)
            return true
        } else if (item.itemId == R.id.externalLink) {
            displayInfoDialog(linkDialog!!)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun bindActivity() {
        backdropImage = findViewById(R.id.backdrop_movie)
        tabLayout = findViewById(R.id.tab_layout)
        posterImage = findViewById(R.id.poster_movie)
        rating = findViewById(R.id.rating_movie)
        title = findViewById(R.id.title_movie)
        tagLine = findViewById(R.id.tagline_movie)
        runtime = findViewById(R.id.runtime_movie)
        overview = findViewById(R.id.overview)
        cast = findViewById(R.id.cast)
        crew = findViewById(R.id.crew)
        status = findViewById(R.id.status_movie)
        infoButton = findViewById(R.id.moreInfo)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MovieViewModel::class.java)

        viewModel.movieDetails(id, this)
        viewModel.fetchCredits(id, this)
        viewModel.getKeywords(id, this)
        viewModel.fetchRecommendations(id, this)
        viewModel.fetchSimilarMovies(id, this)
        viewModel.fetchReviews(id, this)
        viewModel.fetchExternalIds(id, this)
    }


    fun setMovieDetails(movieDetails: MovieDetails) {
        Log.d("movie", "$movieDetails")
        infoDialog = InfoDialogFragment.newInstance(createInfo(movieDetails))
        backdropImage.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        if (movieDetails.images?.backdrops != null) {
            backdropImage.adapter = ImageAdapter(movieDetails.images.backdrops, WeakReference(this))
        } else
            backdropImage.adapter = ImageAdapter(
                listOf(ImageDetails(0.0, movieDetails.backdrop_path, 0, 0)),
                WeakReference(this)
            )
        TabLayoutMediator(tabLayout, backdropImage) { tab, position ->
            backdropImage.setCurrentItem(tab.position, true)
        }.attach()

        LoadPosterImage(
            WeakReference(posterImage),
            WeakReference(this)
        ).execute(movieDetails.poster_path)

        title.text = movieDetails.title
        rating.text = movieDetails.vote_average.toString()
        tagLine.text = movieDetails.tagline
        val text = "${movieDetails.runtime} minutes"
        runtime.text = text
        overview.text = movieDetails.overview
        status.text = movieDetails.status

        setGenres(movieDetails.genres)
        infoButton.setOnClickListener {
            displayInfoDialog(infoDialog!!)
        }
    }

    fun setCredits(credit: Credit) {
        Log.d("credit", credit.toString())

        cast.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        crew.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)

        cast.setHasFixedSize(true)
        crew.setHasFixedSize(true)

        cast.adapter = CreditAdapter(false, WeakReference(this)).apply {
            castList = credit.cast
        }

        crew.adapter = CreditAdapter(true, WeakReference(this)).apply {
            crewList = credit.crew
        }

    }

    private fun setGenres(genres: List<Genres>) {

        val genresList: RecyclerView = findViewById(R.id.genresList)

        genresList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        genresList.setHasFixedSize(true)
        genresList.adapter = ChipAdapter(true).apply {
            this.genresList = genres
        }

    }

    fun setKeywords(keywords: List<Keyword>) {
        val keywordList: RecyclerView = findViewById(R.id.keywordsList)

        keywordList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        keywordList.setHasFixedSize(true)
        keywordList.adapter = ChipAdapter(false).apply {
            this.keywordList = keywords
        }
    }

    fun setRecommendations(movies: List<MoviePreview>) {
        val recommendations: RecyclerView = findViewById(R.id.recommendations)
        recommendations.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        recommendations.setHasFixedSize(true)
        recommendations.adapter = SectionListAdapter(true, WeakReference(this), false).apply {
            setMovieSectionList(movies)
        }
    }

    fun setSimilarMovies(movies: List<MoviePreview>) {
        val similarMoviesList: RecyclerView = findViewById(R.id.similarMovies)
        similarMoviesList.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        similarMoviesList.setHasFixedSize(true)
        similarMoviesList.adapter = SectionListAdapter(true, WeakReference(this), false).apply {
            setMovieSectionList(movies)
        }
    }

    fun setReviews(reviews: List<Review>) {
        val reviewsList: RecyclerView = findViewById(R.id.reviews)
        reviewsList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        reviewsList.setHasFixedSize(true)
        reviewsList.adapter = ReviewAdapter(reviews)
    }

    private fun createInfo(movieDetails: MovieDetails): List<InfoClass> {
        val list = mutableListOf<InfoClass>()
        list.add(InfoClass(R.drawable.ic_revenue, movieDetails.revenue.toString(), "Revenue"))
        list.add(InfoClass(R.drawable.ic_homepage, movieDetails.homepage, "Homepage"))
        list.add(
            InfoClass(
                R.drawable.ic_language,
                movieDetails.original_language,
                "Original Language"
            )
        )
        list.add(InfoClass(R.drawable.ic_release_date, movieDetails.release_date, "Release Date"))
        list.add(InfoClass(R.drawable.ic_budget, movieDetails.budget.toString(), "Budget"))

        return list
    }

    private fun createLinkInfo(externalID: ExternalID): List<InfoClass> {
        val list = mutableListOf<InfoClass>()

        list.add(InfoClass(R.drawable.ic_imdb, externalID.imdbId, "IMDB ID"))
        list.add(InfoClass(R.drawable.ic_instagram, externalID.instagramId, "Instagram ID"))
        list.add(InfoClass(R.drawable.ic_facebook, externalID.facebookId, "Facebook ID"))
        list.add(InfoClass(R.drawable.ic_twitter, externalID.twitterId, "Twitter ID"))

        return list
    }

    private fun displayInfoDialog(fragment: InfoDialogFragment) {
        fragment.show(supportFragmentManager, "INFO")
    }

    fun setExternalIds(externalID: ExternalID) {
        linkDialog = InfoDialogFragment.newInstance(createLinkInfo(externalID))
    }

}
