package com.example.movie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.databinding.FragmentSearchPostBinding
import com.example.movie.models.Result
import com.example.movie.observeOnce
import com.example.movie.untils.Constants.Companion.MOVIE_RecentT_ABLE
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.untils.MovieCase
import com.example.movie.untils.NetworkResult
import com.example.movie.viewmodels.DatabaseViewModel
import com.example.movie.viewmodels.QueryViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchPostFragment : Fragment() {


    private var _binding: FragmentSearchPostBinding? = null
    private val binding get() = _binding!!

    @InternalCoroutinesApi
    private val databaseViewModel: DatabaseViewModel by viewModels()
    private val queryViewModel: QueryViewModel by viewModels()
    var movie_recycler: ShimmerRecyclerView? = null

    private var page = 1
    var movieList = mutableListOf<Result>()

     private val movieListAdapter: MovieListAdapter by lazy{
         MovieListAdapter(
             MovieCase.MOVIE_SEARCH
         )
     }


    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {


        _binding = FragmentSearchPostBinding.inflate(inflater, container, false)


        movie_recycler = binding.searchRecycler

        requestApiData()

        recyclerViewPaging()

        setRecentAdapter()


        binding.refreshLayout.setOnRefreshListener {
            movieListAdapter.notifyDataSetChanged() // 새로고침 하고

            readDataBase(page)

            binding.refreshLayout.isRefreshing = false // 새로고침을 완료하면 아이콘을 없앤다.


        }

        databaseViewModel.getAllDiscoverData.observe(viewLifecycleOwner, Observer {
            movieListAdapter.notifyItemRangeInserted((page - 1) * 19, 19)

        })


        Log.d(TAG, "onCreateView: $page")
        readDataBase(page++)

        setHasOptionsMenu(true)
        return binding.root

    }

    @InternalCoroutinesApi
    fun recyclerViewPaging() {

        val upBtn = binding.upBtn
        movie_recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                Log.d(TAG, "position: $lastVisibleItemPosition")
                if (lastVisibleItemPosition > 19) {


                    upBtn.visibility = View.VISIBLE
                    binding.upBtn.setOnClickListener() {

                        movie_recycler!!.scrollToPosition(0)

                    }
                } else binding.upBtn.visibility = View.GONE

                //recyclerView 마지막일때
                if (!movie_recycler!!.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {

                    movie_recycler?.showShimmer()
                    readDataBase(++page)
                }


            }
        })
    }


    @InternalCoroutinesApi
    private fun readDataBase(page: Int) {

        // 비동기 생성
        lifecycleScope.launch {
            databaseViewModel.getAllDiscoverData.observeOnce(viewLifecycleOwner, { database ->
                Log.d(TAG, "addPage: $page")
                databaseViewModel.getDiscoverMovie(queryViewModel.getQuery(), page)

                if (database.isNotEmpty()) {
                    movieListAdapter.setData(database[0].movie)
                } else {
                    // 비어있으면
                    requestApiData()
                }

            })
        }
    }


    @InternalCoroutinesApi
    private fun requestApiData() {


        databaseViewModel.getAllMovie.observe(viewLifecycleOwner, { response ->

            Log.d(TAG, "requestApiData: ")
//           // response 상태에 따라 로직 나누기
            when (response) {

                is NetworkResult.Success -> {
                    //데이터가 들어오면
                    Log.d(TAG, "requestApiData: success")
                    hideShimmerEffect()
                    response.data?.let {
                        movieListAdapter.setData(((it)))
                    }


                }
                is NetworkResult.Error -> {
                    Log.d(TAG, "requestApiData: error")
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    movie_recycler?.showShimmer()

                }

                is NetworkResult.Loading -> {
                    Log.d(TAG, "requestApiData: loading")
                    movie_recycler?.showShimmer()
                }

            }
        })
    }


    private fun hideShimmerEffect() {
        movie_recycler?.hideShimmer()

    }


    private fun setRecentAdapter() {


        movie_recycler?.adapter = movieListAdapter
        movie_recycler?.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        movie_recycler?.setHasFixedSize(false)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.menu_action_search).actionView as SearchView

        searchView.queryHint = getString(R.string.search_view_hint)
        Log.d(TAG, "onCreateOptionsMenu: ")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: ")
                movieListAdapter.filter.filter(query)
                movieList.let { movieListAdapter.setFilterData(it) }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: ")
                movieListAdapter.filter.filter(newText)
                movieList.let { movieListAdapter.setFilterData(it) }

                return true
            }

        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_action_search ->
                true
            else -> super.onOptionsItemSelected(item)
        }

    }


}