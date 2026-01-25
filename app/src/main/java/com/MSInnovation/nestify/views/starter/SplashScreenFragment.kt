package com.MSInnovation.nestify.views.starter

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.MSInnovation.nestify.R
import com.MSInnovation.nestify.base.BaseFragment
import com.MSInnovation.nestify.databinding.FragmentSplashScreenBinding
import com.MSInnovation.nestify.views.dashboard.AppDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment :
    BaseFragment<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    @Inject
    lateinit var qAuth: FirebaseAuth

    override fun setListener() {
        val fadeIn =
            AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        binding.splashLogo.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            checkInternetAndProceed()
        }, 1000)
    }

    private fun checkInternetAndProceed() {
        if (isInternetAvailable(requireContext())) {

            val currentUser = qAuth.currentUser
            if (currentUser != null) {
                startActivity(
                    Intent(requireContext(), AppDashboardActivity::class.java)
                )
                requireActivity().finish()
            } else {
                findNavController().navigate(
                    R.id.action_splashScreenFragment_to_startFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreenFragment, true)
                        .build()
                )
            }

        } else {
            showNoInternetUI()
        }
    }

    private fun showNoInternetUI() {
        with(binding) {
            splashLogo.visibility = View.GONE
            lottieNoInternet.visibility = View.VISIBLE
            textNoInternet.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE

            retryButton.setOnClickListener {
                checkInternetAndProceed()
            }
        }
    }

    override fun allObserver() {

    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
