package com.example.movie.data

import com.example.movie.data.database.MovieDao
import com.example.movie.data.database.entities.MovieLikeEntity
import com.example.movie.data.database.entities.UserDao
import com.example.movie.data.database.entities.UserEntity
import com.example.movie.data.database.entities.movie.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DatabaseSource @Inject constructor(
    private val movieDao: MovieDao,

) {






    fun getAllMovie(): Flow<List<MovieEntity>> {
        return movieDao.getAllData()
    }

   suspend fun insertMovie(movie: MovieEntity) {
        return movieDao.insertMovie(movie)

    }


}