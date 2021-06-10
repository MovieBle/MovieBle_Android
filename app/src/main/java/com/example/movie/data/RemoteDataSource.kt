package com.example.movie.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movie.models.Movie
import com.example.movie.network.ApiInterface
import com.example.movie.untils.Constants.Companion.TAG
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiInterface: ApiInterface

) {

    suspend fun getPopularMovie(queries: Map<String, String>, page: Int): Response<Movie> {
        Log.d(TAG, "RemoteDataSource - getPopularMovie() called")
        return apiInterface.getPopularMovie(queries, page)
    }

    suspend fun getDiscoverMovie(queries: Map<String, String>, page: Int): Response<Movie> {
        Log.d(TAG, "RemoteDataSource - getDiscoverMovie() called")
        return apiInterface.getDiscoverMovies(queries, page)
    }

    suspend fun getRecentMovie(queries: Map<String, String>, page: Int): Response<Movie> {
        Log.d(TAG, "RemoteDataSource - getRecentMovie() called")
        return apiInterface.getRecentMovie(queries, page)
    }

    suspend fun getTopMovie(queries: Map<String, String>, page: Int): Response<Movie> {
        Log.d(TAG, "RemoteDataSource - getTopMovie() called")
        return apiInterface.getTopRatedMovies(queries, page)
    }

    suspend fun getMovieList(queries: Map<String, String>, page: Int): Response<Movie> {
        Log.d(TAG, "RemoteDataSource - getTopMovie() called")
        return apiInterface.getMovieList(queries, page)
    }

}
