package com.example.discover.show

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.discover.R
import com.example.discover.dataModel.ShowPreview.ShowPreview
import com.example.discover.dataModel.credits.Credit
import com.example.discover.dataModel.externalId.ExternalID
import com.example.discover.dataModel.genre.Genres
import com.example.discover.dataModel.images.ImageDetails
import com.example.discover.dataModel.keywords.Keyword
import com.example.discover.dataModel.reviews.Review
import com.example.discover.dataModel.showDetail.Season
import com.example.discover.dataModel.showDetail.ShowDetails
import com.example.discover.movie.*
import com.example.discover.review.ReviewActivity
import com.example.discover.section.SectionListAdapter
import com.example.discover.utils.LoadPosterImage
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.ref.WeakReference
import kotlin.math.abs


class ShowActivity : AppCompatActivity(), OnReviewClickListener, OnUrlSelectedListener {

    var id: Int = 0
    lateinit var showName: String

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var backdrop: ViewPager2
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var rating: TextView
    private lateinit var infoButton: ImageButton
    private lateinit var runtime: TextView
    private lateinit var details: TextView
    private lateinit var status: TextView
    private lateinit var overview: TextView

    private lateinit var createdBy: RecyclerView


    private var menu: Menu? = null
    private var linkDialog: InfoDialogFragment? = null
    private var infoDialog: InfoDialogFragment? = null

    private lateinit var viewModel: ShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show)

        fetchIntentData()

        bindActivity()

        appBarLayout = findViewById(R.id.appbar_show_detail)
        collapsingToolbarLayout = findViewById(R.id.collapsing_show_detail)
        toolbar = findViewById(R.id.toolbar_show)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

    }

    private fun fetchIntentData() {
        id = intent.getIntExtra("id", 0)
        showName = intent.getStringExtra("name")!!
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.movie_detail_menu, menu)
        collapsingToolbarChanges()
        return true
    }

    private fun collapsingToolbarChanges() {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val infoItem = menu?.findItem(R.id.info_menu)
            appBarLayout.post {
                if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    collapsingToolbarLayout.title = showName
                    infoItem?.isVisible = true
                } else if (verticalOffset == 0) {
                    collapsingToolbarLayout.title = " "
                    infoItem?.isVisible = false
                }
            }
        })
    }

    private fun bindActivity() {
        tabLayout = findViewById(R.id.tab_layout)
        backdrop = findViewById(R.id.backdrop_show)
        poster = findViewById(R.id.poster_show)
        title = findViewById(R.id.title_show)
        rating = findViewById(R.id.rating_show)
        infoButton = findViewById(R.id.moreInfo_show)
        runtime = findViewById(R.id.runtime_show)
        details = findViewById(R.id.details_show)
        status = findViewById(R.id.status_show)
        overview = findViewById(R.id.overview_show)

        createdBy = findViewById(R.id.createdBy_show)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ShowViewModel::class.java)

        viewModel.showDetails(id, this)
        viewModel.fetchCredits(id, this)
        viewModel.fetchKeywords(id, this)
        viewModel.fetchExternalIds(id, this)
        viewModel.fetchRecommendations(id, this)
        viewModel.fetchSimilarShows(id, this)
        viewModel.fetchReviews(id, this)

    }

    fun setShowDetails(showDetails: ShowDetails) {
        Log.d("show details", showDetails.toString())

        infoDialog = InfoDialogFragment.newInfoInstance(createInfo(showDetails))
        backdrop.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        backdrop.adapter = when {
            showDetails.images?.backdrops != null -> {
                ImageAdapter(true, showDetails.images.backdrops, WeakReference(this))
            }
            showDetails.backdrop_path != null -> ImageAdapter(
                true,
                listOf(ImageDetails(0.0, showDetails.backdrop_path, 0, 0)),
                WeakReference(this)
            )
            else -> ImageAdapter(true, listOf(ImageDetails(0.0, " ", 0, 0)), WeakReference(this))
        }

        TabLayoutMediator(tabLayout, backdrop) { tab, _ ->
            backdrop.setCurrentItem(tab.position, true)
        }.attach()

        LoadPosterImage(
            WeakReference(poster),
            WeakReference(this)
        ).execute(showDetails.poster_path)

        title.text = showDetails.name
        rating.text = showDetails.vote_average.toString()

        val time = " ${showDetails.episode_run_time[0]} minutes"
        runtime.text = time


        infoButton.setOnClickListener {
            displayInfoDialog(infoDialog!!)
        }
        var details = if (showDetails.in_production) "In Production" else "Not in Production"
        details += "| ${showDetails.type}| ${showDetails.number_of_seasons} Seasons"
        this.details.text = details

        status.text = showDetails.status

        overview.text = showDetails.overview

        createdBy.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        createdBy.setHasFixedSize(true)
        createdBy.adapter = CreditAdapter(true, WeakReference(this)).apply {
            crewList = showDetails.created_by
        }

        setGenres(showDetails.genres)
        setSeasons(showDetails.seasons)

    }

    fun setCredits(credit: Credit) {
        Log.d("credit", credit.toString())

        val cast: RecyclerView = findViewById(R.id.cast_show)
        val crew: RecyclerView = findViewById(R.id.crew_show)

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
        Log.d("genres", genres.toString())
        val genresList: RecyclerView = findViewById(R.id.genresList_show)
        genresList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        genresList.setHasFixedSize(true)
        genresList.adapter = ChipAdapter(true).apply {
            this.genresList = genres
        }

    }

    fun setKeywords(keywords: List<Keyword>?) {
        Log.d("keywords", keywords.toString())
        if (keywords != null) {
            val keywordsList: RecyclerView = findViewById(R.id.keywordsList_show)
            keywordsList.layoutManager =
                GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            keywordsList.setHasFixedSize(true)
            keywordsList.adapter = ChipAdapter(false).apply {
                this.keywordList = keywords
            }
        }

    }

    fun setExternalIds(externalId: ExternalID) {
        Log.d("ids", externalId.toString())
        linkDialog = InfoDialogFragment.newInfoInstance(createLinkInfo(externalId))
    }

    private fun createLinkInfo(externalID: ExternalID): List<InfoClass> {
        val list = mutableListOf<InfoClass>()

        externalID.imdbId?.let {
            list.add(
                InfoClass(
                    R.drawable.ic_imdb,
                    "https://www.imdb.com/title/${externalID.imdbId}",
                    "IMDB ID"
                )
            )
        }
        externalID.instagramId?.let {
            list.add(
                InfoClass(
                    R.drawable.ic_instagram,
                    "https://www.instagram.com/$it",
                    "Instagram ID"
                )
            )
        }

        Log.d("null", "https://www.instagram.com/${externalID.instagramId}")
        externalID.facebookId?.let {
            list.add(
                InfoClass(
                    R.drawable.ic_facebook,
                    "https://www.facebook.com/$it",
                    "Facebook ID"
                )
            )
        }
        externalID.twitterId?.let {
            list.add(InfoClass(R.drawable.ic_twitter, "https://twitter.com/$it", "Twitter ID"))
        }

        return list
    }

    private fun createInfo(showDetails: ShowDetails): List<InfoClass> {
        val list = mutableListOf<InfoClass>()

        if (showDetails.homepage.trim().isNotEmpty())
            list.add(InfoClass(R.drawable.ic_homepage, showDetails.homepage, "Homepage"))

        if (showDetails.first_air_date.trim().isNotEmpty())
            list.add(
                InfoClass(
                    R.drawable.ic_release_date,
                    showDetails.first_air_date,
                    "First Air Date"
                )
            )

        list.add(
            InfoClass(
                R.drawable.ic_language,
                showDetails.original_language,
                "Original Language"
            )
        )

        if (showDetails.last_air_date.trim().isNotEmpty())
            list.add(
                InfoClass(
                    R.drawable.ic_release_date,
                    showDetails.last_air_date,
                    "Last Air Date"
                )
            )


        return list
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

    private fun displayInfoDialog(fragment: InfoDialogFragment) {
        fragment.show(supportFragmentManager, "INFO")
    }

    override fun onUrlSelected(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onReviewClicked(review: Review) {
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra("review", review)
        startActivity(intent)
    }

    fun setRecommendations(shows: List<ShowPreview>) {
        Log.d("recomm", shows.toString())
        val recommendations: RecyclerView = findViewById(R.id.recommendations_show)
        recommendations.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        recommendations.setHasFixedSize(true)
        recommendations.adapter = SectionListAdapter(false, WeakReference(this), false).apply {
            setTvSectionList(shows)
        }
    }

    fun setSimilarShows(shows: List<ShowPreview>) {
        Log.d("similar", shows.toString())
        val similarShows: RecyclerView = findViewById(R.id.similarMovies_show)
        similarShows.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        similarShows.setHasFixedSize(true)
        similarShows.adapter = SectionListAdapter(false, WeakReference(this), false).apply {
            setTvSectionList(shows)
        }
    }

    fun setReviews(reviews: List<Review>) {
        Log.d("reviews", reviews.toString())
        val reviewsList: RecyclerView = findViewById(R.id.reviews_show)
        reviewsList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        reviewsList.setHasFixedSize(true)
        reviewsList.adapter = ReviewAdapter(reviews, this)
    }

    private fun setSeasons(season: List<Season>) {
        Log.d("season", season.toString())
        val seasonsList: RecyclerView = findViewById(R.id.season_show)
        seasonsList.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        seasonsList.setHasFixedSize(true)
        seasonsList.adapter = SeasonAdapter(WeakReference(this)).apply {
            fetchSeasons(season)
        }
    }

    fun onSeasonClicked(seasonNumber: Int, seasonName: String) {
        val intent = Intent(this, SeasonActivity::class.java).apply {
            putExtra("show id", id)
            putExtra("season number", seasonNumber)
            putExtra("show name", showName)
            putExtra("season name", seasonName)
        }
        startActivity(intent)
    }
}
