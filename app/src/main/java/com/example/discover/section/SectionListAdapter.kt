package com.example.discover.section

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.DiscoverApplication
import com.example.discover.R
import com.example.discover.dataModel.moviePreview.MoviePreview
import com.example.discover.dataModel.tvPreview.TvPreview
import com.example.discover.mediaList.MediaListActivity
import com.example.discover.utils.LoadPosterImage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Serializable
import java.lang.ref.WeakReference

class SectionListAdapter(
    private val isMovie: Boolean,
    private val activity: WeakReference<Activity>, private val withTitle: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val movieList = mutableListOf<MoviePreview>()
    private val tvList = mutableListOf<TvPreview>()

    lateinit var section: String

    inner class MediaWithTitleViewHolder(movieView: View) : RecyclerView.ViewHolder(movieView),
        View.OnClickListener {
        val id: Int = 0
        val posterImage: ImageView = movieView.findViewById(R.id.poster)
        val title: TextView = movieView.findViewById(R.id.title)
        val voting: TextView = movieView.findViewById(R.id.voting)
        var imageTask: LoadPosterImage? = null

        init {
            movieView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Toast.makeText(activity.get()?.applicationContext, title.text, Toast.LENGTH_LONG).show()
        }
    }

    inner class MediaWithoutTitleViewHolder(movieView: View) : RecyclerView.ViewHolder(movieView),
        View.OnClickListener {
        val id: Int = 0
        val posterImage: ImageView = movieView.findViewById(R.id.poster)
        val voting: TextView = movieView.findViewById(R.id.voting)
        var imageTask: LoadPosterImage? = null

        init {
            movieView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Toast.makeText(v?.context, id.toString(), Toast.LENGTH_LONG).show()
        }
    }

    inner class LoadMoreViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val moreButton: FloatingActionButton = view.findViewById(R.id.more)

        init {
            moreButton.setOnClickListener(this)
            moreButton.hide()
        }

        override fun onClick(v: View?) {
            Toast.makeText(activity.get()?.applicationContext, "More", Toast.LENGTH_LONG).show()
            val intent = Intent(activity.get()!!, MediaListActivity::class.java)
            if (isMovie) {
                val arrayList = arrayListOf<MoviePreview>()
                arrayList.addAll(movieList)
                intent.putExtra("list", arrayList as Serializable)
            } else {
                val arrayList = arrayListOf<TvPreview>()
                arrayList.addAll(tvList)
                intent.putExtra("list", arrayList as Serializable)
            }
            Log.d("intent", isMovie.toString())
            intent.putExtra("isMovie", isMovie)
            intent.putExtra("section", section)
            activity.get()!!.startActivity(intent)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> MediaWithTitleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.movie_card_with_title,
                    parent,
                    false
                )
            )
            1 -> LoadMoreViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.load_more,
                    parent,
                    false
                )
            )
            else -> MediaWithoutTitleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.movie_card,
                    parent,
                    false
                )
            )
        }
    }


    override fun getItemCount() = if (withTitle) {
        if (isMovie) movieList.size + 1 else tvList.size + 1
    } else {
        if (isMovie) movieList.size else tvList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            (holder as LoadMoreViewHolder).moreButton.show()

        } else {
            if (getItemViewType(position) == 0) {
                holder as MediaWithTitleViewHolder

                if (isMovie)
                    onBindMovie(holder, position)
                else
                    onBindShow(holder, position)
            } else {
                holder as MediaWithoutTitleViewHolder

                if (isMovie)
                    onBindMovie(holder, position)
                else
                    onBindShow(holder, position)
            }


        }
        setFadeAnimation(holder.itemView)
    }

    fun setMovieSectionList(newList: List<MoviePreview>) {
        movieList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setTvSectionList(newList: List<TvPreview>) {
        tvList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is MediaWithTitleViewHolder)
            holder.imageTask?.cancel(true)
    }

    private fun fetchCacheImage(key: String): Bitmap? {
        val memoryCache = (activity.get()?.application as DiscoverApplication).memoryCache
        Log.d("key", "$key error")
        return memoryCache[key]
    }

    private fun fetchFromNetwork(url: String, holder: MediaWithTitleViewHolder) {
        holder.imageTask?.cancel(true)
        holder.imageTask = LoadPosterImage(WeakReference(holder.posterImage), activity)
        holder.imageTask!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
    }

    private fun fetchFromNetwork(url: String, holder: MediaWithoutTitleViewHolder) {
        holder.imageTask?.cancel(true)
        holder.imageTask = LoadPosterImage(WeakReference(holder.posterImage), activity)
        holder.imageTask!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
    }

    private fun setPosterImage(holder: MediaWithTitleViewHolder, posterPath: String) {
        val posterCache = fetchCacheImage(posterPath)
        if (posterCache == null) {
            fetchFromNetwork(posterPath, holder)
        } else
            holder.posterImage.setImageBitmap(posterCache)
    }

    private fun setPosterImage(holder: MediaWithoutTitleViewHolder, posterPath: String) {
        val posterCache = fetchCacheImage(posterPath)
        if (posterCache == null) {
            fetchFromNetwork(posterPath, holder)
        } else
            holder.posterImage.setImageBitmap(posterCache)
    }


    private fun onBindMovie(holder: MediaWithTitleViewHolder, position: Int) {
        val movie = movieList[position]

        if (movie.poster_path != null)
            setPosterImage(holder, movie.poster_path)
        else
            setPlaceHolder(holder.posterImage)

        holder.title.text = movie.title
        holder.voting.text = movie.vote_average.toString()
    }

    private fun onBindMovie(holder: MediaWithoutTitleViewHolder, position: Int) {
        val movie = movieList[position]

        if (movie.poster_path != null)
            setPosterImage(holder, movie.poster_path)
        else
            setPlaceHolder(holder.posterImage)

        holder.voting.text = movie.vote_average.toString()
    }

    private fun onBindShow(holder: MediaWithTitleViewHolder, position: Int) {
        val show = tvList[position]
        Log.d("show", show.toString())

        if (show.poster_path != null)
            setPosterImage(holder, show.poster_path)
        else
            setPlaceHolder(holder.posterImage)

        holder.title.text = show.name
        holder.voting.text = show.vote_average.toString()
    }

    private fun onBindShow(holder: MediaWithoutTitleViewHolder, position: Int) {
        val show = tvList[position]
        Log.d("show", show.toString())

        if (show.poster_path != null)
            setPosterImage(holder, show.poster_path)
        else
            setPlaceHolder(holder.posterImage)

        holder.voting.text = show.vote_average.toString()
    }

    private fun setPlaceHolder(imageView: ImageView) {
        val drawable = ContextCompat.getDrawable(activity.get()!!, R.drawable.ic_placeholder)
        val bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable!!.setBounds(10, 20, canvas.width - 10, canvas.height - 20)
        drawable.draw(canvas)
        imageView.setImageBitmap(bitmap)
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 200
        view.startAnimation(anim)
    }

    override fun getItemViewType(position: Int): Int {
        return if (!withTitle) {
            return 2
        } else {
            if (position < movieList.size || position < tvList.size)
                0
            else
                1
        }
    }

    fun appendMovieSectionList(newList: List<MoviePreview>) {
        movieList.addAll(newList)
        notifyDataSetChanged()
    }

    fun appendTvSectionList(newList: List<TvPreview>) {
        tvList.addAll(newList)
        notifyDataSetChanged()
    }

}