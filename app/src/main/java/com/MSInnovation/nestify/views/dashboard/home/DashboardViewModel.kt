package com.MSInnovation.nestify.views.dashboard.home

import androidx.lifecycle.*
import com.MSInnovation.nestify.core.DataState
import com.MSInnovation.nestify.data.models.DashboardSummary
import com.MSInnovation.nestify.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _dashboardData = MutableLiveData<DataState<DashboardSummary>>()
    val dashboardData: LiveData<DataState<DashboardSummary>> = _dashboardData

    fun loadDashboardData() {
        _dashboardData.value = DataState.Loading()

        repository.getDashboardSummary(
            onResult = { profit, sell ->
                _dashboardData.value =
                    DataState.Success(DashboardSummary(profit, sell))
            },
            onError = {
                _dashboardData.value = DataState.Error(it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearListener()
    }
}
