package com.MSInnovation.nestify.data.services

interface DashboardService {
    fun getDashboardSummary(
        onResult: (Double, Double) -> Unit,
        onError: (String) -> Unit
    )

    fun clearListener()
}
