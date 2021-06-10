package com.example.movie.data.database.entities.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movie.models.Movie
import com.example.movie.untils.Constants

@Entity(tableName = Constants.MOVIE_List_TABLE)
class MovieListEntity(
    var movie: Movie
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}