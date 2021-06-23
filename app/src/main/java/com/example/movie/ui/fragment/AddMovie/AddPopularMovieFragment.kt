package com.example.movie.ui.fragment.addMovie
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.databinding.FragmentPopularAddMovieBinding
import com.example.movie.untils.Constants
import com.example.movie.untils.MovieCase
import com.example.movie.untils.NetworkResult
import com.example.movie.viewmodels.DatabaseViewModel
import com.example.movie.viewmodels.QueryViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPopularMovieFragment : Fragment() {

    private var _binding: FragmentPopularAddMovieBinding? = null
    private val binding get() = _binding!!

    private val listAdapter: MovieListAdapter by lazy {
        MovieListAdapter(
            MovieCase.MOVIE_POPULAR

        )
    }
    var page=1
    private val popular_recycler: ShimmerRecyclerView by lazy{
        binding.popularAddRecycler
    }
    @InternalCoroutinesApi


    private lateinit var databaseViewModel: DatabaseViewModel

    private lateinit var queryViewModel: QueryViewModel

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        databaseViewModel = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)

        queryViewModel = ViewModelProvider(requireActivity()).get(QueryViewModel::class.java)

    }
    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {

        _binding = FragmentPopularAddMovieBinding.inflate(inflater, container, false)


        setPopularAdapter()

        return binding.root
    }

    private fun setPopularAdapter() {


        popular_recycler.adapter = listAdapter
        popular_recycler.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        popular_recycler.setHasFixedSize(true)


    }


    @InternalCoroutinesApi
    private fun requestPopularApiData() {


        databaseViewModel.getAllMovie.observe(viewLifecycleOwner, { response ->


            when (response) {

                is NetworkResult.Success -> {
                    Log.d(Constants.TAG, "requestApiData: success")
                    popular_recycler.hideShimmer()
                    response.data?.let {

                        listAdapter.setData(((it)))

                    }
                }
                is NetworkResult.Error -> {
                    popular_recycler.hideShimmer()

                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {

                    popular_recycler.showShimmer()
                }

            }
        })
    }
}