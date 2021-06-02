package com.example.movie.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movie.data.database.entities.MovieEntity
import com.example.movie.data.database.entities.MovieLikeEntity


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikeMovie(movie: MovieLikeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteLikeMovie(movie: MovieLikeEntity)

    @Query("SELECT * FROM like_movie_table ORDER BY id ASC")
    fun getLikeAllData() : LiveData<List<MovieLikeEntity>>

    @Query("SELECT * FROM movie_table ORDER BY id ASC")
    fun getAllData() : LiveData<List<MovieEntity>>
}