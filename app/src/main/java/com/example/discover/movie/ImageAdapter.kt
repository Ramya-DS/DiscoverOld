package com.example.discover.movie

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import com.example.discover.dataModel.images.ImageDetails
import com.example.discover.utils.LoadBackdropImage
import com.example.discover.utils.LoadPosterImage
import java.lang.ref.WeakReference

class ImageAdapter(
    private val isBackdrop: Boolean,
    private val list: List<ImageDetails>,
    private val activity: WeakReference<Activity>
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(imageView: View) : RecyclerView.ViewHolder(imageView) {
        val image: ImageView = imageView.findViewById(R.id.backdropImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.backdrop_viewpager,
            parent,
            false
        )
    )


    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (isBackdrop) {
            getWidth()?.let {
                displayPlaceholder(holder.image, it, (it / 1.77777).toInt())
                LoadBackdropImage(
                    WeakReference(holder.image),
                    activity
                ).executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR,
                    list[position].file_path,
                    it.toString(),
                    (it / 1.77777).toInt().toString()
                )
            }
        } else {
            displayPlaceholder(holder.image, 120, 180)
            holder.image.scaleType=ImageView.ScaleType.FIT_XY
            LoadPosterImage(
                WeakReference(holder.image),
                activity
            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list[position].file_path)
        }
    }

    private fun getWidth(): Int? {
        val displayMetrics: DisplayMetrics? = activity.get()?.resources?.displayMetrics
        Log.d("density", "${displayMetrics?.density}")
        return displayMetrics?.widthPixels
    }

    private fun displayPlaceholder(imageView: ImageView, width: Int, height: Int) {
        val drawable =
            ContextCompat.getDrawable(imageView.context, R.drawable.ic_backdrop_placeholder)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas);
        imageView.setImageBitmap(bitmap)
    }
}