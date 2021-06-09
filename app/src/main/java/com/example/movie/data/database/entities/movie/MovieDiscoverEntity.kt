package com.example.movie.data.database.entities.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movie.models.Movie
import com.example.movie.untils.Constants

@Entity(tableName = Constants.MOVIE_Discover_TABLE)
class MovieDiscoverEntity(
    var movie: Movie
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}