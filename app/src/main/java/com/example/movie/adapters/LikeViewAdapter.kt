package com.example.movie.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.MovieListRowLayoutBinding
import com.example.movie.ui.fragment.ExampleMovieFragment
import com.example.movie.untils.App
import com.example.movie.untils.Constants
import kotlinx.coroutines.InternalCoroutinesApi

@Suppress("CAST_NEVER_SUCCEEDS")
@InternalCoroutinesApi
class LikeViewAdapter(
        val context: Context,
        var fragment: Fragment)
    : RecyclerView.Adapter<LikeViewAdapter.LikeViewHolder>() {

    var likeList = emptyList<MovieEntity>()


    interface ItemClickListener {
       fun onClickEvent(fragment : ExampleMovieFragment,position : Int, movie:MovieEntity)

    }
    private lateinit var itemClickListner: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }


    inner class LikeViewHolder(val binding: MovieListRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: MovieEntity) {

            val url = Constants.BASE_IMG_URL + item.result.poster_path

            binding.pagerItemText.text = item.result.title


            Log.d(Constants.TAG, "bind: $url")

            Glide.with(App.instance)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.test_post)
                    .into(binding.pagerItemImage)


            itemView.setOnClickListener {

                itemClickListner.onClickEvent(ExampleMovieFragment(),position,item)
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

    fun setLikeData(likeList: List<MovieEntity>) {
        this.likeList = likeList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        val currentItem = likeList[position]
        holder.bind(currentItem)



        with(holder) {


        }


    }

    override fun getItemCount(): Int {
        return likeList.size
    }

}