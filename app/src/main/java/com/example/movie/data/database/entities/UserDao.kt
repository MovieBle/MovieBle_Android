package com.example.movie.data.database.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {


    @Insert
    suspend fun insertUser(userEntity: UserEntity)


    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllUser(): Flow<List<UserEntity>>
}