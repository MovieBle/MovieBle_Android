package com.example.movie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapters.SearchViewAdapter
import com.example.movie.data.Result
import com.example.movie.databinding.FragmentSearchPostBinding
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.viewmodels.NetworkViewModel


class SearchPostFragment : Fragment() {


    private var _binding: FragmentSearchPostBinding? = null
    private val binding get() = _binding!!


    var movie_recycler: RecyclerView? = null
    private val networkViewModel: NetworkViewModel by viewModels()

    private var page = 1
    var movieList = mutableListOf<Result>()
    lateinit var mSearchAdapter: SearchViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {


        _binding = FragmentSearchPostBinding.inflate(inflater, container, false)
        mSearchAdapter = SearchViewAdapter(movieList, requireContext())


        //실행안됨 error
        networkViewModel.getAll().observe(viewLifecycleOwner, Observer {

            Log.d(TAG, "SearchPostFragment - onCreateView() called")
            mSearchAdapter.setList(it.results as MutableList<Result>)
            mSearchAdapter.notifyItemRangeInserted((page - 1) * 19, 19)
        })


        movie_recycler = binding.searchRecycler

        setHasOptionsMenu(true)

        recyclerViewPaging()

        setRecentAdapter(movieList)
        //getNowPlayingMovies(page,requireContext())

        networkViewModel.getNowPlayingMovies(page)


        binding.refreshLayout.setOnRefreshListener {

            mSearchAdapter.notifyDataSetChanged() // 새로고침 하고
            binding.refreshLayout.isRefreshing = false // 새로고침을 완료하면 아이콘을 없앤다.


        }



        return binding.root

    }

    fun recyclerViewPaging() {


        movie_recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                //recyclerView 마지막일때
                if (!movie_recycler!!.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    //  mSearchAdapter.deleteLoading()
                    Log.d(TAG, "onScrolled Position: $lastVisibleItemPosition")
                    networkViewModel.getNowPlayingMovies(++page)
                    Log.d(TAG, "onScrolled : Page: $page")

                }
            }
        })
    }


    fun setRecentAdapter(movieList: List<Result>) {

        mSearchAdapter = SearchViewAdapter(movieList as MutableList<Result>, requireContext())

        movie_recycler?.adapter = mSearchAdapter
        movie_recycler?.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        movie_recycler?.setHasFixedSize(false)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.menu_action_search).actionView as SearchView

        searchView.queryHint = getString(R.string.search_view_hint)


    //recyclerVIew filter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: ")
                mSearchAdapter.filter?.filter(query)
                movieList.let { mSearchAdapter?.setFilterData(it) }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: ")
                mSearchAdapter.filter?.filter(newText)
                movieList.let { mSearchAdapter?.setFilterData(it) }

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