package com.example.wembleymoviesapp

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class Config {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/original"

        const val API_KEY = "3136eeb68c464e699fc66620af5abe67"

        const val UPDATE_MOVIE_REQUEST = "update_movie_request"
        const val UPDATE_MOVIE_KEY = "update_movie"
    }
}


fun ImageView.loadImage(imageUri: String) {
    val options: RequestOptions = RequestOptions()
        .placeholder(R.drawable.ic_baseline_image_24)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    Glide.with(WembleyApplication.application)
        .setDefaultRequestOptions(options)
        .load(imageUri)
        .into(this)
}