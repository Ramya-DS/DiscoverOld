package com.example.discover.show

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import com.example.discover.dataModel.showDetail.Season
import com.example.discover.utils.LoadPosterImage
import java.lang.ref.WeakReference

class SeasonAdapter(private val activity: WeakReference<Activity>) :
    RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {

    private var seasons = emptyList<Season>()

    inner class SeasonViewHolder(seasonView: View) : RecyclerView.ViewHolder(seasonView),
        View.OnClickListener {
        init {
            seasonView.setOnClickListener(this)
        }

        var number = 0
        val poster: ImageView = seasonView.findViewById(R.id.poster_season)
        val season: TextView = seasonView.findViewById(R.id.season)
        val episode: TextView = seasonView.findViewById(R.id.episode_count)
        val airDate: TextView = seasonView.findViewById(R.id.air_date)
        override fun onClick(v: View?) {
            Log.d("SeasonAdapter", "$number")
            (activity.get() as ShowActivity).onSeasonClicked(number, season.text.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SeasonViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.season_card_layout,
            parent,
            false
        )
    )

    override fun getItemCount() = seasons.size

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        val season = seasons[position]

        Log.d("SeasonAdapter", "BindViewHolder ${season.season_number}")
        holder.number = season.season_number
        holder.season.text = season.name

        val episodeText = "Episodes: ${season.episode_count}"
        holder.episode.text = episodeText

        val airDateText = "AirDate: ${season.air_date}"
        holder.airDate.text = airDateText

        setPlaceHolder(holder.poster)

        if (season.poster_path != null)
            LoadPosterImage(
                WeakReference(holder.poster),
                activity
            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, season.poster_path)
    }

    private fun setPlaceHolder(imageView: ImageView) {
        val drawable = ContextCompat.getDrawable(activity.get()!!, R.drawable.ic_placeholder)
        val bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable!!.setBounds(10, 20, canvas.width - 10, canvas.height - 20)
        drawable.draw(canvas)
        imageView.setImageBitmap(bitmap)
    }

    fun fetchSeasons(season: List<Season>) {
        seasons = season
        notifyDataSetChanged()
    }
}