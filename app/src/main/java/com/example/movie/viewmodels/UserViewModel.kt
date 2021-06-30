package com.example.movie.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation.findNavController
import com.example.movie.R
import com.example.movie.untils.Constants

class UserViewModel(application: Application) : AndroidViewModel(application) {

    // 뮤터블은 값을 수정, 삭제, 추가 가능
    private val _isLoading = MutableLiveData(false)

    // private 을 get() 으로 초기화 한 이유
    // 객체지향의 캡슐화를 지키기 위해서다.
    val isLoading: MutableLiveData<Boolean> get() = _isLoading


    fun checkBtn() {
        isLoading.value = true
    }


}