package com.example.movie.adapters

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.models.Result
import com.example.movie.databinding.RowLoadingBinding
import com.example.movie.databinding.SearchMovieRowBinding
import com.example.movie.models.Movie
import com.example.movie.ui.fragment.SearchPostFragmentDirections
import com.example.movie.untils.App
import com.example.movie.untils.Constants
import com.example.movie.untils.MovieDiffUtil
import java.util.*
import kotlin.collections.ArrayList

class SearchViewAdapter(
    private var movieList: MutableList<Result>,
    val context: Context

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var listFilter: MovieListFilter? = null
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun getItemViewType(position: Int): Int {
        return when (movieList[position].title) {
            " " -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    class SearchViewHolder(
        val binding: SearchMovieRowBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Result) {


            val url = Constants.BASE_IMG_URL + item.poster_path

            binding.searchText.text = item.title


            Glide.with(App.instance)
                .load(url)
                .centerCrop()
                .into(binding.searchPostImg)


        }
    }

    class LoadingViewHolder(private val binding: RowLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchMovieRowBinding.inflate(layoutInflater, parent, false)
                SearchViewHolder(binding)
            }

            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchViewHolder) {
            holder.bind(movieList[position])
            holder.binding.searchItemPg.setOnClickListener() {

                Log.d(Constants.TAG, "RecentHolder - bind() called")
                val action =
                    SearchPostFragmentDirections.actionSearchPostFragmentToExampleMovieFragment4(
                        movieList[position]


                    )

                it.findNavController().navigate(action)

            }
        }


    }


    override fun getItemCount(): Int {
        return movieList.size
    }


    override fun getFilter(): Filter {
        if (listFilter == null) listFilter = MovieListFilter(this, movieList)

        return listFilter as MovieListFilter

    }


    // recyclerView Filter
    private class MovieListFilter(
        private val listAdapter: SearchViewAdapter,
            private val originalData: List<Result>
    ) : Filter() {
        private val filteredData: MutableList<Result>
        override fun performFiltering(constraint: CharSequence): FilterResults {
            filteredData.clear()
            val results = FilterResults()

            Log.d("performFiltering: ", constraint.toString())


            if (TextUtils.isEmpty(constraint.toString())) {
                filteredData.addAll(originalData)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (user in originalData) {
                    if (user.title.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredData.add(user)
                    }
                }
            }
            results.values = filteredData
            results.count = filteredData.size
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {

            listAdapter.setFilterData(filterResults.values as List<Result>)
            listAdapter.notifyDataSetChanged()
        }

        init {
            filteredData = ArrayList()
        }
    }


    fun setFilterData(movieData: List<Result>) {
        this.movieList = movieData as MutableList<Result>
        notifyDataSetChanged()

    }





    fun setList(notice: Movie) {
        movieList.addAll(notice.results)
        movieList.add(
            Result(
                false, "", emptyList<Int>(), 0, "", "", ""
            , 0.0,"","","",false,0.0,
                0
            )
        ) // progress bar 넣을 자리
    }

    fun deleteLoading(){
        movieList.removeAt(movieList.lastIndex)
    }
    fun setData(movieData: Movie) {

        val movieDiffUtil = MovieDiffUtil(movieList, movieData.results)
        val diffUtilResult = movieDiffUtil.let { DiffUtil.calculateDiff(it) }
        movieList = movieData.results as MutableList<Result>
        diffUtilResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}