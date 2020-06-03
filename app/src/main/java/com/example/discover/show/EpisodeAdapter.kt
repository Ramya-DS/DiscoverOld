package com.example.discover.show

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import com.example.discover.dataModel.showDetail.Episode
import com.example.discover.utils.LoadBackdropImage
import java.lang.ref.WeakReference

class EpisodeAdapter(
    private val episodes: List<Episode>,
    private val activity: WeakReference<Activity>,
    private val onCreditSelectedListener: OnCreditSelectedListener
) :
    RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    inner class EpisodeViewHolder(episodeView: View) : RecyclerView.ViewHolder(episodeView) {
        var mPosition = 0
        val stillPath: ImageView = episodeView.findViewById(R.id.still_path)
        val rating: TextView = episodeView.findViewById(R.id.ratings_episode)
        val airDate: TextView = episodeView.findViewById(R.id.air_date_episode)
        val name: TextView = episodeView.findViewById(R.id.name_episode)
        val overview: TextView = episodeView.findViewById(R.id.overview_episode)

        init {
            highlightText(episodeView.findViewById(R.id.crewList_season), mPosition)
            highlightText(episodeView.findViewById(R.id.guestStarsList_season), mPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.episode_layout, parent, false
            )
        )

    override fun getItemCount() = episodes.size
    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = episodes[position]

        holder.mPosition = position

        setPlaceHolder(holder.stillPath)

        if (episode.still_path != null) {
            Toast.makeText(holder.itemView.context, "${episode.still_path}", Toast.LENGTH_SHORT)
                .show()
            LoadBackdropImage(
                WeakReference(holder.stillPath),
                activity
            ).executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                episode.still_path,
                getWidth().toString(),
                (getWidth()!! / 1.77777).toInt().toString()
            )
        }

        holder.name.text = episode.name
        holder.overview.text = episode.overview
        holder.rating.text = " ${episode.vote_average}"
        holder.airDate.text = episode.air_date
    }

    private fun setPlaceHolder(imageView: ImageView) {
        val drawable = ContextCompat.getDrawable(activity.get()!!, R.drawable.ic_placeholder)
        val bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable!!.setBounds(10, 20, canvas.width - 10, canvas.height - 20)
        drawable.draw(canvas)
        imageView.setImageBitmap(bitmap)
    }

    private fun getWidth(): Int? {
        val displayMetrics: DisplayMetrics? = activity.get()?.resources?.displayMetrics
        Log.d("density", "${displayMetrics?.density}")
        return displayMetrics?.widthPixels
    }

    private fun highlightText(textView: TextView, position: Int) {
        val text = textView.text
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if (text.contains("crew", true)) {
                    Log.d("position", "$position")
                    onCreditSelectedListener.onCrewSelected(episodes[position].crew)
                } else {
                    Log.d("position", "$position")
                    onCreditSelectedListener.onCastSelected(episodes[position].guest_stars)
                }
            }
        }

        spannableString.setSpan(
            clickableSpan,
            0,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}