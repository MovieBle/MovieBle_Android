package com.example.movie.viewmodels

import android.app.Application
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.untils.App
import com.example.movie.untils.Constants
import com.google.android.material.snackbar.Snackbar

class ViewModel(application: Application) : AndroidViewModel(application) {



    fun imageLoad(img: ImageView, poster: String) {
        Glide.with(App.instance)
            .load(Constants.BASE_IMG_URL + poster)
            .centerCrop()
            .placeholder(R.drawable.test_post)
            .into(img)
    }

    fun showSnackBar(message: String, view: View) {
        Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }



}