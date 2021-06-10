package com.example.movie.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movie.models.Result
import com.example.movie.untils.Constants
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel

@Suppress("DEPRECATED_ANNOTATION", "INAPPLICABLE_IGNORED_ON_PARCEL_CONSTRUCTOR_PROPERTY")
@Entity(tableName = Constants.MOVIE_LIKE_TABLE)
@Parcelize
class MovieLikeEntity(
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true) // 기본키 중복 x
    var id: Int = 0,
    var result: Result
) : Parcelable {

}