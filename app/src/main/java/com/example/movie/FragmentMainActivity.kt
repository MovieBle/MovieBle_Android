package com.example.movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movie.databinding.ActivityNaviMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentMainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var navi: BottomNavigationView
    private lateinit var navController: NavController
    private val binding by lazy { ActivityNaviMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d(TAG, "MainActivity - onCreate() called")
        //NavController 생성
        navController = findNavController(R.id.navHostFragment)

        //앱 바 구성성
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movieListFragment,
                R.id.searchPostFragment,
                R.id.profileFragment
            )
        )
        navi = binding.bottomNavigationView
        initNavigation()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navi.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigation() {
        val navController = findNavController(R.id.navHostFragment)
        navi.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.profileFragment || destination.id == R.id.movieListFragment || destination.id == R.id.searchPostFragment ) {
                navi.visibility = View.VISIBLE
            } else {
                navi.visibility = View.GONE
            }
        }
    }
}