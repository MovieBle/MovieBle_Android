package com.example.movie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.untils.MovieCase
import com.example.movie.R
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.data.Movie
import com.example.movie.data.Result
import com.example.movie.databinding.FragmentSearchPostBinding
import com.example.movie.network.RecentBuilder
import com.example.movie.untils.Constants.Companion.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchPostFragment : Fragment() {


    private var _binding: FragmentSearchPostBinding? = null
    private val binding get() = _binding!!
    var mSearchAdapter: MovieListAdapter? = null

    private val movieList: List<Result>? = null
    var movie_recycler: RecyclerView? = null
    private val viewmodel: com.example.movie.viewmodels.ViewModel by viewModels()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
            : View {

        mSearchAdapter?.notifyDataSetChanged()
        movieList?.let { mSearchAdapter?.setData(it) }
        getNowPlayingMovies()
        _binding = FragmentSearchPostBinding.inflate(inflater, container, false)

        movie_recycler = binding.searchRecycler
        setHasOptionsMenu(true)
        return binding.root
    }


    private fun setRecentAdapter(movieList: List<Result>) {

        mSearchAdapter = MovieListAdapter(movieList, MovieCase.SEARCH_LIST_VIEW, requireContext())

        movie_recycler?.adapter = mSearchAdapter
        movie_recycler?.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        movie_recycler?.setHasFixedSize(false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.menu_action_search).actionView as SearchView

        searchView.queryHint = getString(R.string.search_view_hint)




            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "onQueryTextSubmit: ")
                    mSearchAdapter?.filter?.filter(query)
                    movieList?.let { mSearchAdapter?.setData(it) }

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "onQueryTextChange: ")
                    mSearchAdapter?.filter?.filter(newText)
                    movieList?.let { mSearchAdapter?.setData(it) }

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

        private fun getNowPlayingMovies(page: Int = 1): Boolean {

        RecentBuilder.service.getNowPlayingMovies(page = page).enqueue(object : Callback<Movie> {
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


}