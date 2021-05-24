package com.example.movie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.navigateUp
import com.example.movie.R
import com.example.movie.adapters.LikeViewAdapter
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.FragmentExampleLikeMovieBinding
import com.example.movie.untils.Constants
import com.example.movie.viewmodels.DatabaseViewModel
import com.example.movie.viewmodels.ViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import javax.xml.parsers.DocumentBuilderFactory.newInstance

class ExampleMovieLikeFragment : Fragment() {

    private var _binding: FragmentExampleLikeMovieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ExampleMovieLikeFragmentArgs>()


    private lateinit var contents: TextView
    private lateinit var examText: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseText: TextView

    var movieId: Int = 0


    private var movieSave = true

    @InternalCoroutinesApi
    private val databaseViewModel: DatabaseViewModel by viewModels()
    private val viewModel: ViewModel by viewModels()

    @InternalCoroutinesApi
    private val likeAdapter: LikeViewAdapter by lazy {
        LikeViewAdapter(
            requireContext(),

        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {
        _binding = FragmentExampleLikeMovieBinding.inflate(inflater, container, false)

        contents = binding.likeExamContents
        examText = binding.likeExamTitle
        rating = binding.likeMovieRating
        releaseText = binding.likeReleaseText





        rating.rating = args.likeMovie.result.vote_average.toFloat() / 2
        contents.text = args.likeMovie.result.overview
        examText.text = args.likeMovie.result.title
        releaseText.text = args.likeMovie.result.release_date

        viewModel.imageLoad(binding.likeExamImg, args.likeMovie.result.poster_path)



        setHasOptionsMenu(true)

        return binding.root
    }

    @InternalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exam, menu)
        val menuItem = menu.findItem(R.id.menu_action_star)
        changeMenuItemColor(menuItem, R.color.yellow)
        checkSaveMovie(menuItem)
    }

    @InternalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_action_star && movieSave) {
            deleteMovie(item)
        } else {
            return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    @InternalCoroutinesApi
    private fun deleteMovie(item: MenuItem) {


        val movieData = args.likeMovie.result.let {
            MovieEntity(
                movieId--,
                args.likeMovie.result
            )
        }


        movieData?.let {
            databaseViewModel.deleteData(it)
            viewModel.showSnackBar("포스트가 지워졌습니다.", requireView())

            likeAdapter.setLikeData(listOf(movieData))
            movieSave = false

            changeMenuItemColor(item, R.color.white)
        }

        findNavController().navigate(R.id.action_exampleLikeMovieFragment_to_movieListFragment)


        Log.d(Constants.TAG, "deleteMovie: $movieData")
    }


    @InternalCoroutinesApi
    private fun checkSaveMovie(menuItem: MenuItem) {

        // ROOM 과 jSON 객체로 받아온 id 값을 비교
        databaseViewModel.getAllData.observe(this, { favoritesEntity ->
            try {
                for (savedMovie in favoritesEntity) {
                    if (savedMovie.result.id == args.likeMovie.result?.id) {
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

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }



}