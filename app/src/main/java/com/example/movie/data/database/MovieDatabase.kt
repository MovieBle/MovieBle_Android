package com.example.movie.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movie.MovieTypeConverter
import kotlinx.coroutines.InternalCoroutinesApi

@Database(
    entities=[MovieEntity::class],
    version=1,
    exportSchema = false

)

@TypeConverters(MovieTypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context): MovieDatabase {

            val temInstance = INSTANCE
            if (temInstance != null) {
                return temInstance
            }
            kotlinx.coroutines.internal.synchronized(this) {

                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "like_movie_database"
                ).build()


                INSTANCE = instance
                return instance

            }

        }
    }

}