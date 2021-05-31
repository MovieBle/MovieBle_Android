package com.example.movie.repositorys

import com.example.movie.data.Movie
import com.example.movie.network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    // 이코드를 썻을 뿐기인데 어떻게 NetworkModule Object 에 있는거라고 알 수 있지 ..?
    private val apiInterface: ApiInterface

) {

    suspend fun getRecipes(queries: Map<String, String>): Response<Movie> {
        return apiInterface.getRecentMovie(queries)
    }
}
