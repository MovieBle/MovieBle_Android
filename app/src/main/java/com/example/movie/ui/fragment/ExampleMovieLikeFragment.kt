package com.example.movie.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movie.databinding.FragmentExampleLikeMovieBinding
import com.example.movie.viewmodels.DatabaseViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@Suppress("UNCHECKED_CAST")
class ExampleMovieLikeFragment : Fragment() {

    private var _binding: FragmentExampleLikeMovieBinding? = null
     val binding get() = _binding!!


    private lateinit var contents: TextView
    private lateinit var examText: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseText: TextView

    var movieId: Int = 0


    private var movieSave = true

    @InternalCoroutinesApi
    private lateinit var databaseViewModel: DatabaseViewModel

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        databaseViewModel = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)



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







        setHasOptionsMenu(true)

        return binding.root
    }

//    @InternalCoroutinesApi
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_exam, menu)
//        val menuItem = menu.findItem(R.id.menu_action_star)
//        changeMenuItemColor(menuItem, R.color.yellow)
//        checkSaveMovie(menuItem)
//    }
//
//    @InternalCoroutinesApi
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        if (item.itemId == R.id.menu_action_star && movieSave) {
//            deleteMovie(item)
//        } else {
//            return super.onOptionsItemSelected(item)
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    @InternalCoroutinesApi
//    private fun deleteMovie(item: MenuItem) {
//
//
//        val movieData = args.likeMovie?.result.let {
//            args.likeMovie?.result?.let { it1 ->
//                MovieLikeEntity(
//                    movieId--,
//                    it1
//                )
//            }
//        }
//

//        movieData.let {
//            movieData?.let { it1 -> databaseViewModel.deleteLikeMovie(it1) }
//            viewModel.showSnackBar("포스트가 지워졌습니다.", requireView())
//
//            likeAdapter.setLikeData(listOf(movieData) as List<MovieLikeEntity>)
//            movieSave = false
//
//            changeMenuItemColor(item, R.color.white)
//        }
//
//        findNavController().navigate(R.id.action_exampleLikeMovieFragment_to_movieListFragment)
//
//
//        Log.d(Constants.TAG, "deleteMovie: $movieData")
//    }


//    @InternalCoroutinesApi
//    private fun checkSaveMovie(menuItem: MenuItem) {
//
//        // ROOM 과 jSON 객체로 받아온 id 값을 비교
//        databaseViewModel.getAllLikeData.observe(this, { favoritesEntity ->
//            try {
//                for (savedMovie in favoritesEntity) {
//                    if (savedMovie.result.id == args.likeMovie?.result?.id) {
//                        changeMenuItemColor(menuItem, R.color.yellow)
//                        movieId = savedMovie.id
//                        movieSave = true
//                    } else {
//                        changeMenuItemColor(menuItem, R.color.white)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d("DetailsActivity", e.message.toString())
//            }
//        })
//    }
//
//    private fun changeMenuItemColor(item: MenuItem, color: Int) {
//        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
//    }



}