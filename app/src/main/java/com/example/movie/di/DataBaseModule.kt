package com.example.movie.di

import android.content.Context
import androidx.room.Room
import com.example.movie.data.database.MovieDatabase
import com.example.movie.untils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
//DataBase에 쓸것을여깄다 씀
object DataBaseModule {


    @Singleton
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        Constants.DATABASE_NAME

    ).fallbackToDestructiveMigration().build()


    @Singleton
    @Provides
    fun provideDao(database: MovieDatabase) = database.movieDao()
}