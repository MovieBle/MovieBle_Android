package com.example.movie.adapters

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.MovieCase
import com.example.movie.R
import com.example.movie.data.Result
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.MovieListRowLayoutBinding
import com.example.movie.databinding.SearchMovieRowBinding
import com.example.movie.ui.fragment.MovieListFragmentDirections
import com.example.movie.ui.fragment.SearchPostFragmentDirections
import com.example.movie.untils.App
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.TAG


@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
class MovieListAdapter(
        private var movieList: List<Result>,
        private val movieCase: MovieCase,
        val context: Context
) :
        RecyclerView.Adapter<MovieListAdapter.BaseViewHolder<*>>(), Filterable {

    var searchList: List<Result> = mutableListOf()
    private var listFilter: MovieListFilter? = null
    private var likeList = emptyList<MovieEntity>()
    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Result)
    }


    inner class MovieHolder(
            val binding: MovieListRowLayoutBinding

    ) :
            BaseViewHolder<List<Result>>(binding.root) {


        override fun bind(item: Result) {

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

    inner class SearchViewHolder(
            val binding: SearchMovieRowBinding
    ) :
            BaseViewHolder<List<Result>>(binding.root) {


        override fun bind(item: Result) {


            var url = BASE_IMG_URL + item.poster_path

            binding.searchText.text = item.title


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
            MovieCase.SEARCH_LIST_VIEW -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchMovieRowBinding.inflate(layoutInflater, parent, false)
                SearchViewHolder(binding)
            }

            MovieCase.LIST_VIEW -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListRowLayoutBinding.inflate(layoutInflater, parent, false)
                MovieHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = movieList[position]

        when (holder) {
            is MovieHolder -> {


                val recentViewHolder: MovieHolder = holder
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


    fun setLikeData(movieData: List<MovieEntity>) {
        this.likeList = movieData
        notifyDataSetChanged()
    }
    fun setData(movieData: List<Result>){
    this.movieList = movieData
    notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        if (listFilter == null) listFilter = MovieListFilter(this, movieList)

        return listFilter as MovieListFilter

    }


    private class MovieListFilter(private val listAdapter: MovieListAdapter, private val originalData: List<Result>) : Filter() {

        // List 와 MutableList의 차이점은?
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
                    // set condition for filter here
                    if (user.title.toLowerCase().contains(filterPattern)) {
                        filteredData.add(user)
                    }
                }
            }
            results.values = filteredData
            results.count = filteredData.size
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {

            //listAdapter.getData().clear();
            listAdapter.setData(filterResults.values as List<Result>)
            listAdapter.notifyDataSetChanged()
        }

        init {
            filteredData = ArrayList()
        }
    }

}