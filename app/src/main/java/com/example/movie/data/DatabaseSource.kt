package com.example.movie.data

import com.example.movie.data.database.MovieDao
import com.example.movie.data.database.entities.MovieLikeEntity
import com.example.movie.data.database.entities.movie.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DatabaseSource @Inject constructor(
    private val movieDao: MovieDao
) {


    fun getLikeAllMovie(): Flow<List<MovieLikeEntity>> {
        return movieDao.getLikeAllData()
    }
    fun getAllMovie(): Flow<List<MovieEntity>> {
        return movieDao.getAllData()
    }

    fun getAllPopularMovie(): Flow<List<MoviePopularEntity>> {
        return movieDao.getAllPopularData()
    }

    fun getAllRecentMovie(): Flow<List<MovieRecentEntity>> {
        return movieDao.getAllRecentData()
    }
    fun getAllDiscoverMovie(): Flow<List<MovieDiscoverEntity>> {
        return movieDao.getAllDiscoverData()
    }
    fun getAllMovieListMovie(): Flow<List<MovieListEntity>> {
        return movieDao.getAllMovieListData()
    }


    suspend fun insertLikeMovie(movie: MovieLikeEntity) {
        movieDao.insertLikeMovie(movie)
    }
    suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    suspend fun insertPopularMovie(movie: MoviePopularEntity) {
        movieDao.insertPopularMovie(movie)
    }

    suspend fun insertRecentMovie(movie: MovieRecentEntity) {
        movieDao.insertRecentMovie(movie)
    }

    suspend fun insertDiscoverMovie(movie: MovieDiscoverEntity) {
        movieDao.insertDiscoverMovie(movie)
    }

    suspend fun insertMovieListMovie(movie: MovieListEntity) {
        movieDao.insertMovieListMovie(movie)
    }


    suspend fun deleteLikeMovie(movie: MovieLikeEntity) {
        movieDao.deleteLikeMovie(movie)
    }

}