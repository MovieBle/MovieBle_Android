package com.example.movie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.MovieCase
import com.example.movie.viewmodels.DatabaseViewModel
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.data.Movie
import com.example.movie.data.Result
import com.example.movie.databinding.FragmentMoiveListBinding
import com.example.movie.network.RecentBuilder
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.viewmodels.ShareViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@InternalCoroutinesApi
@Suppress("UNREACHABLE_CODE")
class MovieListFragment : Fragment() {


    private var _binding: FragmentMoiveListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DatabaseViewModel by viewModels()
    private val sharedViewModel: ShareViewModel by viewModels()
    private var like_recycler: RecyclerView? = null
    private var popular_recycler: RecyclerView? = null
    private var recent_recycler: RecyclerView? = null
    private var top_recycler: RecyclerView? = null

    private lateinit var adapter: MovieListAdapter


    var movieList = emptyList<Result>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    )
            : View? {


        _binding = FragmentMoiveListBinding.inflate(inflater, container, false)

        adapter = MovieListAdapter(movieList, MovieCase.LIST_VIEW, requireContext())


        popular_recycler = binding.popularRecycler
        recent_recycler = binding.recentRecycler
        top_recycler = binding.topRecycler
        like_recycler = binding.likeRecycler



        viewModel.getAllData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data ->
            // 데이터가 있으면 초기화
            viewModel.checkIfDatabaseEmpty(data)
            adapter.setLikeData(data)
        })


        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
           showLikeData(it)

        })


        getPopularMovies()
        getMovie()
        getTopRatedMovies()

        setLikeAdapter(movieList)
        return binding.root
    }

    private fun showLikeData(emptyData: Boolean) {
        if (emptyData) {
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility=View.VISIBLE
        }else{
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility=View.INVISIBLE
        }
    }



    private fun setAdapter(movieList: List<Result>) {

        adapter = MovieListAdapter(movieList, MovieCase.LIST_VIEW, requireContext())

        popular_recycler?.adapter = adapter
        popular_recycler?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popular_recycler?.setHasFixedSize(false)
    }

    private fun setLikeAdapter(movieList: List<Result>) {

        adapter = MovieListAdapter(movieList, MovieCase.LIST_VIEW, requireContext())

        like_recycler?.adapter = adapter
        like_recycler?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        like_recycler?.setHasFixedSize(false)
    }

    private fun setTopAdapter(movieList: List<Result>) {


        adapter = MovieListAdapter(movieList, MovieCase.LIST_VIEW, requireContext())
        top_recycler?.adapter = adapter
        top_recycler?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        top_recycler?.setHasFixedSize(false)
    }

    private fun setRecentAdapter(movieList: List<Result>) {


        adapter = MovieListAdapter(movieList, MovieCase.LIST_VIEW, requireContext())
        recent_recycler?.adapter = adapter
        recent_recycler?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recent_recycler?.setHasFixedSize(false)
    }


    private fun getMovie(page: Int = 1): Boolean {


        RecentBuilder.service.getMovie(page = page).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {


                if (response.isSuccessful) {


                    val body = response.body()
                    body?.let {
                        setRecentAdapter(it.results)
                        Log.d(TAG, "onResponse: ${it.results}")

                    }

                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })

        return false

    }

    private fun getPopularMovies(page: Int = 1): Boolean {

        RecentBuilder.service.getPopularMovie(page = page).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {


                if (response.isSuccessful) {


                    val body = response.body()
                    body?.let {
                        setAdapter(it.results)
                        Log.d(TAG, "onResponse: ${it.results}")

                    }

                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })

        return false

    }

    private fun getTopRatedMovies(page: Int = 1): Boolean {

        RecentBuilder.service.getTopRatedMovies(page = page).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {

                if (response.isSuccessful) {


                    val body = response.body()
                    body?.let {
                        setTopAdapter(it.results)
                        Log.d(TAG, "onResponse: ${it.results}")

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