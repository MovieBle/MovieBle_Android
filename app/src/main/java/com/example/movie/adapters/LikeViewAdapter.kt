package com.example.movie.adapters

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.Result
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.MovieListRowLayoutBinding
import com.example.movie.ui.fragment.ExampleMovieFragment
import com.example.movie.untils.App
import com.example.movie.untils.Constants
import com.example.movie.untils.Constants.Companion.TAG
import kotlinx.coroutines.InternalCoroutinesApi

class LikeViewAdapter : RecyclerView.Adapter<LikeViewAdapter.LikeViewHolder>() {
    var likeList = emptyList<MovieEntity>()

    class LikeViewHolder(val binding: MovieListRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {



        fun bind(item: MovieEntity) {

            val url = Constants.BASE_IMG_URL + item.result.poster_path

            binding.pagerItemText.text = item.result.title


            Log.d(Constants.TAG, "bind: $url")
            Glide.with(App.instance)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.test_post)
                    .into(binding.pagerItemImage)


        }

        companion object {
            fun from(parent: ViewGroup): LikeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListRowLayoutBinding.inflate(layoutInflater, parent, false)
                return LikeViewHolder(
                        binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        return LikeViewHolder.from(
                parent
        )
    }

    fun setLikeData(likeList: List<MovieEntity>) {
        this.likeList = likeList
        notifyDataSetChanged()
    }

    @InternalCoroutinesApi
    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        val currentItem = likeList[position]
        holder.bind(currentItem)

        with(holder) {
            this.binding.pagerItemPg.setOnClickListener() {
                val myFragment: Fragment = ExampleMovieFragment()
//                newInstance(currentItem)



                Log.d(TAG, "onBindViewHolder: ")

            }


                //
            }
        }



    @InternalCoroutinesApi
    fun newInstance(data: MovieEntity) = ExampleMovieFragment().apply {
        arguments = Bundle().apply {
            putString(KEY, data.toString())
        }

    }

    override fun getItemCount(): Int {
        return likeList.size
    }
}