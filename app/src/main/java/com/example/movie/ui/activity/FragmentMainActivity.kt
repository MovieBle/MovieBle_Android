package com.example.movie.ui.activity

import android.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.movie.R
import com.example.movie.adapters.LikeViewAdapter
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.ActivityNaviMainBinding
import com.example.movie.ui.fragment.ExampleMovieFragment
import com.example.movie.ui.fragment.MovieListFragment
import com.example.movie.ui.fragment.ProfileFragment
import com.example.movie.ui.fragment.SearchPostFragment
import com.example.movie.untils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi


@AndroidEntryPoint
class FragmentMainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var navi: BottomNavigationView
    private lateinit var navController: NavController
    private val binding by lazy { ActivityNaviMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navi = binding.bottomNavigationView


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
            // fragment id 가 아닐 시 bottom navigation 안뜸
            if (destination.id == R.id.profileFragment || destination.id == R.id.movieListFragment || destination.id == R.id.searchPostFragment) {
                navi.visibility = View.VISIBLE
            } else {
                navi.visibility = View.GONE
            }
        }
    }





}

