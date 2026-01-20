package com.MSInnovation.nestify.base
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.MSInnovation.nestify.R

@Suppress("DEPRECATION")
abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB

) : Fragment() {

    private var _binding: VB? = null
    val binding: VB get() = _binding as VB

    var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)

        setupLoadingDialog()

        setListener()
        allObserver()

        return binding.root
    }

    fun setupLoadingDialog(){
        loadingDialog = Dialog(requireContext())
        loadingDialog!!.setContentView(R.layout.layout_progress)
        loadingDialog!!.setCancelable(false)
        loadingDialog!!.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    abstract fun setListener()
    abstract fun allObserver()

}