package com.example.movie.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movie.data.Movie


@Dao
interface MovieDao {

    @Insert
   suspend fun insertMovie(movie: Movie)


    @Query("SELECT * FROM like_movie_table ORDER BY id ASC")
    fun readMovie() :List<MovieEntity>
}