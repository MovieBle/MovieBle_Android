package com.example.movie.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movie.data.Movie
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteData(movie: MovieEntity)

    @Query("SELECT * FROM like_movie_table ORDER BY id ASC")
    fun getAllData() : LiveData<List<MovieEntity>>
}