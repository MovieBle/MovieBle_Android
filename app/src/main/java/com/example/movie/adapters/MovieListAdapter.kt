package com.example.movie.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.untils.App
import com.example.movie.R
import com.example.movie.data.Movie
import com.example.movie.data.Result
import com.example.movie.databinding.MovieListRowLayoutBinding
import com.example.movie.ui.fragment.MovieListFragmentDirections
import com.example.movie.ui.fragment.SearchPostFragmentDirections
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.viewmodels.BaseViewModel


@Suppress("DEPRECATION")
class MovieListAdapter(val movieList: List<Result>, val context: Context) :
    RecyclerView.Adapter<MovieListAdapter.RecentHolder>() {


    class RecentHolder(
        val binding: MovieListRowLayoutBinding,

        ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {

            var url = BASE_IMG_URL + result.poster_path

            binding.pagerItemText.text = result.original_title


            Log.d(TAG, "bind: $url")
            Glide.with(App.instance)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.test_post)
                .into(binding.pagerItemImage)
        }


        companion object {
            fun from(parent: ViewGroup): RecentHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListRowLayoutBinding.inflate(layoutInflater, parent, false)
                return RecentHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        return RecentHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecentHolder, position: Int) {

        Log.d(TAG, "onBindViewHolder: ")
        holder.bind(movieList[position])

        with(holder) {
            binding.pagerItemBg.setOnClickListener() {
                val action =
                    MovieListFragmentDirections.actionMovieListFragmentToExampleMovieFragment(
                        movieList[position]
                    )
                holder.itemView.findNavController().navigate(action)



        }

    }
        }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ")
        return movieList.size
    }
}