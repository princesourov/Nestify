package com.MSInnovation.nestify.data.repository

import com.MSInnovation.nestify.core.Nodes
import com.MSInnovation.nestify.data.services.DashboardService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val db: FirebaseFirestore
) : DashboardService {

    private var listener: ListenerRegistration? = null

    override fun getDashboardSummary(
        onResult: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        listener?.remove()

        listener = db.collection(Nodes.ORDER_DATA)
            .addSnapshotListener { value, error ->

                if (error != null) {
                    onError(error.message ?: "Firestore error")
                    return@addSnapshotListener
                }

                var totalProfit = 0.0
                var totalSell = 0.0

                value?.forEach { doc ->
                    totalProfit += doc.getString("profit")?.toDoubleOrNull() ?: 0.0
                    totalSell += doc.getString("sprice")?.toDoubleOrNull() ?: 0.0
                }

                onResult(totalProfit, totalSell)
            }
    }

    override fun clearListener() {
        listener?.remove()
        listener = null
    }
}
