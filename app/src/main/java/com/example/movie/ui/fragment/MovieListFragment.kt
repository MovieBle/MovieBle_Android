package com.example.movie.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapters.LikeViewAdapter
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.data.database.entities.MovieLikeEntity
import com.example.movie.databinding.FragmentMoiveListBinding
import com.example.movie.models.Result
import com.example.movie.observeOnce
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.untils.MovieCase
import com.example.movie.untils.NetworkResult
import com.example.movie.viewmodels.DatabaseViewModel
import com.example.movie.viewmodels.QueryViewModel
import com.sergivonavi.materialbanner.Banner
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment() : Fragment() {


    private var _binding: FragmentMoiveListBinding? = null
    private val binding get() = _binding!!


    private var like_recycler: ShimmerRecyclerView? = null
    private var popular_recycler: ShimmerRecyclerView? = null
    private var recent_recycler: ShimmerRecyclerView? = null
    private var top_recycler: ShimmerRecyclerView? = null
    private var discover_recycler: ShimmerRecyclerView? = null

    // 코드 리팩토링 필요
    private var listPopularAdapter: MovieListAdapter? = null
    private var listTopAdapter: MovieListAdapter? = null
    private var listRecentAdapter: MovieListAdapter? = null
    private var listDiscoverAdapter: MovieListAdapter? = null

    @InternalCoroutinesApi
    private val likeAdapter: LikeViewAdapter by lazy {
        LikeViewAdapter(

            requireContext()

        )
    }

    var likeList = emptyList<MovieLikeEntity>()

    @InternalCoroutinesApi
    private lateinit var databaseViewModel: DatabaseViewModel

    private lateinit var queryViewModel: QueryViewModel

    var movieList = emptyList<Result>()

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

        _binding = FragmentMoiveListBinding.inflate(inflater, container, false)

        popular_recycler = binding.popularRecycler
        recent_recycler = binding.recentRecycler
        top_recycler = binding.topRecycler
        like_recycler = binding.likeRecycler
        discover_recycler = binding.discoverRecycler

        databaseViewModel.getAllLikeData.observe(
            viewLifecycleOwner,

            { data ->
                // 데이터가 있으면 초기화
                databaseViewModel.checkIfDatabaseEmpty(data)
                likeAdapter.setLikeData(data)
            })


        databaseViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showLikeData(it)

        })



        setLikeAdapter()

        setPopularAdapter()
        setTopAdapter()
        setRecentAdapter()
        setDiscoverAdapter()


        readPopularDataBase()
        readTopDataBase()
        readDiscoverDataBase()
        readRecentDataBase()


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


    // recyclerView Adapter 연동
    @InternalCoroutinesApi
    private fun setLikeAdapter() {

        like_recycler?.adapter = likeAdapter
        like_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        like_recycler?.setHasFixedSize(false)

    }

    private fun setPopularAdapter() {
        listPopularAdapter = MovieListAdapter(MovieCase.MOVIE_LIST)
        popular_recycler?.adapter = listPopularAdapter
        popular_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popular_recycler?.setHasFixedSize(false)
        recyclerViewEvent(popular_recycler!!)

    }

    private fun setTopAdapter() {
        listTopAdapter = MovieListAdapter(MovieCase.MOVIE_LIST)
        top_recycler?.adapter = listTopAdapter
        top_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        top_recycler?.setHasFixedSize(false)
        recyclerViewEvent(top_recycler!!)


    }

    private fun setRecentAdapter() {
        listRecentAdapter = MovieListAdapter(MovieCase.MOVIE_LIST)
        recent_recycler?.adapter = listRecentAdapter
        recent_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recent_recycler?.setHasFixedSize(false)
        recyclerViewEvent(recent_recycler!!)


    }

    private fun setDiscoverAdapter() {
        listDiscoverAdapter = MovieListAdapter(movieCase = MovieCase.MOVIE_LIST)
        discover_recycler?.adapter = listDiscoverAdapter
        discover_recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        discover_recycler?.setHasFixedSize(false)
        recyclerViewEvent(discover_recycler!!)
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


    private fun recyclerViewAddPlus(recyclerView: ShimmerRecyclerView, text: TextView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                //recyclerView 마지막일때
                if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {

                    text.visibility = VISIBLE

                    when (text) {
                        binding.moreRecent -> {
                            text.setOnClickListener {
                                findNavController().navigate(R.id.action_movieListFragment_to_addMovieRecentFragment)
                            }

                        }
                        binding.moreDiscover -> {
                            text.setOnClickListener {
                                findNavController().navigate(R.id.action_movieListFragment_to_addDiscoverMovieFragment)
                            }
                        }
                        binding.moreTop -> {
                            text.setOnClickListener {
                                findNavController().navigate(R.id.action_movieListFragment_to_addTopMovieFragment)
                            }
                        }
                        binding.morePopular -> {
                            text.setOnClickListener {
                                findNavController().navigate(R.id.action_movieListFragment_to_addPopularMovieFragment)
                            }
                        }
                    }


                } else text.visibility = View.INVISIBLE
            }
        })
    }


    @InternalCoroutinesApi
    private fun readPopularDataBase() {

        // 비동기 생성
        lifecycleScope.launch {


            databaseViewModel.getAllPopularData.observeOnce(viewLifecycleOwner, { database ->

                databaseViewModel.getPopularMovie(queryViewModel.getQuery(), 1)
                if (database.isNotEmpty()) {

                    Log.d(TAG, "readDiscoverDataBase: ")
                    listPopularAdapter?.setData(database[0].movie)


                } else {
                    // 비어있으면
                    requestPopularApiData()

                }

            })
        }
    }

    @InternalCoroutinesApi
    private fun readDiscoverDataBase() {

        // 비동기 생성
        lifecycleScope.launch {
            databaseViewModel.getAllDiscoverData.observeOnce(viewLifecycleOwner, { database ->

                setDiscoverAdapter()
                if (database.isNotEmpty()) {

                    Log.d(TAG, "readDiscoverDataBase: ")
                    listDiscoverAdapter?.setData(database[0].movie)


                } else {
                    // 비어있으면
                    requestDiscoverApiData()
                }

            })
        }
    }



    @InternalCoroutinesApi
    private fun bannerFloat() {


        val banner: Banner = binding.banner
        banner.setLeftButtonListener {

            banner.dismiss()
            }

        banner.setRightButtonListener {

            banner.dismiss()

        }

        banner.show()


    }


    @InternalCoroutinesApi
    private fun readRecentDataBase() {

        // 비동기 생성
        lifecycleScope.launch {
            databaseViewModel.getRecentMovie(queryViewModel.getQuery(), 1)

            databaseViewModel.getAllRecentData.observeOnce(viewLifecycleOwner, { database ->
                setRecentAdapter()
                if (database.isNotEmpty()) {

                    Log.d(TAG, "readRecentDataBase: ")
                    listRecentAdapter?.setData(database[0].movie)

                } else {
                    // 비어있으면
                    requestRecentApiData()
                }

            })
        }
    }

    @InternalCoroutinesApi
    private fun readTopDataBase() {

        // 비동기 생성
        lifecycleScope.launch {

            databaseViewModel.getAllData.observeOnce(viewLifecycleOwner, { database ->
                setTopAdapter()

                databaseViewModel.getTopMovie(queryViewModel.getQuery(), 1)
                if (database.isNotEmpty()) {
                    Log.d(TAG, "readTopDataBase: ")
                    listTopAdapter?.setData(database[0].movie)

                } else {
                    // 비어있으면
                    requestTopApiData()
                }

            })
        }
    }

    @InternalCoroutinesApi
    private fun requestDiscoverApiData() {


        databaseViewModel.getAllMovie.observe(viewLifecycleOwner, { response ->


            when (response) {

                is NetworkResult.Success -> {
                    Log.d(TAG, "requestApiData: success")
                    discover_recycler?.hideShimmer()
                    response.data?.let {

                        listDiscoverAdapter?.setData(((it)))

                    }
                }
                is NetworkResult.Error -> {

                    discover_recycler?.showShimmer()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    bannerFloat()

                }

                is NetworkResult.Loading -> {

                    discover_recycler?.showShimmer()
                }

            }
        })
    }

    @InternalCoroutinesApi
    private fun requestTopApiData() {


        databaseViewModel.getAllMovie.observe(viewLifecycleOwner, { response ->

//           // response 상태에 따라 로직 나누기
            when (response) {

                is NetworkResult.Success -> {
                    Log.d(TAG, "requestApiData: success")
                    top_recycler?.hideShimmer()
                    response.data?.let {
                        listTopAdapter?.setData(((it)))
                    }
                }
                is NetworkResult.Error -> {

                    top_recycler?.hideShimmer()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    bannerFloat()
                }

                is NetworkResult.Loading -> {

                    top_recycler?.showShimmer()
                }

            }
        })
    }

    @InternalCoroutinesApi
    private fun requestRecentApiData() {


        databaseViewModel.getAllMovie.observe(viewLifecycleOwner, { response ->

//           // response 상태에 따라 로직 나누기
            when (response) {

                is NetworkResult.Success -> {
                    recent_recycler?.hideShimmer()
                    response.data?.let {
                        Log.d(TAG, "requestRecentApiData: ")
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        listRecentAdapter?.setData(((it)))
                    }
                }
                is NetworkResult.Error -> {
                    recent_recycler?.hideShimmer()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    bannerFloat()
                }

                is NetworkResult.Loading ->
                    recent_recycler?.showShimmer()

            }
        })
    }

    @InternalCoroutinesApi
    private fun requestPopularApiData() {


        databaseViewModel.getAllMovie.observe(viewLifecycleOwner, { response ->

            when (response) {

                is NetworkResult.Success -> {
                    popular_recycler?.hideShimmer()
                    response.data?.let {

                        Log.d(TAG, "requestPopularApiData: ")

                        listPopularAdapter?.setData(((it)))
                    }
                }
                is NetworkResult.Error -> {
                    popular_recycler?.hideShimmer()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    bannerFloat()
                }

                is NetworkResult.Loading -> popular_recycler?.showShimmer()

            }
        })
    }

    @InternalCoroutinesApi
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




}



