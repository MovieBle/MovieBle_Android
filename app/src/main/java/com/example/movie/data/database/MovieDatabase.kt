package com.example.movie.data.database

import androidx.room.*
import com.example.movie.data.database.entities.MovieLikeEntity
import com.example.movie.data.database.entities.movie.*

@Database(
    entities = [MovieEntity::class, MovieLikeEntity::class, MovieDiscoverEntity::class, MoviePopularEntity::class, MovieRecentEntity::class,MovieListEntity::class],
    version = 14,
    exportSchema = false

)

@TypeConverters(MovieTypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

}