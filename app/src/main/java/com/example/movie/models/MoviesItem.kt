package com.example.movie.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoviesItem(
    val adult: Boolean,
    val backdrop: String,
    val budget: Int,
    val cast: String,
    val director: String,
    val genres: String,
    val overview: String,
    val poster: String,
    val production: String,
    val release: String,
    val runtime: Int,
    val status: String,
    val title: String,
    val video: String,
    val voteAver: Double,
    val voteCount: Int
) : Parcelable