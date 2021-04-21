package com.example.movie.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.FragmentMainActivity
import com.example.movie.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, FragmentMainActivity::class.java))
        }

    }
}