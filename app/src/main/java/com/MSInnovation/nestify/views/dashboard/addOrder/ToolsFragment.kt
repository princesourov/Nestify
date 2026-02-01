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
        with(binding){
            spinnerPlatform.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Data.platform))
            spinnerDistrict.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Data.districts))
            spinnerSellType.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Data.sellType))
            spinnerDeliveryStatus.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Data.deliveryStatus))
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
                etParcelsId.isEmpty()
                spinnerDeliveryStatus.isEmpty()
                etPhone.isEmpty()
                etBuyPrice.isEmpty()
                etSellPrice.isEmpty()
                etDelivaryCharge.isEmpty()
                etTakenCharge.isEmpty()
                etPackaging.isEmpty()
                etAdsCost.isEmpty()

                if (!etDate.isEmpty() && !spinnerPlatform.isEmpty() && !etCustomerName.isEmpty() && !etProductName.isEmpty() && !spinnerDistrict.isEmpty() &&
                    !spinnerSellType.isEmpty() && !etCustomerLocation.isEmpty() && !etParcelsId.isEmpty() && !spinnerDeliveryStatus.isEmpty() && !etPhone.isEmpty()
                    && !etBuyPrice.isEmpty() && !etSellPrice.isEmpty() && !etDelivaryCharge.isEmpty() && !etTakenCharge.isEmpty()
                    && !etPackaging.isEmpty() && !etAdsCost.isEmpty())
                {
                    val buyPrice = binding.etBuyPrice.extract().toDoubleOrNull() ?: 0.0
                    val deliveryCharge = binding.etDelivaryCharge.extract().toDoubleOrNull() ?: 0.0
                    val sellPrice = binding.etSellPrice.extract().toDoubleOrNull() ?: 0.0
                    val takenCharge = binding.etTakenCharge.extract().toDoubleOrNull() ?: 0.0
                    val packaging = binding.etPackaging.extract().toDoubleOrNull() ?: 0.0
                    val adsCost = binding.etAdsCost.extract().toDoubleOrNull() ?: 0.0

                    val totalCost = buyPrice + deliveryCharge + packaging + adsCost
                    val totalSell = sellPrice + takenCharge

                    val netProfit = totalSell - totalCost
                    val profitPercent = if (totalCost != 0.0) (netProfit / totalCost) * 100 else 0.0

                    val orderData = OrderData(
                        binding.etDate.extract(),
                        binding.spinnerPlatform.extract(),
                        binding.etCustomerName.extract(),
                        binding.etProductName.extract(),
                        binding.spinnerDistrict.extract(),
                        binding.spinnerSellType.extract(),
                        binding.etCustomerLocation.extract(),
                        binding.etParcelsId.extract(),
                        binding.spinnerDeliveryStatus.extract(),
                        binding.etPhone.extract(),
                        binding.etBuyPrice.extract(),
                        binding.etSellPrice.extract(),
                        binding.etDelivaryCharge.extract(),
                        binding.etTakenCharge.extract(),
                        binding.etPackaging.extract(),
                        binding.etAdsCost.extract(),
                        netProfit.toString(),
                        String.format("%.2f", profitPercent)
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