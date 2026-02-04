package com.MSInnovation.nestify.views.dashboard.addOrder

import android.app.DatePickerDialog
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.MSInnovation.nestify.R
import com.MSInnovation.nestify.base.BaseFragment
import com.MSInnovation.nestify.core.DataState
import com.MSInnovation.nestify.core.extract
import com.MSInnovation.nestify.core.isEmpty
import com.MSInnovation.nestify.data.models.OrderData
import com.MSInnovation.nestify.databinding.FragmentToolsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import kotlin.getValue

@AndroidEntryPoint
class ToolsFragment : BaseFragment<FragmentToolsBinding>(FragmentToolsBinding::inflate) {

    private val viewModel: AddOrderViewModel by viewModels()

    override fun setListener() {

        setupDropdowns()
        setupDatePicker()
        addData()

    }

    override fun allObserver() {
        observeAddData()

    }

    private fun setupDropdowns() {
        with(binding) {
            spinnerPlatform.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Data.platform)
            )
            spinnerDistrict.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Data.districts)
            )
            spinnerSellType.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Data.sellType)
            )
        }
    }

    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    binding.etDate.setText("$day/${month + 1}/$year")
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun addData() {
        with(binding) {
            btnAddData.setOnClickListener {
                etDate.isEmpty()
                spinnerPlatform.isEmpty()
                etCustomerName.isEmpty()
                etProductName.isEmpty()
                spinnerDistrict.isEmpty()
                spinnerSellType.isEmpty()
                etCustomerLocation.isEmpty()
                etPhone.isEmpty()
                etBuyPrice.isEmpty()
                etSellPrice.isEmpty()
                etTakenCharge.isEmpty()
                etPackaging.isEmpty()

                if (!etDate.isEmpty() && !spinnerPlatform.isEmpty() && !etCustomerName.isEmpty() && !etProductName.isEmpty()
                    && !spinnerDistrict.isEmpty() && !spinnerSellType.isEmpty() && !etCustomerLocation.isEmpty() && !etPhone.isEmpty() && !etBuyPrice.isEmpty()
                    && !etSellPrice.isEmpty() && !etTakenCharge.isEmpty() && !etPackaging.isEmpty()
                ) {

                    val orderData = OrderData(
                        binding.etOrderNo.extract(),
                        binding.etDate.extract(),
                        binding.spinnerPlatform.extract(),
                        binding.etCustomerName.extract(),
                        binding.etProductName.extract(),
                        binding.spinnerDistrict.extract(),
                        binding.spinnerSellType.extract(),
                        binding.etCustomerLocation.extract(),
                        "0000000000",
                        "In Review",
                        binding.etPhone.extract(),
                        binding.etBuyPrice.extract(),
                        binding.etSellPrice.extract(),
                        "00",
                        binding.etTakenCharge.extract(),
                        binding.etPackaging.extract(),
                        "00",
                        "00",
                        "00",
                    )
                    viewModel.addOrderData(orderData)
                }
            }
        }
    }

    private fun observeAddData() {
        viewModel.addOrderResponse.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> loadingDialog?.show()
                is DataState.Error -> {
                    loadingDialog?.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                is DataState.Success -> {
                    loadingDialog?.dismiss()

                    findNavController().navigate(
                        R.id.action_toolsFragment_to_orderTrackerFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.toolsFragment, true)
                            .build()
                    )

                }
            }
        }
    }

}