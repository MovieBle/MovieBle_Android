package com.example.movie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.databinding.RecentRowLayoutBinding

class RecentAdapter : RecyclerView.Adapter<RecentAdapter.RecentHolder>() {

    class RecentHolder(private val binding: RecentRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(){

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

        holder.bind()

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}