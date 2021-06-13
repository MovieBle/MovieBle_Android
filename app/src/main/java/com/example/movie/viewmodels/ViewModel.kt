package com.example.movie.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.models.ChatModel
import com.example.movie.ui.activity.SplashActivity.Companion.userName
import com.example.movie.untils.App
import com.example.movie.untils.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.InternalCoroutinesApi

class ViewModel {



    fun imageLoad(img: ImageView, poster: String) {
        Glide.with(App.instance)
            .load(Constants.BASE_IMG_URL + poster)
            .centerCrop()
            .placeholder(R.drawable.test_post)
            .into(img)
    }

    fun toastText(view: Context, message:String){
        Toast.makeText(view,message,Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(message: String, view: View) {
        Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }


    @InternalCoroutinesApi
    fun startGetUserInfo() {
        val auth = FirebaseAuth.getInstance()
        Log.d("Mylog", "my uid : ${auth.currentUser?.uid}")

        var database: DatabaseReference = FirebaseDatabase.getInstance().reference
        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userData = auth.currentUser?.uid?.let {
                    snapshot.child(it).getValue(
                        ChatModel::class.java
                    )
                }

                userName = userData?.chatName





            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }


}