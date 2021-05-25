package com.example.movie.repositorys

import androidx.lifecycle.LiveData
import com.example.movie.data.Movie
import com.example.movie.data.database.MovieDao
import com.example.movie.data.database.MovieEntity
import com.example.movie.network.ApiInterface
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class DatabaseRepository(private val movieDao: MovieDao) {

    val getAllData: LiveData<List<MovieEntity>> = movieDao.getAllData()

    fun readDatabase(): LiveData<List<MovieEntity>> {
        return movieDao.getAllData()
    }
    suspend fun insertData(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    suspend fun deleteData(movie: MovieEntity) {
        movieDao.deleteData(movie)
    }

}