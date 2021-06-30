package com.example.movie.ui.user

import androidx.fragment.app.viewModels
import com.example.movie.R
import com.example.movie.base.UtilityBase
import com.example.movie.databinding.FragmentUserMovieInformationBinding
import com.example.movie.viewmodels.UserViewModel

class UserMovieInformationFragment :
    UtilityBase.BaseFragment<FragmentUserMovieInformationBinding>(R.layout.fragment_user_movie_information) {
    private val userViewModel: UserViewModel by viewModels()

    override fun FragmentUserMovieInformationBinding.onCreateView() {
        binding.fragment = this@UserMovieInformationFragment
        binding.viewModel = userViewModel
        binding.lifecycleOwner = this@UserMovieInformationFragment
    }


    override fun FragmentUserMovieInformationBinding.onViewCreated() {

    }
}