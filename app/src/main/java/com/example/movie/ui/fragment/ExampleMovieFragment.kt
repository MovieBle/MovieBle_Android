package com.example.movie.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.adapters.MovieListAdapter
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.FragmentExampleMovieBinding
import com.example.movie.untils.App
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.viewmodels.DatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi

class ExampleMovieFragment : Fragment() {
  

    // 저장 -> movieSave true
    // 삭제 -> movieSave false
    
    // liveData로 비동기처리하면서 movieSave 상태에 따라 snackBar에 뜨는 text변경
    private var _binding: FragmentExampleMovieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ExampleMovieFragmentArgs>()

    private val databaseViewModel: DatabaseViewModel by viewModels()
    var mLikeAdapter: MovieListAdapter? = null
    private lateinit var contents: TextView
    private lateinit var examText: TextView

    private var movieSave = false
    private var movieId: Int = 0


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExampleMovieBinding.inflate(inflater, container, false)




        imgLoad()


        contents = binding.examContents
        examText = binding.examTitle


        contents.text = args.currentItem.overview
        examText.text = args.currentItem.title





        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exam, menu)
        val menuItem = menu.findItem(R.id.menu_action_star)
        checkSaveMovie(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_action_star && !movieSave) {
            insertMovie(item)

        } else if (item.itemId == R.id.menu_action_star && movieSave) {
            deleteMovie(item)
        } else {
            return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSaveMovie(menuItem: MenuItem) {

        // ROOM 과 jSON 객체로 받아온 id 값을 비교
        databaseViewModel.getAllData.observe(this, { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.result.id == args.currentItem.id) {

                        changeMenuItemColor(menuItem, R.color.yellow)
                        movieId = savedRecipe.id
                        movieSave = true
                    } else {
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    //데이터 저장
    private fun insertMovie(item: MenuItem) {
        val movieData = MovieEntity(
            movieId,
            args.currentItem

        )

        movieSave = true
        databaseViewModel.insertData(movieData)

        mLikeAdapter?.setLikeData(listOf(movieData))



        changeMenuItemColor(item, R.color.yellow)


        showSnackBar("포스트가 저장되었습니다.")
        Log.d(
            TAG,
            "insertMovie: ${BASE_IMG_URL + args.currentItem.poster_path}, ${args.currentItem.title}"
        )

    }

    private fun deleteMovie(item: MenuItem) {
        val movieData = MovieEntity(

            movieId,
            args.currentItem
        )

        databaseViewModel.deleteData(movieData)

        movieSave = false
        mLikeAdapter?.setLikeData(listOf(movieData))
        changeMenuItemColor(item, R.color   .white)
        showSnackBar("포스트가 지워졌습니다.")
        Log.d(TAG, "deleteMovie: $movieData")
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.exampleLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()

    }

    private fun imgLoad() {
        Glide.with(App.instance)
            .load(BASE_IMG_URL + args.currentItem.poster_path)
            .centerCrop()
            .placeholder(R.drawable.test_post)
            .into(binding.examImg)
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

}