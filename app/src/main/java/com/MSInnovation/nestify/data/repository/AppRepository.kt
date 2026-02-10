package com.MSInnovation.nestify.data.repository

import com.MSInnovation.nestify.core.Nodes
import com.MSInnovation.nestify.data.models.OrderData
import com.MSInnovation.nestify.data.services.AppService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val db: FirebaseFirestore
) : AppService {
    override fun orderData(user: OrderData): Task<Void> {
        return db.collection(Nodes.ORDER_DATA)
            .document()
            .set(user)
    }
    override fun getDataRequest(): Task<QuerySnapshot> {
        return db.collection(Nodes.ORDER_DATA).get()
    }
    override fun updateOrderData(docId: String, pid: String, status: String, dCharge: String, adsCost: String, profit: String, profitPercent: String): Task<Void> {
        return db.collection(Nodes.ORDER_DATA)
            .document(docId)
            .update(
                mapOf(
                    "pid" to pid,
                    "status" to status,
                    "dcharge" to dCharge,
                    "adsCost" to adsCost,
                    "profit" to profit,
                    "profitPercent" to profitPercent
                )
            )
    }
}
