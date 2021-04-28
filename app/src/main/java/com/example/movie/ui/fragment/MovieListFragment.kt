package com.example.movie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.data.Movie
import com.example.movie.data.Result
import com.example.movie.databinding.FragmentMoiveListBinding
import com.example.movie.network.RecentBuilder
import com.example.movie.untils.Constants.Companion.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNREACHABLE_CODE")
class MovieListFragment : Fragment() {
    private var _binding: FragmentMoiveListBinding? = null

    var movieListAdapter: MovieListAdapter ?=null
    private val binding get() = _binding!!

     var like_recycler:RecyclerView?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {




        getPopularMovies()

        return binding.root
    }


    private fun setAdapter(movieList : List<Result>){
        like_recycler = binding.likeRecycler //adapter

        val mAdapter = MovieListAdapter(movieList,requireContext())
        like_recycler?.adapter = mAdapter
        like_recycler?.layoutManager = LinearLayoutManager(requireContext())
        like_recycler?.setHasFixedSize(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getPopularMovies(page: Int = 1): Boolean {

        RecentBuilder.service.getMovie(page = page).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {

                val img = response.body()?.results?.get(1)?.poster_path
                var originalTitle = response.body()?.results?.get(1)?.original_title



                if (response.isSuccessful) {


                    val body = response.body()
                    body?.let {
                        setAdapter(it.results)
                        Log.d(TAG, "onResponse: ")
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