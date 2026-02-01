package com.MSInnovation.nestify.views.login

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.MSInnovation.nestify.base.BaseFragment
import com.MSInnovation.nestify.core.DataState
import com.MSInnovation.nestify.core.extract
import com.MSInnovation.nestify.core.isEmpty
import com.MSInnovation.nestify.data.models.UserLogIn
import com.MSInnovation.nestify.databinding.FragmentLoginBinding
import com.MSInnovation.nestify.views.dashboard.AppDashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LogInViewModel by viewModels()

    override fun setListener() {
        with(binding) {
            btnLogin.setOnClickListener {
                etEmail.isEmpty()
                etPassword.isEmpty()
                if (!etEmail.isEmpty() && !etPassword.isEmpty()) {
                    var user = UserLogIn(etEmail.extract(), etPassword.extract())

                    viewModel.userLogin(user)
                    loadingDialog?.show()
                }
            }

        }
    }

    override fun allObserver() {
        logInResponse()

    }
    private fun logInResponse() {
        viewModel.logInResponse.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Error -> {
                    loadingDialog?.dismiss()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> {
                    loadingDialog?.show()
                }
                is DataState.Success -> {
                    loadingDialog?.dismiss()
                    val intent = Intent(requireContext(), AppDashboardActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }
}