package com.MSInnovation.nestify.views.dashboard.addOrder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.MSInnovation.nestify.core.DataState
import com.MSInnovation.nestify.data.models.OrderData
import com.MSInnovation.nestify.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AddOrderViewModel @Inject constructor(
    private val userService: AppRepository
) : ViewModel() {


    private val _addOrderResponse = MutableLiveData<DataState<OrderData>>()
    val addOrderResponse: LiveData<DataState<OrderData>> = _addOrderResponse


    private val _updateOrderResponse = MutableLiveData<DataState<String>>()
    val updateOrderResponse: LiveData<DataState<String>> = _updateOrderResponse


    fun addOrderData(user: OrderData) {
        _addOrderResponse.postValue(DataState.Loading())
        userService.orderData(user)
            .addOnSuccessListener {
                _addOrderResponse.postValue(DataState.Success(user))
            }.addOnFailureListener { error ->
                _addOrderResponse.postValue(DataState.Error(error.message ?: "Add failed"))
            }
    }


    fun updateOrder(docId: String, pid: String, status: String, dCharge: String, adsCost: String, profit: String, profitPercent: String) {
        _updateOrderResponse.postValue(DataState.Loading())
        userService.updateOrderData(docId, pid, status, dCharge, adsCost, profit,profitPercent)
            .addOnSuccessListener {
                _updateOrderResponse.postValue(DataState.Success("Updated"))
            }.addOnFailureListener { error ->
                _updateOrderResponse.postValue(DataState.Error(error.message ?: "Update failed"))
            }
    }
}
