package com.example.movie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.data.Movie
import com.example.movie.data.Result
import com.example.movie.databinding.FragmentMoiveListBinding
import com.example.movie.databinding.FragmentSearchPostBinding
import com.example.movie.network.RecentBuilder
import com.example.movie.untils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchPostFragment : Fragment() {


    private var _binding: FragmentSearchPostBinding? = null
    private val binding get() = _binding!!

    var movie_recycler: RecyclerView? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
            : View? {
        _binding = FragmentSearchPostBinding.inflate(inflater, container, false)
        getMovie()
        movie_recycler=binding.searchRecycler
        return binding.root
    }

    private fun setRecentAdapter(movieList: List<Result>) {



        val mAdapter = MovieListAdapter(movieList, requireContext())
        movie_recycler?.adapter = mAdapter
        movie_recycler?.layoutManager =
                GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL, false,)
        movie_recycler?.setHasFixedSize(false)
    }






    fun getMovie(page: Int = 1): Boolean {

        RecentBuilder.service.getMovie(page = page).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {


                if (response.isSuccessful) {


                    val body = response.body()
                    body?.let {
                        setRecentAdapter(it.results)
                        Log.d(Constants.TAG, "onResponse: ${it.results}")

                    }

                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })

        return false

    }
}