package com.example.movie.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.Result
import com.example.movie.databinding.MovieListRowLayoutBinding
import com.example.movie.ui.fragment.AddMovieFragmentDirections
import com.example.movie.ui.fragment.MovieListFragmentDirections
import com.example.movie.untils.App
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.untils.MovieCase
import com.example.movie.untils.MovieDiffUtil
import com.example.movie.viewmodels.NetworkViewModel


class MovieListAdapter(

    private val movieCase: MovieCase
) :
    RecyclerView.Adapter<MovieListAdapter.MovieHolder>() {


    private var movieList = mutableListOf<Result>()

    class MovieHolder(
        val binding: MovieListRowLayoutBinding

    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Result) {

            val url = BASE_IMG_URL + item.poster_path

            binding.pagerItemText.text = item.title


            Log.d(TAG, "bind: $url")
            Glide.with(App.instance)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.test_post)
                .into(binding.pagerItemImage)


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieListRowLayoutBinding.inflate(layoutInflater, parent, false)
        return MovieHolder(binding)


    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ")
        return movieList.size
    }


    fun setData(movieData: List<Result>?) {
        if (movieData != null) {
            this.movieList = movieData as MutableList<Result>
        }
        notifyDataSetChanged()
        val movieDiffUtil = movieData?.let { MovieDiffUtil(movieList, it) }
        val diffUtilResult = movieDiffUtil?.let { DiffUtil.calculateDiff(it) }
        if (movieData != null) {
            movieList = movieData as MutableList<Result>
        }
        diffUtilResult?.dispatchUpdatesTo(this)
    }


    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val element = movieList[position]

        when (movieCase) {
            MovieCase.MOVIE_LIST -> {
                holder.bind(element)
                holder.binding.pagerItemPg.setOnClickListener {
                    val action =
                        MovieListFragmentDirections.actionMovieListFragmentToExampleMovieFragment(
                            movieList[position]


                        )

                    it.findNavController().navigate(action)
                }
            }
            MovieCase.MOVIE_RECENT -> {
                holder.bind(element)
                holder.binding.pagerItemPg.setOnClickListener {
                    val action =
                        AddMovieFragmentDirections.actionAddMovieFragmentToExampleMovieFragment(
                            movieList[position]


                        )

                    it.findNavController().navigate(action)

                }
            }


        }
    }
}

