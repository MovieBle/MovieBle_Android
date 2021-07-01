package com.example.movie.data

import com.example.movie.data.network.ApiInterface
import com.example.movie.models.Movie
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiInterface: ApiInterface
) {


    suspend fun getMovie(page:Int): Response<Movie> {

        return apiInterface.getMovie(page)
    }

}
