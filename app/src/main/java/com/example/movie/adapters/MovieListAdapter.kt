package com.example.movie.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.models.Result
import com.example.movie.databinding.MovieListRowLayoutBinding
import com.example.movie.models.Movie

import com.example.movie.ui.fragment.MovieListFragmentDirections
import com.example.movie.ui.fragment.addMovie.AddDiscoverMovieFragmentDirections
import com.example.movie.ui.fragment.addMovie.AddMovieRecentFragmentDirections
import com.example.movie.ui.fragment.addMovie.AddPopularMovieFragmentDirections
import com.example.movie.ui.fragment.addMovie.AddTopMovieFragmentDirections
import com.example.movie.untils.App
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.untils.MovieCase
import com.example.movie.untils.MovieDiffUtil


@Suppress("CAST_NEVER_SUCCEEDS", "UNREACHABLE_CODE")
class MovieListAdapter(
    private var movieList: List<Result> = emptyList(),
    private val movieCase: MovieCase
) :
    RecyclerView.Adapter<MovieListAdapter.MovieHolder>() {


    class MovieHolder(
        val binding: MovieListRowLayoutBinding

    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Result) {

            val url = BASE_IMG_URL + item.poster_path

            binding.pagerItemText.text = item.title


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
        return movieList.size
    }


    fun setData(movieData: Movie) {

        val movieDiffUtil = MovieDiffUtil(movieList, movieData.results)
        val diffUtilResult = movieDiffUtil.let { DiffUtil.calculateDiff(it) }
        movieList = movieData.results
        diffUtilResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
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
                        AddMovieRecentFragmentDirections.actionAddMovieRecentFragmentToExampleMovieFragment(
                            movieList[position]


                        )

                    it.findNavController().navigate(action)

                }
            }
            MovieCase.MOVIE_TOP -> {
                holder.bind(element)
                holder.binding.pagerItemPg.setOnClickListener {
                    val action =
                        AddTopMovieFragmentDirections.actionAddTopMovieFragmentToExampleMovieFragment(
                            movieList[position]
                        )

                    it.findNavController().navigate(action)
                }
            }
            MovieCase.MOVIE_POPULAR -> {
                holder .bind(element)
                holder.binding.pagerItemPg.setOnClickListener {
                    val action =
                        AddPopularMovieFragmentDirections.actionAddPopularMovieFragmentToExampleMovieFragment(
                            movieList[position]
                        )

                    it.findNavController().navigate(action)
                }

            }
            MovieCase.MOVIE_DISCOVER -> {
                holder.bind(element)
                holder.binding.pagerItemPg.setOnClickListener {
                    val action =
                        AddDiscoverMovieFragmentDirections.actionAddDiscoverMovieFragmentToExampleMovieFragment(
                            movieList[position]
                        )

                    it.findNavController().navigate(action)
                }
            }


        }
    }
}

