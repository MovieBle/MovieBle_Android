package com.example.movie.data.database

import androidx.room.*
import com.example.movie.data.database.entities.MovieEntity
import com.example.movie.data.database.entities.MovieLikeEntity

@Database(
    entities=[MovieEntity::class, MovieLikeEntity::class],
    version=12   ,
    exportSchema = false

)

@TypeConverters(MovieTypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

}