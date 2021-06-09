package com.example.movie.data.database

import androidx.room.*
import com.example.movie.data.database.entities.MovieLikeEntity
import com.example.movie.data.database.entities.movie.*
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikeMovie(movie: MovieLikeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopularMovie(movie: MoviePopularEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentMovie(movie: MovieRecentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscoverMovie(movie: MovieDiscoverEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieListMovie(movie: MovieListEntity)


    @Delete
    suspend fun deleteLikeMovie(movie: MovieLikeEntity)

    @Query("SELECT * FROM like_movie_table ORDER BY id ASC")
    fun getLikeAllData(): Flow<List<MovieLikeEntity>>

    @Query("SELECT * FROM movie_table ORDER BY id ASC")
    fun getAllData(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie_popular_table ORDER BY id ASC")
    fun getAllPopularData(): Flow<List<MoviePopularEntity>>

    @Query("SELECT * FROM movie_recent_table ORDER BY id ASC")
    fun getAllRecentData(): Flow<List<MovieRecentEntity>>

    @Query("SELECT * FROM movie_discover_table ORDER BY id ASC")
    fun getAllDiscoverData(): Flow<List<MovieDiscoverEntity>>

    @Query("SELECT * FROM MOVIE_LIST_TABLE ORDER BY id ASC")
    fun getAllMovieListData(): Flow<List<MovieListEntity>>
}