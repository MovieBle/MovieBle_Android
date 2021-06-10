package com.example.movie.models

import com.example.movie.models.Dates
import com.example.movie.models.Result

data class  Movie(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)