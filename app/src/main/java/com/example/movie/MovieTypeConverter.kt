package com.example.movie

import androidx.room.TypeConverter
import com.example.movie.data.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(movie: Movie): String {

        return gson.toJson(movie)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): Movie {

        val listType = object : TypeToken<Movie>() {}.type
        return gson.fromJson(data, listType)
    }
}