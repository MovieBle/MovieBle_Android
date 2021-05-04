package com.example.movie.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.MovieCase
import com.example.movie.untils.App
import com.example.movie.R
import com.example.movie.data.Result
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.MovieListRowLayoutBinding
import com.example.movie.databinding.SearchMovieRowBinding
import com.example.movie.ui.fragment.MovieListFragmentDirections
import com.example.movie.ui.fragment.SearchPostFragment
import com.example.movie.ui.fragment.SearchPostFragmentDirections
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL

import com.example.movie.untils.Constants.Companion.TAG


@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
class MovieListAdapter(
    private var movieList: List<Result>,
    private val movieCase: MovieCase,
    val context: Context
) :
    RecyclerView.Adapter<MovieListAdapter.BaseViewHolder<*>>() {


    // var likeList = emptyList<MovieEntity>()

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Result)
    }


    inner class RecentHolder(
        val binding: MovieListRowLayoutBinding

    ) :
        BaseViewHolder<List<Result>>(binding.root) {


        override fun bind(item: Result) {

            var url = BASE_IMG_URL + item.poster_path

            binding.pagerItemText.text = item.original_title


            Log.d(TAG, "bind: $url")
            Glide.with(App.instance)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.test_post)
                .into(binding.pagerItemImage)


        }
    }

    inner class SearchViewHolder(
        val binding: SearchMovieRowBinding
    ) :
        BaseViewHolder<List<Result>>(binding.root) {


        override fun bind(item: Result) {


            var url = BASE_IMG_URL + item.poster_path

            binding.searchText.text = item.original_title


            Log.d(TAG, "bind: $url")
            Glide.with(App.instance)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.test_post)
                .into(binding.searchPostImg)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        Log.d(TAG, "onCreateViewHolder: ")

        return when (movieCase) {
            MovieCase.SEARCH_VIEW -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchMovieRowBinding.inflate(layoutInflater, parent, false)
                SearchViewHolder(binding)
            }

            MovieCase.LIST_VIEW -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListRowLayoutBinding.inflate(layoutInflater, parent, false)
                RecentHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {


        when (holder) {
            is RecentHolder -> {
                val element = movieList[position]


                val recentViewHolder: RecentHolder = holder
                recentViewHolder.bind(element)

                holder.bind(element)


                recentViewHolder.binding.pagerItemBg.setOnClickListener() {

                    Log.d(TAG, "RecentHolder - bind() called")
                    val action =
                        MovieListFragmentDirections.actionMovieListFragmentToExampleMovieFragment(
                            movieList[position]
                        )

                    it.findNavController().navigate(action)

                }
            }

            is SearchViewHolder -> {

                val element = movieList[position]

                val searchViewHolder: SearchViewHolder = holder
                searchViewHolder.bind(element)


                searchViewHolder.binding.searchItemPg.setOnClickListener() {

                    Log.d(TAG, "SearchViewHolder - bind() called")
                    val action =
                        SearchPostFragmentDirections.actionSearchPostFragmentToExampleMovieFragment4(
                            movieList[position]
                        )
                    it.findNavController().navigate(action)


                }

            }


            else -> throw IllegalArgumentException()
        }


    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ")
        return movieList.size
    }


//
//    fun setData(movieData: List<MovieEntity>) {
//        this.likeList = movieData
//        notifyDataSetChanged()
//    }

    fun setData1(movieData: List<Result>) {
        this.movieList = movieData
        notifyDataSetChanged()
    }
}