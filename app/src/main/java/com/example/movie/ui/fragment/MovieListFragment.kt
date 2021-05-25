package com.example.movie.ui.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapters.LikeViewAdapter
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.FragmentMoiveListBinding
import com.example.movie.observeOnce
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.untils.MovieCase
import com.example.movie.untils.NetworkResult
import com.example.movie.viewmodels.DatabaseViewModel
import com.example.movie.viewmodels.NetworkViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class MovieListFragment : Fragment() {


    private var _binding: FragmentMoiveListBinding? = null
    private val binding get() = _binding!!

    private val databaseViewModel: DatabaseViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()

    private var like_recycler: ShimmerRecyclerView? = null
    private var popular_recycler: ShimmerRecyclerView? = null
    private var recent_recycler: ShimmerRecyclerView? = null
    private var top_recycler: ShimmerRecyclerView? = null
    private var discover_recycler: ShimmerRecyclerView? = null

    // 코드 리팩토링 필요
    private val listAdapter1: MovieListAdapter by lazy {
        MovieListAdapter(

            MovieCase.MOVIE_LIST

        )
    }
    private val listAdapter2: MovieListAdapter by lazy {
        MovieListAdapter(
            MovieCase.MOVIE_LIST

        )
    }
    private val listAdapter3: MovieListAdapter by lazy {
        MovieListAdapter(
            MovieCase.MOVIE_LIST

        )
    }
    private val listAdapter4: MovieListAdapter by lazy {
        MovieListAdapter(
            MovieCase.MOVIE_LIST

        )
    }
    private val likeAdapter: LikeViewAdapter by lazy {
        LikeViewAdapter(

            requireContext()

        )
    }

    private val page = 1

    var likeList = emptyList<MovieEntity>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {


        _binding = FragmentMoiveListBinding.inflate(inflater, container, false)


        popular_recycler = binding.popularRecycler
        recent_recycler = binding.recentRecycler
        top_recycler = binding.topRecycler
        like_recycler = binding.likeRecycler
        discover_recycler = binding.discoverRecycler


        databaseViewModel.getAllData.observe(
            viewLifecycleOwner,

            androidx.lifecycle.Observer { data ->
                // 데이터가 있으면 초기화
                databaseViewModel.checkIfDatabaseEmpty(data)
                likeAdapter.setLikeData(data)
            })


        databaseViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showLikeData(it)

        })


        // recyclerView 연결

        setPopularAdapter()
        setLikeAdapter()
        setTopAdapter()
        setRecentAdapter()
        setDiscoverAdapter()

        // viewModel 연결 <-> network 연결

        // 하나의 연결이 4개의 recyclerView에 연결됨 각각의로 연결되게 로직필요

        // 데이터를가져옴 파라미터로 넘기기 ?

        networkViewModel.getTopRatedMovies(page)

        networkViewModel.getRecentMovies(page)

        networkViewModel.getPopularMovies(page)

        networkViewModel.getDiscoverMovies(page)


        // 각각의 데이터를 가져오고 그것을 각각의 리사이클러뷰에 연결한다.
        readDataBase()



        networkViewModel.getAllPopular().observe(viewLifecycleOwner, Observer {
            listAdapter1.setData(it.results)
            listAdapter1.notifyItemRangeInserted((page - 1) * 19, 19)
            recyclerViewEvent(popular_recycler!!)

        })
        networkViewModel.getAllTop().observe(viewLifecycleOwner, Observer {
            listAdapter2.setData(it.results)

            listAdapter2.notifyItemRangeInserted((page - 1) * 19, 19)
            recyclerViewEvent(top_recycler!!)
        })
        networkViewModel.grtAllDiscover().observe(viewLifecycleOwner, Observer {
            listAdapter3.setData(it.results)
            recyclerViewEvent(discover_recycler!!)

            listAdapter3.notifyItemRangeInserted((page - 1) * 19, 19)

        })
        networkViewModel.getAllRecent().observe(viewLifecycleOwner, Observer {
            listAdapter4.setData(it.results)

            listAdapter4.notifyItemRangeInserted((page - 1) * 19, 19)
            recyclerViewEvent(recent_recycler!!)
        })


        return binding.root
    }


    private fun showLikeData(emptyData: Boolean) {
        if (emptyData) {
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }

    private fun setLikeAdapter() {

        Log.d(TAG, "setLikeAdapter: ")
        like_recycler?.adapter = likeAdapter
        like_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        like_recycler?.setHasFixedSize(false)


    }

    private fun recyclerViewEvent(recyclerView: RecyclerView) {
        when (recyclerView) {
            recent_recycler -> {
                recyclerViewAddPlus(recent_recycler!!, binding.moreRecent)
            }
            top_recycler -> {
                recyclerViewAddPlus(top_recycler!!, binding.moreTop)
            }
            popular_recycler -> {
                recyclerViewAddPlus(popular_recycler!!, binding.morePopular)
            }
            discover_recycler -> {
                recyclerViewAddPlus(discover_recycler!!, binding.moreDiscover)
            }
        }
    }


    private fun recyclerViewAddPlus(recyclerView: RecyclerView, text: TextView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("WrongConstant")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                //recyclerView 마지막일때
                if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    //  mSearchAdapter.deleteLoading()

                    text.visibility = ConstraintSet.VISIBLE

                    when (text) {
                        binding.moreRecent -> {
                            text.setOnClickListener {
                                findNavController().navigate(R.id.action_movieListFragment_to_addMovieFragment)
                            }

                        }
                    }


                } else text.visibility = View.INVISIBLE
            }
        })
    }

    fun setPopularAdapter() {

        Log.d(TAG, "setAdapter: ")
        popular_recycler?.adapter = listAdapter1
        popular_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popular_recycler?.setHasFixedSize(false)


    }


    private fun setTopAdapter() {


        Log.d(TAG, "setTopAdapter: ")
        top_recycler?.adapter = listAdapter2
        top_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        top_recycler?.setHasFixedSize(false)


    }


    private fun setRecentAdapter() {

        Log.d(TAG, "setRecentAdapter: ")
        recent_recycler?.adapter = listAdapter3
        recent_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recent_recycler?.setHasFixedSize(false)


    }

    private fun setDiscoverAdapter() {

        discover_recycler?.adapter = listAdapter4
        discover_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        discover_recycler?.setHasFixedSize(false)


    }


    private fun readDataBase() {

        // 비동기 생성
        lifecycleScope.launch {
            databaseViewModel.readMovie.observeOnce(viewLifecycleOwner, { database ->
                // 비어있지 않다면
                if (database.isNotEmpty()) {

                    Log.d(ContentValues.TAG, "readDataBase: ")
                    likeAdapter.setLikeData(listOf(database[0]))

                } else {

                    // 비어있으면
                    requestApiData()
                }

            })
        }
    }

    private fun requestApiData() {

        databaseViewModel.getAllDataJson.observe(viewLifecycleOwner, { response ->
//           // response 상태에 따라 로직 나누기
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        likeAdapter.setLikeData((listOf(it)))
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

            }
        })
    }

    private fun loadDataFromCache() {

        // 코루틴 속 에넣음
        lifecycleScope.launch {

            databaseViewModel.getAllData.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    likeAdapter.setLikeData(likeList)
                }
            })
        }
    }

    private fun showShimmerEffect() {

        discover_recycler?.showShimmer()
        like_recycler?.showShimmer()
        popular_recycler?.showShimmer()
        recent_recycler?.showShimmer()
        top_recycler?.showShimmer()
    }

    private fun hideShimmerEffect() {
        discover_recycler?.hideShimmer()
        like_recycler?.hideShimmer()
        popular_recycler?.hideShimmer()
        recent_recycler?.hideShimmer()
        top_recycler?.hideShimmer()
    }
}



