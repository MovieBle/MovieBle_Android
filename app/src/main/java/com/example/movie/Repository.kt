package com.example.movie

import androidx.lifecycle.LiveData
import com.example.movie.data.Movie
import com.example.movie.data.database.MovieDao
import com.example.movie.data.database.MovieEntity

class Repository(private val movieDao: MovieDao) {

    val getAllData: LiveData<List<MovieEntity>> = movieDao.getAllData()


    suspend fun insertData(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    suspend fun deleteData(movie: MovieEntity) {
        movieDao.deleteData(movie)
    }

}