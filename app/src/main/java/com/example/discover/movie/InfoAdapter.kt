package com.example.discover.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R

class InfoAdapter(private val infoList: List<InfoClass>) :
    RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    class InfoViewHolder(infoView: View) : RecyclerView.ViewHolder(infoView) {
        val image: ImageView = infoView.findViewById(R.id.icon)
        val content: TextView = infoView.findViewById(R.id.content_info)
        val title: TextView = infoView.findViewById(R.id.title_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InfoViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.info_list_layout,
            parent,
            false
        )
    )

    override fun getItemCount() = infoList.size

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val info = infoList[position]
        holder.image.setImageDrawable(
            ContextCompat.getDrawable(
                holder.itemView.context,
                info.image
            )
        )
        if (info.content == null || info.content.trim().isEmpty())
            holder.content.text = "-"
        else
            holder.content.text = info.content

        holder.title.text = info.title
    }
}