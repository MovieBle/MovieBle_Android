package com.example.movie.untils

import androidx.room.TypeConverter
import com.example.movie.data.Movie
import com.example.movie.data.Result
import com.example.movie.data.database.MovieEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// json 을 저장할려면 converter 로 String 값으로 바꿔야함
class MovieTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToResult(data: String): Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType)
    }


    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

}