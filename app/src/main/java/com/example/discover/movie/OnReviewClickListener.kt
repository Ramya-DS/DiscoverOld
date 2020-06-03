package com.example.discover.movie

import com.example.discover.dataModel.reviews.Review

interface OnReviewClickListener {
    fun onReviewClicked(review: Review)
}