package com.example.movie.ui.user

import androidx.navigation.fragment.findNavController
import com.example.movie.R
import com.example.movie.base.UtilityBase
import com.example.movie.databinding.FragmentUserInformationBinding

class UserInformationFragment :
    UtilityBase.BaseFragment<FragmentUserInformationBinding>(R.layout.fragment_user_information) {


    override fun FragmentUserInformationBinding.onCreateView() {
        binding.activity = this@UserInformationFragment

    }

    override fun FragmentUserInformationBinding.onViewCreated() {
    }

    fun checkNameBtn() {

        findNavController().navigate(R.id.action_userInformationFragment_to_UserMovieInformationFragment)
    }
}