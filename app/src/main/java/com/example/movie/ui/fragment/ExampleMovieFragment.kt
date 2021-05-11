package com.example.movie.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.Result
import com.example.movie.data.database.MovieEntity
import com.example.movie.databinding.FragmentExampleMovieBinding
import com.example.movie.untils.App
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.TAG
import com.example.movie.viewmodels.DatabaseViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi

class ExampleMovieFragment : Fragment() {
    var i = 0

    private var _binding: FragmentExampleMovieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ExampleMovieFragmentArgs>()

    private val databaseViewModel: DatabaseViewModel by viewModels()
    private lateinit var like_img: ImageButton

    private lateinit var contents: TextView
    private lateinit var examText: TextView


    var likeList = mutableListOf<Result>()

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExampleMovieBinding.inflate(inflater, container, false)

        Log.d(TAG, "onCreateView: ${BASE_IMG_URL + args.currentItem.poster_path}")


        Glide.with(App.instance)
                .load(BASE_IMG_URL + args.currentItem.poster_path)
                .centerCrop()
                .placeholder(R.drawable.test_post)
                .into(binding.examImg)


        contents = binding.examContents
        examText = binding.examTitle
        like_img = binding.examLikeImg

        like_img.setBackgroundColor(R.color.black)

        contents.text = args.currentItem.overview
        examText.text = args.currentItem.title


        Log.d(TAG, "onCreateView: $examText")


        like_img.setOnClickListener {

            if (i == 0) {
                like_img.setBackgroundColor(R.color.red)

                insertMovie()
                i++
            } else {
                like_img.setBackgroundColor(R.color.black)
                deleteMovie()
                i--
            }

        }

        return binding.root
    }


    private fun insertMovie() {
        val movieData = MovieEntity(

                BASE_IMG_URL + args.currentItem.poster_path,

                args.currentItem.title

        )
        databaseViewModel.insertData(movieData)


        Toast.makeText(requireContext(), "포스트가 추가됩니다.", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "insertMovie: ${BASE_IMG_URL + args.currentItem.poster_path}, ${args.currentItem.title}")

    }

    private fun deleteMovie() {
        val movieData = MovieEntity(



                BASE_IMG_URL + args.currentItem.poster_path,

                args.currentItem.title
        )

        databaseViewModel.deleteData(movieData)

        Toast.makeText(requireContext(), "포스트가 지워집니다.", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "deleteMovie: $movieData")
    }


}