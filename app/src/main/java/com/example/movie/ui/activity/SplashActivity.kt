package com.example.movie.ui.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.movie.R
import com.example.movie.databinding.ActivitySplashBinding
import com.example.movie.untils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sarnava.textwriter.TextWriter


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar!!.hide()
        binding.textWriter
            .setWidth(12F)
            .setDelay(30)
            .setColor(getColor(R.color.black))
            .setConfig(TextWriter.Configuration.INTERMEDIATE)
            .setSizeFactor(30f)
            .setLetterSpacing(25f)
            .setText("THE MOVIE")
            .startAnimation()

        Handler().postDelayed({
            startActivity(Intent(this, UserActivity::class.java))
            finish()
        }, 3000)
    }

}





