package com.MSInnovation.nestify.views.dashboard.orderTracker

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.MSInnovation.nestify.base.BaseFragment
import com.MSInnovation.nestify.core.DataState
import com.MSInnovation.nestify.data.models.OrderData
import com.MSInnovation.nestify.databinding.FragmentOrderTrackerBinding
import com.MSInnovation.nestify.views.dashboard.addOrder.Data
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderTrackerFragment :
    BaseFragment<FragmentOrderTrackerBinding>(FragmentOrderTrackerBinding::inflate) {

    private val viewModel: OrderTrackerViewModel by viewModels()
    private lateinit var adapter: OrderTrackerAdapter
    private var fullOrderList = listOf<OrderData>()
    private var selectedStatus = "All"
    private var sortNewestFirst = true

    override fun setListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDataRequest()
        }
        setupStatusSpinner()
        setupSortSpinner()
    }

    override fun allObserver() {
        viewModel.getDataResponse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DataState.Loading -> loadingDialog?.show()

                is DataState.Error -> {
                    loadingDialog?.dismiss()
                    binding.swipeRefresh.isRefreshing = false
                }

                is DataState.Success -> {
                    loadingDialog?.dismiss()
                    binding.swipeRefresh.isRefreshing = false
                    val list = state.data ?: emptyList()
                    fullOrderList = list
                    setDataToRV(list)
                    applyFilters()
                }
            }
        }
    }

    private fun setDataToRV(list: List<OrderData>) {
        if (!::adapter.isInitialized) {
            adapter = OrderTrackerAdapter(list.toMutableList())
            binding.rvCustomerData.layoutManager = LinearLayoutManager(requireContext())
            binding.rvCustomerData.adapter = adapter
        } else {
            adapter.updateList(list)
        }
    }

    private fun setupStatusSpinner() {
        binding.spinnerStatus.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Data.deliveryStatus)

        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedStatus = Data.deliveryStatus[position]
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSortSpinner() {
        binding.spinnerSort.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Data.sortOptions)

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                sortNewestFirst = position == 0
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilters() {
        if (!::adapter.isInitialized) return

        val filteredList = fullOrderList
            .filter { status ->
                (selectedStatus == "All") || (status.status?.trim() == selectedStatus)
            }
            .let { list ->
                if (sortNewestFirst) list.sortedByDescending { it.createdAt }
                else list.sortedBy { it.createdAt }
            }

        adapter.updateList(filteredList)
        toggleEmptyView(filteredList)
    }

    private fun toggleEmptyView(list: List<OrderData>) {
        binding.layoutEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        binding.rvCustomerData.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
    }
}
