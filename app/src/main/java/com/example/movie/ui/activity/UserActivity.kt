package com.example.movie.ui.activity

import android.view.WindowManager
import com.example.movie.R
import com.example.movie.base.UtilityBase
import com.example.movie.databinding.ActivityUserBinding

@Suppress("DEPRECATION")
class UserActivity : UtilityBase.BaseActivity<ActivityUserBinding>(R.layout.activity_user) {
    override fun ActivityUserBinding.onCreate() {
        supportActionBar!!.title = ""
        supportActionBar!!.hide()
         window.setFlags(
             WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}