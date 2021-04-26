package com.example.movie.network

import com.example.movie.data.Result
import com.example.movie.untils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("")
    suspend fun getMovie(
        @Query("KEY") KEY: String = API_KEY,
        @Query("original_title") original_title: String,
        @Query("overview") overview: String,
        @Query("poster_path") poster_path: String,
        @Query("backdrop_path") backdrop_path: String,
        @Query("release_date") release_date: String

    ): Response<Result>


}