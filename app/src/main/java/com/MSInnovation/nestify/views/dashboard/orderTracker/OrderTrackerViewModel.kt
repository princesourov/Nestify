package com.MSInnovation.nestify.views.dashboard.orderTracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.MSInnovation.nestify.core.DataState
import com.MSInnovation.nestify.data.models.OrderData
import com.MSInnovation.nestify.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class OrderTrackerViewModel @Inject constructor(
    private val appService: AppRepository
) : ViewModel() {

    private val _getDataResponse = MutableLiveData<DataState<List<OrderData>>>()
    val getDataResponse: LiveData<DataState<List<OrderData>>> = _getDataResponse

    init {
        getDataRequest()
    }

    fun getDataRequest() {
        _getDataResponse.postValue(DataState.Loading())

        appService.getDataRequest()
            .addOnSuccessListener { document ->

                val list = mutableListOf<OrderData>()

                document.documents.forEach { doc ->
                    doc.toObject(OrderData::class.java)?.let { order ->
                        order.docId = doc.id
                        list.add(order)
                    }
                }

                val sortedList = list.sortedByDescending { it.createdAt }

                _getDataResponse.postValue(DataState.Success(sortedList))
            }.addOnFailureListener {
                _getDataResponse.postValue(
                    DataState.Error(it.message ?: "Error fetching orders")
                )
            }
    }
}
