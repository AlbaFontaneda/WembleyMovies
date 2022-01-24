package com.example.wembleymoviesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class MovieItem(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("adult")
    var isAdult: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val poster: String,

    var isLiked: Boolean = false
): Serializable