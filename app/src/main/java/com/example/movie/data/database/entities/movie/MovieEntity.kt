package com.example.movie.data.database.entities.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movie.models.Movie
import com.example.movie.models.Result
import com.example.movie.untils.Constants.Companion.MOVIE_TABLE

@Entity(tableName = MOVIE_TABLE)
class MovieEntity(
    var movie: Movie
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}