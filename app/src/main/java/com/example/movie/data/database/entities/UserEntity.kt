package com.example.movie.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movie.untils.Constants


@Entity(tableName = Constants.USER_TABLE)
class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,


) {

}