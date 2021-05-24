package com.example.movie.network

import com.example.movie.data.Movie
import com.example.movie.data.Viedo.GetVideoResponse
import com.example.movie.untils.Constants.Companion.API_KEY
import com.example.movie.untils.Constants.Companion.MOVIE_ID
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/upcoming")
    fun getRecentMovie(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int,
        @Query("language") language: String = "ko-KR"

    ): Call<Movie>

    @GET("movie/popular")
    fun getPopularMovie(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int,
        @Query("language") language: String = "ko-KR"

    ): Call<Movie>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int,
        @Query("language") language: String = "ko-KR"
    ): Call<Movie>

    @GET("movie/724989/videos")
    fun getVideoTrailer(
        @Path("movie_id") movieID: Long = MOVIE_ID, //선택된 movie id
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "ko-KR"
    ): Call<GetVideoResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page : Int,
        @Query("language") language : String = "ko-KR"
    ): Call<Movie>

    @GET("movie/now_playing")
    fun getDiscoverMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page : Int,
        @Query("language") language : String = "ko-KR"
    ): Call<Movie>

    @GET("genre/movie/list")
    fun getMovieList(
            @Query("api_key") apiKey: String = API_KEY,
            @Query("page") page: Int,
            @Query("language") language: String = "ko-KR"
    ): Call<Movie>


}