package com.example.discover.movie

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import com.example.discover.dataModel.credits.Cast
import com.example.discover.dataModel.credits.Crew
import com.example.discover.utils.LoadPosterImage
import java.lang.ref.WeakReference


class CreditAdapter(private val isCrew: Boolean, private val activity: WeakReference<Activity>) :
    RecyclerView.Adapter<CreditAdapter.CreditViewHolder>() {

    var crewList = emptyList<Crew>()
    var castList = emptyList<Cast>()

    inner class CreditViewHolder(creditView: View) : RecyclerView.ViewHolder(creditView) {
        val image: ImageView = creditView.findViewById(R.id.creditImage)
        val name: TextView = creditView.findViewById(R.id.name)
        val job: TextView = creditView.findViewById(R.id.job)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CreditViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.credit_layout,
            parent,
            false
        )
    )

    override fun getItemCount() = if (isCrew) crewList.size else castList.size

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        if (isCrew) {
            val crew = crewList[position]
            if (crew.profile_path == null)
                setPlaceHolder(holder.image)
            else
                LoadPosterImage(
                    WeakReference(holder.image),
                    activity
                ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, crew.profile_path, "120", "120")
            holder.name.text = crew.name
            holder.job.text = crew.job
        } else {
            val cast = castList[position]
            if (cast.profile_path == null)
                setPlaceHolder(holder.image)
            else
                LoadPosterImage(
                    WeakReference(holder.image),
                    activity
                ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cast.profile_path, "100", "100")
            holder.name.text = cast.name
            holder.job.text = cast.character
        }
    }

    private fun setPlaceHolder(imageView: ImageView) {
        val drawable =
            ContextCompat.getDrawable(imageView.context, R.drawable.ic_credit_placeholder)
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas);
        imageView.setImageBitmap(bitmap)

    }
}