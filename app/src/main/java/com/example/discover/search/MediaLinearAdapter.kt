package com.example.discover.search

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.DiscoverApplication
import com.example.discover.R
import com.example.discover.dataModel.collection.Collection
import com.example.discover.dataModel.moviePreview.MoviePreview
import com.example.discover.dataModel.multiSearch.MultiSearch
import com.example.discover.dataModel.tvPreview.TvPreview
import com.example.discover.section.SectionListAdapter
import com.example.discover.utils.LoadPosterImage
import java.lang.ref.WeakReference

class MediaLinearAdapter(private val type: String, private val activity: WeakReference<Activity>) :
    RecyclerView.Adapter<MediaLinearAdapter.MediaLinearViewHolder>() {

    private val movies = mutableListOf<MoviePreview>()
    private val shows = mutableListOf<TvPreview>()
    private val mix = mutableListOf<MultiSearch>()
    private val collections = mutableListOf<Collection>()

    class MediaLinearViewHolder(mediaView: View) : RecyclerView.ViewHolder(mediaView) {
        var imageTask: LoadPosterImage? = null
        val image: ImageView = mediaView.findViewById(R.id.poster)
        val name: TextView = mediaView.findViewById(R.id.title)
        val genre: TextView = mediaView.findViewById(R.id.Genre)
        val director: TextView = mediaView.findViewById(R.id.director)
        val rating: TextView = mediaView.findViewById(R.id.rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MediaLinearViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.movie_card_linear,
            parent,
            false
        )
    )

    override fun getItemCount() = when (type) {
        "multi-search" -> mix.size
        "movies" -> movies.size
        "shows" -> shows.size
        else -> collections.size
    }

    override fun onBindViewHolder(holder: MediaLinearViewHolder, position: Int) {
        when (type) {
            "multi-search" -> {
                val multiSearch = mix[position]
                Log.d("bind", multiSearch.toString())
                bindMix(holder, multiSearch)
            }
            "movies" -> bindMovie(holder, movies[position])
            "shows" -> bindShow(holder, shows[position])
            "collections" -> bindCollection(holder, collections[position])
        }
        setFadeAnimation(holder.itemView)
    }

    override fun onBindViewHolder(
        holder: MediaLinearViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            for (i in bundle.keySet()) {
                when (i) {
                    "mix" -> bindMix(holder, bundle.getParcelable(i)!!)
                    "movie" -> bindMovie(holder, bundle.getParcelable(i)!!)
                    "show" -> bindShow(holder, bundle.getParcelable(i)!!)
                    "collection" -> bindCollection(holder, bundle.getParcelable(i)!!)
                }
            }
            setFadeAnimation(holder.itemView)
        }
    }

    private fun bindMovie(holder: MediaLinearViewHolder, moviePreview: MoviePreview) {
        bindDetails(
            holder,
            moviePreview.poster_path ?: "",
            moviePreview.title,
            moviePreview.genre_ids.toString(), moviePreview.vote_average.toString()
        )
    }

    private fun bindShow(holder: MediaLinearViewHolder, tvPreview: TvPreview) {
        bindDetails(
            holder,
            tvPreview.poster_path ?: "",
            tvPreview.name,
            tvPreview.genre_ids.toString(), tvPreview.vote_average.toString()
        )
    }

    private fun bindCollection(holder: MediaLinearViewHolder, collection: Collection) {
        bindDetails(holder, collection.poster_path, collection.name, "", "")
    }

    private fun bindMix(holder: MediaLinearViewHolder, multiSearch: MultiSearch) {
        when (multiSearch.media_type) {
            "movie" -> bindDetails(
                holder,
                multiSearch.poster_path,
                multiSearch.title,
                multiSearch.genre_ids.toString(), multiSearch.vote_average.toString()
            )
            "tv" -> bindDetails(
                holder,
                multiSearch.poster_path,
                multiSearch.name,
                multiSearch.genre_ids.toString(), multiSearch.vote_average.toString()
            )
//            "people" -> {
//                var string: String = ""
//                for (i in multiSearch.known_for) {
//                    string += "${i.original_name}, "
//                }
//                bindDetails(
//                    holder,
//                    multiSearch.profile_path,
//                    multiSearch.name,
//                    string, multiSearch.vote_average.toString()
//                )
//            }
        }

    }

    private fun bindDetails(
        holder: MediaLinearViewHolder,
        path: String?,
        name: String,
        genre: String, rating: String
    ) {

        holder.name.text = name
        holder.rating.text = rating
        holder.genre.text = genre
        setPlaceHolder(holder.image)
        if (path != null)
            setPosterImage(holder, path)

    }

    fun setMovies(results: List<MoviePreview>) {
        val result = DiffUtil.calculateDiff(
            MovieListDiffUtilCallback(
                movies,
                results
            )
        )
        result.dispatchUpdatesTo(this)
        movies.clear()
        movies.addAll(results)
//        notifyDataSetChanged()
    }

    fun setShows(results: List<TvPreview>) {
        val result = DiffUtil.calculateDiff(
            ShowListDiffUtilCallback(
                shows,
                results
            )
        )
        result.dispatchUpdatesTo(this)
        shows.clear()
        shows.addAll(results)
//        notifyDataSetChanged()
    }

    fun setCollections(results: List<Collection>) {
        val result = DiffUtil.calculateDiff(
            CollectionListDiffUtilCallback(
                collections,
                results
            )
        )
        result.dispatchUpdatesTo(this)
        collections.clear()
        collections.addAll(results)
//        notifyDataSetChanged()
    }

    fun setMix(results: List<MultiSearch>) {
        val result = DiffUtil.calculateDiff(
            MixListDiffUtilCallback(
                mix,
                results
            )
        )
        result.dispatchUpdatesTo(this)
        Log.d("adapter", results.toString())
        mix.clear()
        mix.addAll(results)
    }

    fun appendMovies(results: List<MoviePreview>) {
        val newResultsList = mutableListOf<MoviePreview>()
        newResultsList.addAll(movies)
        newResultsList.addAll(results)
        val result = DiffUtil.calculateDiff(
            MovieListDiffUtilCallback(
                movies,
                newResultsList
            )
        )
        result.dispatchUpdatesTo(this)
        movies.addAll(results)
//        notifyDataSetChanged()
    }

    fun appendShows(results: List<TvPreview>) {
        val newResultsList = mutableListOf<TvPreview>()
        newResultsList.addAll(shows)
        newResultsList.addAll(results)
        val result = DiffUtil.calculateDiff(
            ShowListDiffUtilCallback(
                shows,
                newResultsList
            )
        )
        result.dispatchUpdatesTo(this)
        shows.addAll(results)
//        notifyDataSetChanged()
    }

    fun appendCollections(results: List<Collection>) {
        val newResultsList = mutableListOf<Collection>()
        newResultsList.addAll(collections)
        newResultsList.addAll(results)
        val result = DiffUtil.calculateDiff(
            CollectionListDiffUtilCallback(
                collections,
                newResultsList
            )
        )
        result.dispatchUpdatesTo(this)
        collections.addAll(results)
//        notifyDataSetChanged()
    }

    fun appendMix(results: List<MultiSearch>) {
        val newResultsList = mutableListOf<MultiSearch>()
        newResultsList.addAll(mix)
        newResultsList.addAll(results)
        val result = DiffUtil.calculateDiff(
            MixListDiffUtilCallback(
                mix,
                newResultsList
            )
        )
        result.dispatchUpdatesTo(this)
        mix.addAll(results)
//        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: MediaLinearViewHolder) {
        super.onViewRecycled(holder)
        holder.imageTask?.cancel(true)
    }

    inner class MovieListDiffUtilCallback(
        private var oldMovies: List<MoviePreview>,
        private var newMovies: List<MoviePreview>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldMovies.size

        override fun getNewListSize(): Int = newMovies.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldMovies[oldItemPosition].id == newMovies[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldMovies[oldItemPosition] == newMovies[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldMovie = oldMovies[oldItemPosition]
            val newMovie = newMovies[newItemPosition]

            val diffBundle = Bundle()

            if (oldMovie.id != newMovie.id) {
                diffBundle.putParcelable("movie", newMovie)
            }

            return if (diffBundle.size() == 0) null else diffBundle

        }

    }

    inner class ShowListDiffUtilCallback(
        private var oldShows: List<TvPreview>,
        private var newShows: List<TvPreview>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldShows.size

        override fun getNewListSize(): Int = newShows.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldShows[oldItemPosition].id == newShows[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldShows[oldItemPosition] == newShows[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldShow = oldShows[oldItemPosition]
            val newShow = newShows[newItemPosition]

            val diffBundle = Bundle()

            if (oldShow.id != newShow.id) {
                diffBundle.putParcelable("show", newShow)
            }

            return if (diffBundle.size() == 0) null else diffBundle

        }

    }

    inner class CollectionListDiffUtilCallback(
        private var oldCollections: List<Collection>,
        private var newCollections: List<Collection>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldCollections.size

        override fun getNewListSize(): Int = newCollections.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldCollections[oldItemPosition].id == newCollections[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldCollections[oldItemPosition] == newCollections[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldCollection = oldCollections[oldItemPosition]
            val newCollection = newCollections[newItemPosition]

            val diffBundle = Bundle()

            if (oldCollection.id != newCollection.id) {
                diffBundle.putParcelable("collection", newCollection)
            }

            return if (diffBundle.size() == 0) null else diffBundle

        }

    }

    inner class MixListDiffUtilCallback(
        private var oldMixes: List<MultiSearch>,
        private var newMixes: List<MultiSearch>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldMixes.size

        override fun getNewListSize(): Int = newMixes.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldMixes[oldItemPosition].id == newMixes[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldMixes[oldItemPosition] == newMixes[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldMix = oldMixes[oldItemPosition]
            val newMix = newMixes[newItemPosition]

            val diffBundle = Bundle()

            if (oldMix.id != newMix.id) {
                diffBundle.putParcelable("mix", newMix)
            }

            return if (diffBundle.size() == 0) null else diffBundle

        }

    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 200
        view.startAnimation(anim)
    }

    private fun setPlaceHolder(imageView: ImageView) {
        val drawable = ContextCompat.getDrawable(activity.get()!!, R.drawable.ic_placeholder)
        val bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable!!.setBounds(10, 20, canvas.width - 10, canvas.height - 20)
        drawable.draw(canvas)
        imageView.setImageBitmap(bitmap)
    }

    private fun fetchCacheImage(key: String): Bitmap? {
        val memoryCache = (activity.get()?.application as DiscoverApplication).memoryCache
        Log.d("key", "$key error")
        return memoryCache[key]
    }

    private fun fetchFromNetwork(url: String, holder: MediaLinearViewHolder) {
        holder.imageTask?.cancel(true)
        holder.imageTask = LoadPosterImage(
            WeakReference(holder.image),
            activity
        )
        holder.imageTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
    }

    private fun setPosterImage(holder: MediaLinearViewHolder, posterPath: String) {
        val posterCache = fetchCacheImage(posterPath)
        if (posterCache == null) {
            fetchFromNetwork(posterPath, holder)
        } else
            holder.image.setImageBitmap(posterCache)
    }

}