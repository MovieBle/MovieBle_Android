package com.example.movie.network

import androidx.lifecycle.MutableLiveData
import com.example.movie.models.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


//binds -> @Module 어노테이션
interface ApiInterface {

    @GET("movie/upcoming")
    suspend fun getRecentMovie(
        @QueryMap queries: Map<String, String>,
        @Query("page") page: Int,
    ): Response<Movie>

    @GET("movie/popular")

    suspend fun getPopularMovie(
        @QueryMap queries: Map<String, String>,
        @Query("page") page: Int,

        ): Response<Movie>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @QueryMap queries: Map<String, String>,
        @Query("page") page: Int,
    ): Response<Movie>


    @GET("movie/now_playing")
    suspend fun getDiscoverMovies(
        @QueryMap queries: Map<String, String>,
        @Query("page") page: Int,
    ): Response<Movie>

    @GET("genre/movie/list")
    fun getMovieList(
        @QueryMap queries: Map<String, String>,
        @Query("page") page: Int,
    ): Response<Movie>


//    @GET("movie/724989/videos")
//    fun getVideoTrailer(
//        @Path("movie_id") movieID: Long = MOVIE_ID, //선택된 movie id
//        @Query("api_key") apiKey: String = API_KEY,
//        @Query("language") language: String = "ko-KR"
//    ): Call<GetVideoResponse>
//
//    @GET("movie/now_playing")
//    fun getNowPlayingMovies(
//        @QueryMap queries: Map<String, String>
//    ): Call<Movie>

}