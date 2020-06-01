package com.example.discover.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import com.example.discover.dataModel.reviews.Review

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(reviewView: View) : RecyclerView.ViewHolder(reviewView) {
        val author: TextView = reviewView.findViewById(R.id.author)
        val content: TextView = reviewView.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.review_layout,
            parent,
            false
        )
    )

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.author.text = review.author
        holder.content.text = review.content
    }
}