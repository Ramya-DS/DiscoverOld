package com.example.discover.show

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.discover.R
import com.example.discover.dataModel.credits.Cast
import com.example.discover.dataModel.credits.Credit
import com.example.discover.dataModel.credits.Crew
import com.example.discover.dataModel.images.ImageDetails
import com.example.discover.dataModel.showDetail.Episode
import com.example.discover.dataModel.showDetail.Season
import com.example.discover.movie.CreditAdapter
import com.example.discover.movie.ImageAdapter
import com.example.discover.movie.InfoClass
import com.example.discover.movie.InfoDialogFragment
import com.example.discover.utils.ExpandableTextView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.ref.WeakReference

class SeasonActivity : AppCompatActivity(), OnCreditSelectedListener {

    var showId: Int = 0
    lateinit var showName: String

    var seasonNumber: Int = 0
    lateinit var seasonName: String

    private lateinit var poster: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var title: TextView
    private lateinit var overview: ExpandableTextView

    private lateinit var viewModel: SeasonViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_season)

        fetchIntentData()

        bindActivity()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movie_detail_menu, menu)
        return true
    }

    private fun bindActivity() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(SeasonViewModel::class.java)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_season)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        poster = findViewById(R.id.poster_season)
        title = findViewById(R.id.title_season)
        overview = findViewById(R.id.overview_season)

        tabLayout = findViewById(R.id.tab_layout_season)

        val text = "$showName $seasonName"
        title.text = text

        viewModel.seasonDetails(showId, seasonNumber, this)
        viewModel.fetchCreditDetails(showId, seasonNumber, this)
    }

    private fun fetchIntentData() {
        intent?.apply {
            showId = getIntExtra("show id", 0)
            seasonNumber = getIntExtra("season number", 0)
            showName = getStringExtra("show name")!!
            seasonName = getStringExtra("season name")!!
        }
        Log.d("SeasonActivity", "intent $showId $seasonNumber $showName $seasonName")
    }

    fun setSeasonDetails(season: Season) {
        Log.d("SeasonActivity", "Season $season")

        print(season.toString())

        poster.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        if (season.images?.posters != null) {
            poster.adapter = ImageAdapter(false, season.images.posters, WeakReference(this))
        } else if (season.poster_path != null) {
            poster.adapter = ImageAdapter(
                false, listOf(ImageDetails(0.0, season.poster_path, 0, 0)),
                WeakReference(this)
            )
        }
        TabLayoutMediator(tabLayout, poster) { tab, _ ->
            poster.setCurrentItem(tab.position, true)
        }.attach()

        overview.text = season.overview

        setEpisode(season.episodes)
    }

    fun setCreditDetails(credit: Credit) {
        Log.d("credit", credit.toString())

        val cast: RecyclerView = findViewById(R.id.cast_season)
        val crew: RecyclerView = findViewById(R.id.crew_season)

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

    private fun setEpisode(episodes: List<Episode>) {
        Log.d("episode", episodes.toString())

        val episodeList: RecyclerView = findViewById(R.id.episode_season)
        episodeList.layoutManager = LinearLayoutManager(this)
        episodeList.setHasFixedSize(false)
        episodeList.adapter = EpisodeAdapter(
            episodes,
            WeakReference(this), this
        )
    }

    override fun onCrewSelected(crew: List<Crew>) {
        Log.d("crew selected",crew.toString())
        InfoDialogFragment.newCrewInstance(crew).show(supportFragmentManager, "Crew")
    }

    override fun onCastSelected(cast: List<Cast>) {
        Log.d("cast selected",cast.toString())
        InfoDialogFragment.newCastInstance(cast).show(supportFragmentManager, "Cast")
    }

}
