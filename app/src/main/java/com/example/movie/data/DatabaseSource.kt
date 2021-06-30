package com.example.movie.data

import com.example.movie.data.database.MovieDao

import com.example.movie.data.database.entities.movie.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DatabaseSource @Inject constructor(
    private val movieDao: MovieDao,

) {

    fun getAllMovie(): Flow<List<MovieEntity>> {
        return movieDao.getAllData()
    }

    fun getAllLikeMovie(): Flow<List<MovieLikeEntity>> {
        return movieDao.getAllLikeData()
    }

   suspend fun insertMovie(movie: MovieEntity) {
        return movieDao.insertMovie(movie)

    }

    suspend fun insertLikeMovie(movie: MovieLikeEntity) {
        return movieDao.insertLikeMovie(movie)

    }

    suspend fun deleteLikeMovie(movie: MovieLikeEntity) {
        return movieDao.deleteLikeMovie(movie)

    }


}