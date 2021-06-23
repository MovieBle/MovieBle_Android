package com.example.movie.ui.fragment.addMovie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.databinding.FragmentAddDiscoverMovieBinding
import com.example.movie.untils.Constants
import com.example.movie.untils.MovieCase
import com.example.movie.untils.NetworkResult
import com.example.movie.viewmodels.DatabaseViewModel
import com.example.movie.viewmodels.QueryViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi


@AndroidEntryPoint
class AddDiscoverMovieFragment : Fragment() {

    private var _binding: FragmentAddDiscoverMovieBinding? = null
    private val binding get() = _binding!!

    @InternalCoroutinesApi
    private val databaseViewModel: DatabaseViewModel by viewModels()
    private val queryViewModel: QueryViewModel by viewModels()
    private val listAdapter: MovieListAdapter by lazy {
        MovieListAdapter(
           MovieCase.MOVIE_DISCOVER

        )
    }
    var page = 1
    private var discover_recycler: ShimmerRecyclerView? = null

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {

        _binding = FragmentAddDiscoverMovieBinding.inflate(inflater, container, false)

        discover_recycler = binding.discoverAddRecycler
        setTopAdapter()
        requestApiData()
        return binding.root
    }

    private fun setTopAdapter() {


        discover_recycler?.adapter = listAdapter
        discover_recycler?.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        discover_recycler?.setHasFixedSize(true)


    }





    @InternalCoroutinesApi
    private fun requestApiData() {


        databaseViewModel.getAllMovie.observe(viewLifecycleOwner, { response ->

            Log.d(Constants.TAG, "requestApiData: ")
//           // response 상태에 따라 로직 나누기
            when (response) {

                is NetworkResult.Success -> {
                    //데이터가 들어오면
                    Log.d(Constants.TAG, "requestApiData: success")

                    response.data?.let {
                        listAdapter.setData(((it)))
                    }


                }
                is NetworkResult.Error -> {
                    Log.d(Constants.TAG, "requestApiData: error")

                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    discover_recycler?.showShimmer()

                }

                is NetworkResult.Loading -> {
                    Log.d(Constants.TAG, "requestApiData: loading")
                    discover_recycler?.showShimmer()
                }

            }
        })
    }


}