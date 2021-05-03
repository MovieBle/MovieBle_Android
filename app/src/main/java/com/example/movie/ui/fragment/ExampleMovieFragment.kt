package com.example.movie.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.databinding.FragmentExampleMovieBinding
import com.example.movie.untils.App
import com.example.movie.untils.Constants.Companion.BASE_IMG_URL
import com.example.movie.untils.Constants.Companion.TAG

class ExampleMovieFragment : Fragment() {


    private var _binding: FragmentExampleMovieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ExampleMovieFragmentArgs>()
    var i = 0
    private lateinit var like_img: ImageView

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentExampleMovieBinding.inflate(inflater, container, false)

        Log.d(TAG, "onCreateView: ${BASE_IMG_URL + args.currentItem.poster_path}")


        Glide.with(App.instance)
            .load(BASE_IMG_URL + args.currentItem.poster_path)
            .centerCrop()
            .placeholder(R.drawable.test_post)
            .into(binding.examImg)

        like_img = binding.examLikeImg
        binding.examContents.text = args.currentItem.overview
        binding.examTitle.text = args.currentItem.original_title



        like_img.setOnClickListener {


            like_img.setBackgroundColor(R.color.black)

            insertMovie()


        }
          
        return binding.root
    }

    private fun insertMovie() {


    }


}