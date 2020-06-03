package com.example.discover.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import com.example.discover.DiscoverApplication
import java.io.BufferedInputStream
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL

class LoadPosterImage(
    private val imageView: WeakReference<ImageView>,
    private val activity: WeakReference<Activity>
) : AsyncTask<String, Unit, Bitmap>() {

    override fun doInBackground(vararg params: String?): Bitmap? {
        val fileName = params[0]
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null

        fileName?.let {

            val url = "https://image.tmdb.org/t/p/w185/$it"
            try {
                inputStream = URL(url).openStream()
                bitmap = inputStream?.let { createScaledBitmapFromStream(inputStream!!) }
                (activity.get()?.application as DiscoverApplication).memoryCache.put(it, bitmap!!)
                Log.d("bitmap", "${bitmap?.height} ${bitmap?.width}")
            } catch (e: Exception) {
                Log.d("LoadImage", "Error occurred. ${e.message}")
                this.cancel(true)
            }

        }
        return bitmap
    }

    override fun onPostExecute(result: Bitmap?) {
        result?.let {
            imageView.get()?.setImageBitmap(it)
        }
    }

    private fun createScaledBitmapFromStream(
        s: InputStream,
        minimumDesiredBitmapWidth: Int = 360,
        minimumDesiredBitmapHeight: Int = 540
    ): Bitmap? {
        val stream = BufferedInputStream(s, 8 * 1024)
        val decodeBitmapOptions = BitmapFactory.Options()
        if (minimumDesiredBitmapWidth > 0 && minimumDesiredBitmapHeight > 0) {
            val decodeBoundsOptions = BitmapFactory.Options()
            decodeBoundsOptions.inJustDecodeBounds = true
            stream.mark(8 * 1024)
            BitmapFactory.decodeStream(stream, null, decodeBoundsOptions)
            stream.reset()
            val originalWidth: Int = decodeBoundsOptions.outWidth
            val originalHeight: Int = decodeBoundsOptions.outHeight

            val scale =
                (originalWidth / minimumDesiredBitmapWidth).coerceAtMost(originalHeight / minimumDesiredBitmapHeight)

            val bitmap: Bitmap?
            if (scale > 1) {
                decodeBitmapOptions.inSampleSize = 1.coerceAtLeast(scale)
                bitmap = BitmapFactory.decodeStream(stream, null, decodeBitmapOptions)

            } else
                bitmap = BitmapFactory.decodeStream(stream)
            stream.close()
            return bitmap
        }
        return null
    }
}