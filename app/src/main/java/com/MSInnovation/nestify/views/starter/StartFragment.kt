package com.MSInnovation.nestify.views.starter


import androidx.navigation.fragment.findNavController
import com.MSInnovation.nestify.R
import com.MSInnovation.nestify.base.BaseFragment
import com.MSInnovation.nestify.databinding.FragmentStartBinding


class StartFragment : BaseFragment<FragmentStartBinding>(FragmentStartBinding::inflate) {
    override fun setListener() {
        with(binding){
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_loginFragment)
            }
            btnRegister.setOnClickListener{
                findNavController().navigate(R.id.action_startFragment_to_registerFragment)
            }
        }
    }
    override fun allObserver() {

    }
}