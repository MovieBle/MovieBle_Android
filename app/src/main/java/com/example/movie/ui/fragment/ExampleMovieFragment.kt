package com.example.movie.ui.fragment

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.movie.R
import com.example.movie.base.UtilityBase
import com.example.movie.data.database.entities.movie.MovieLikeEntity
import com.example.movie.databinding.FragmentExampleMovieBinding
import com.example.movie.viewmodels.DatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi


@AndroidEntryPoint
class ExampleMovieFragment :
    UtilityBase.BaseFragment<FragmentExampleMovieBinding>(R.layout.fragment_example_movie) {

    var movieId: Int = 0
    var movieSave = false
    private val args by navArgs<ExampleMovieFragmentArgs>()

    @InternalCoroutinesApi
    private val databaseViewModel: DatabaseViewModel by viewModels()
    override fun FragmentExampleMovieBinding.onCreateView() {
        setHasOptionsMenu(true)
    }

    override fun FragmentExampleMovieBinding.onViewCreated() {
        binding.movie = args.movie
    }

    @InternalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exam, menu)
        val menuItem = menu.findItem(R.id.menu_action_star)
        changeMenuItemColor(menuItem, R.color.white)
        checkSaveMovie(menuItem)

    }


    @InternalCoroutinesApi
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


    @InternalCoroutinesApi
    private fun checkSaveMovie(menuItem: MenuItem) {

        // ROOM 과 jSON 객체로 받아온 id 값을 비교
        databaseViewModel.getMovie.observe(this, {
            try {
                for (savedMovie in it) {
                    if (savedMovie.id == args.movie.budget) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        movieId = savedMovie.id
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
    @InternalCoroutinesApi
    private fun insertMovie(item: MenuItem) {
        val movieData = MovieLikeEntity(
            movieId++,
            args.movie.adult,
            args.movie.backdrop,
            args.movie.budget,
            args.movie.cast,
            args.movie.director,
            args.movie.genres,
            args.movie.overview,
            args.movie.poster,
            args.movie.production,
            args.movie.release,
            args.movie.runtime,
            args.movie.status,
            args.movie.title,
            args.movie.video,
            args.movie.voteAver,
            args.movie.voteCount

        )
        databaseViewModel.insertLikeMovie(movieData)
        showSnackBar("포스트가 저장되었습니다.", requireView())
        movieSave = true
        changeMenuItemColor(item, R.color.yellow)
    }


    @InternalCoroutinesApi
    private fun deleteMovie(item: MenuItem) {
        val movieData = MovieLikeEntity(
            movieId--,
            args.movie.adult,
            args.movie.backdrop,
            args.movie.budget,
            args.movie.cast,
            args.movie.director,
            args.movie.genres,
            args.movie.overview,
            args.movie.poster,
            args.movie.production,
            args.movie.release,
            args.movie.runtime,
            args.movie.status,
            args.movie.title,
            args.movie.video,
            args.movie.voteAver,
            args.movie.voteCount
        )

        movieData.let {
            databaseViewModel.deleteLikeMovie(it)

            movieSave = false
            changeMenuItemColor(item, R.color.white)
            showSnackBar("포스트가 지워졌습니다.", requireView())

        }


    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    private fun showSnackBar(message: String, view: View) {
        Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

}



