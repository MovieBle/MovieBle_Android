package com.example.movie.network

import com.example.movie.data.Movie
import com.example.movie.data.Viedo.GetVideoResponse
import com.example.movie.di.NetworkModule
import com.example.movie.untils.Constants.Companion.API_KEY
import com.example.movie.untils.Constants.Companion.MOVIE_ID
import com.example.movie.viewmodels.NetworkViewModel
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import javax.inject.Singleton


//binds -> @Module 어노테이션
interface ApiInterface {

    @GET("movie/upcoming")
   suspend fun getRecentMovie(
        @QueryMap queries: Map<String, String>

    ): Response<Movie>
    @GET("movie/popular")

    fun getPopularMovie(
        @QueryMap queries: Map<String, String>

    ): Response<Movie>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @QueryMap queries: Map<String, String>
    ): Response<Movie>

    @GET("movie/724989/videos")
    fun getVideoTrailer(
        @Path("movie_id") movieID: Long = MOVIE_ID, //선택된 movie id
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "ko-KR"
    ): Call<GetVideoResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @QueryMap queries: Map<String, String>
    ): Call<Movie>

    @GET("movie/now_playing")
    fun getDiscoverMovies(
        @QueryMap queries: Map<String, String>
    ): Response<Movie>

    @GET("genre/movie/list")
    fun getMovieList(
        @QueryMap queries: Map<String, String>
    ): Response<Movie>


}