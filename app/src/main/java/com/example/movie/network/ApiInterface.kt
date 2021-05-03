package com.example.movie.network

import com.example.movie.data.Movie
import com.example.movie.untils.Constants.Companion.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/upcoming")
    fun getMovie(
            @Query("api_key") apiKey: String = API_KEY,
            @Query("page") page:Int,
            @Query("language") language: String = "ko-KR"

    ): Call<Movie>

    @GET("movie/popular")
    fun getPopularMovie(
            @Query("api_key") apiKey: String = API_KEY,
            @Query("page") page:Int,
            @Query("language") language: String = "ko-KR"

    )  : Call<Movie>



}