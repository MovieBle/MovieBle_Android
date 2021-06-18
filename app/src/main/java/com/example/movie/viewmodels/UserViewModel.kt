package com.example.movie.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation.findNavController
import com.example.movie.R
import com.example.movie.untils.Constants

class UserViewModel(application: Application) : AndroidViewModel(application) {
     val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)


    fun checkBtn() {
        Log.d(Constants.TAG, "checkBtn: ")
        isLoading.value = true
    }


}