package com.example.movie.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.database.entities.MovieLikeEntity
import com.example.movie.databinding.MovieListRowLayoutBinding
import com.example.movie.ui.fragment.ExampleMovieFragment
import com.example.movie.ui.fragment.MovieListFragmentDirections
import com.example.movie.untils.App
import com.example.movie.untils.Constants
import com.example.movie.untils.Constants.Companion.TAG
import kotlinx.coroutines.InternalCoroutinesApi

@Suppress("CAST_NEVER_SUCCEEDS")
@InternalCoroutinesApi
class LikeViewAdapter(
    val context: Context,

) : RecyclerView.Adapter<LikeViewAdapter.LikeViewHolder>() {

    var likeList = emptyList<MovieLikeEntity>()





    inner class LikeViewHolder(val binding: MovieListRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: MovieLikeEntity) {

            val url = Constants.BASE_IMG_URL + item.result.poster_path

            binding.pagerItemText.text = item.result.title


            Log.d(Constants.TAG, "bind: $url")

            Glide.with(App.instance)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.test_post)
                .into(binding.pagerItemImage)


            binding.pagerItemPg.setOnClickListener {

                val action =
                    MovieListFragmentDirections.actionMovieListFragmentToExampleLikeMovieFragment(
                        likeList[adapterPosition]


                    )

                it.findNavController().navigate(action)
            }


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieListRowLayoutBinding.inflate(layoutInflater, parent, false)
        return LikeViewHolder(
            binding
        )
    }

    fun setLikeData(likeList: List<MovieLikeEntity>) {
        this.likeList = likeList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        val currentItem = likeList[position]
        holder.bind(currentItem)


    }

    override fun getItemCount(): Int {
        return likeList.size
    }

}