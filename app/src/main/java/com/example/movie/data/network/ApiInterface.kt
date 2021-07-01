package com.example.movie.data.network

import androidx.lifecycle.MutableLiveData
import com.example.movie.models.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//binds -> @Module 어노테이션
interface ApiInterface {

    @GET(" Movie/1/")
    suspend fun getMovie(@Query("page") page: Int): Response<Movie>



}