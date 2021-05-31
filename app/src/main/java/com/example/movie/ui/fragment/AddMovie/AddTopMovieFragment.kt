package com.example.movie.ui.fragment.AddMovie
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.databinding.FragmentAddRecentMovieBinding
import com.example.movie.databinding.FragmentTopAddMovieBinding
import com.example.movie.untils.Constants
import com.example.movie.untils.MovieCase
import com.example.movie.viewmodels.NetworkViewModel

class AddTopMovieFragment : Fragment() {

    private var _binding: FragmentTopAddMovieBinding? = null
    private val binding get() = _binding!!

    private val listAdapter: MovieListAdapter by lazy {
        MovieListAdapter(
            MovieCase.MOVIE_TOP

        )
    }
    var page=1
    private val networkViewModel: NetworkViewModel by viewModels()
    private var top_recycler: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {

        _binding = FragmentTopAddMovieBinding.inflate(inflater, container, false)

        top_recycler = binding.topRecycler
        setTopAdapter()

        networkViewModel.getTopRatedMovies(page)

        networkViewModel.getAllTop().observe(viewLifecycleOwner, Observer {
            listAdapter.setData(it.results)
            listAdapter.notifyItemRangeInserted((page - 1) * 19, 19)

        })
        recyclerViewAddPlus()
        return binding.root
    }

    private fun recyclerViewAddPlus() {
        top_recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                //recyclerView 마지막일때
                if (!top_recycler!!.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    //  mSearchAdapter.deleteLoading()
                    Log.d(Constants.TAG, "onScrolled Position: $lastVisibleItemPosition")
                    networkViewModel.getTopRatedMovies(++page)
                    Log.d(Constants.TAG, "onScrolled : Page: $page")

                }
            }
        })
    }
    private fun setTopAdapter() {


        top_recycler?.adapter = listAdapter
        top_recycler?.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        top_recycler?.setHasFixedSize(true)


    }
}