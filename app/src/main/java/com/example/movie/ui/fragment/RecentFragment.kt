package com.example.movie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.movie.adapters.RecentAdapter
import com.example.movie.data.Result
import com.example.movie.databinding.FragmentRecentBinding
import com.example.movie.models.RecentItem
import com.example.movie.network.RecentBuilder
import com.example.movie.untils.Constants
import com.example.movie.untils.Constants.Companion.TAG
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecentFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRecentBinding? = null
    private val binding get() = _binding!!


    private lateinit var recentViewPager: ViewPager2

    private var recentViewList = ArrayList<RecentItem>()
    private var recentList = ArrayList<Result>()

    lateinit var previousBtn: ImageView
    lateinit var nextBtn: ImageView
    private lateinit var recentAdapter: RecentAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
            : View {

        recyclerViewSet()


        recentViewPager = binding.recentViewPager
        previousBtn = binding.previousBtn
        nextBtn = binding.nextBtn

        nextBtn.setOnClickListener(this)
        previousBtn.setOnClickListener(this)





        getPopularMovies()

        return binding.root
    }


    private fun recyclerViewSet() {
        // 리사이클러뷰
        recentViewPager.apply {

            adapter = recentAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL


            binding.dotsIndicator.setViewPager2(this)
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            previousBtn ->
                recentViewPager.currentItem = recentViewPager.currentItem - 1
            nextBtn ->
                recentViewPager.currentItem = recentViewPager.currentItem + 1
        }
    }

    fun getPopularMovies(page: Int = 1): Boolean {

        RecentBuilder.service.getMovie(page = page).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {


                Log.d(TAG, "onResponse: ")

                recentViewList.add(RecentItem("https://image.tmdb.org/t/p/w500" + response.body()?.poster_path, response.body()?.title))


                Log.d(TAG, "onResponse: $recentViewList ")

            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })

        return false

    }


}







