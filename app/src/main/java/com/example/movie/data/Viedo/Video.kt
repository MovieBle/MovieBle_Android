package com.example.movie.data.Viedo

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("id") val video_id : String, //@SerialzedName 은 변수명을 다르게 하고 싶을때 사용
    @SerializedName("key") val video_key : String,
    @SerializedName("name") val video_name : String,
    @SerializedName("size") val video_size : Int
)