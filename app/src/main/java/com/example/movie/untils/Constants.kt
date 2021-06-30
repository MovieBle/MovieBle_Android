package com.example.movie.untils

import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.security.crypto.EncryptedSharedPreferences

class Constants {
    companion object {

        const val BASE_URL = "http://34.64.158.240:8000"
        //TAG
        const val TAG = "TAG"
        //database
        const val DATABASE_NAME = "like_movie_database"
        const val MOVIE_LIKE_TABLE = "like_movie_table"
        const val MOVIE_TABLE="movie_table"
        const val USER_TABLE="user_table"

    }

}

