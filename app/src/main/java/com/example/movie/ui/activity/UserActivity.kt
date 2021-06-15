package com.example.movie.ui.activity

import com.example.movie.R
import com.example.movie.base.UtilityBase
import com.example.movie.databinding.ActivityUserBinding

class UserActivity : UtilityBase.BaseActivity<ActivityUserBinding>(R.layout.activity_user) {
    override fun ActivityUserBinding.onCreate() {
        supportActionBar!!.title = ""
        supportActionBar!!.hide()
    }
}