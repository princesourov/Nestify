package com.MSInnovation.nestify.views.dashboard.home

import android.animation.ValueAnimator
import androidx.fragment.app.viewModels
import com.MSInnovation.nestify.base.BaseFragment
import com.MSInnovation.nestify.core.DataState
import android.view.View
import com.MSInnovation.nestify.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: DashboardViewModel by viewModels()
    private var isDashboardVisible = false
    private val autoCollapseDelay = 5_000L
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private var autoCollapseRunnable: Runnable? = null


    override fun setListener() {

        binding.cardview.setOnClickListener {

            autoCollapseRunnable?.let {
                handler.removeCallbacks(it)
            }
            if (isDashboardVisible) {
                collapseView(binding.dashboard)
                expandView(binding.tvTFDashboard)
                isDashboardVisible = false
            } else {
                collapseView(binding.tvTFDashboard)
                expandView(binding.dashboard)
                isDashboardVisible = true

                autoCollapseRunnable = Runnable {
                    if (isDashboardVisible && isAdded) {
                        collapseView(binding.dashboard)
                        expandView(binding.tvTFDashboard)
                        isDashboardVisible = false
                    }
                }
                handler.postDelayed(autoCollapseRunnable!!, autoCollapseDelay)
            }
        }
    }

    override fun allObserver() {

        viewModel.loadDashboardData()

        viewModel.dashboardData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DataState.Loading -> loadingDialog?.show()

                is DataState.Success -> {
                    loadingDialog?.dismiss()
                    binding.tvTotalProfit.text = "৳ ${String.format("%.2f", state.data?.totalProfit)}"
                    binding.tvTotalSell.text = "৳ ${String.format("%.2f", state.data?.totalSell)}"
                }

                is DataState.Error -> {
                    loadingDialog?.dismiss()
                }
            }
        }
    }
    //animation
    private fun expandView(view: View) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val targetHeight = view.measuredHeight

        view.layoutParams.height = 0
        view.alpha = 0f
        view.visibility = View.VISIBLE

        view.animate()
            .alpha(1f)
            .setDuration(350)
            .start()

        val animator = ValueAnimator.ofInt(0, targetHeight)
        animator.addUpdateListener {
            view.layoutParams.height = it.animatedValue as Int
            view.requestLayout()
        }
        animator.duration = 350
        animator.start()
    }

    private fun collapseView(view: View) {
        val initialHeight = view.measuredHeight

        val animator = ValueAnimator.ofInt(initialHeight, 0)
        animator.addUpdateListener {
            view.layoutParams.height = it.animatedValue as Int
            view.requestLayout()
        }

        view.animate()
            .alpha(0f)
            .setDuration(250)
            .withEndAction {
                view.visibility = View.GONE
            }
            .start()

        animator.duration = 250
        animator.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoCollapseRunnable?.let {
            handler.removeCallbacks(it)
        }
    }


}
