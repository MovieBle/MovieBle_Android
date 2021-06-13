package com.example.movie.models

import android.widget.ImageView

data class ChatModel(val chatText:String,val chatDay:String,val chatName:String,val chatLikeText:Int){
    constructor() : this("", "", "", 0)
}