package com.example.movie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.untils.App
import com.example.movie.R
import com.example.movie.data.Result
import com.example.movie.databinding.RecentRowLayoutBinding


@Suppress("DEPRECATION")
class RecentAdapter() : RecyclerView.Adapter<RecentAdapter.RecentHolder>() {


    companion object {
        private val recentMovieList = emptyList<Result>()

    }

    class RecentHolder(private val binding: RecentRowLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.result = result
            // 데이터 내부에 변경 사항이있을 때마다 레이아웃을 업데이트함
            // 바인딩을 즉시 실행할 때 -> 업데이트가 될 때 ??
            binding.executePendingBindings()

            var url = "https://image.tmdb.org/t/p/w500" + recentMovieList[position].poster_path

            Glide.with(App.instance)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.test_post)
                    .into(binding.recentPostImage)
        }


        companion object {
            fun from(parent: ViewGroup): RecentHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecentRowLayoutBinding.inflate(layoutInflater, parent, false)
                return RecentHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentHolder {

        return RecentHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecentHolder, position: Int) {

        val currentRecipe = recentMovieList[position]
        holder.bind(currentRecipe)


    }

    override fun getItemCount(): Int {
        return recentMovieList.size
    }
}