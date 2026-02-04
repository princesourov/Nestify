package com.MSInnovation.nestify.data.services

import com.MSInnovation.nestify.data.models.OrderData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface AppService {
    fun orderData(user: OrderData) : Task<Void>
    fun getDataRequest(): Task<QuerySnapshot>
    fun updateOrderData(docId: String, pid: String, status: String, dCharge: String, adsCost: String, profit: String, profitPercent: String): Task<Void>

}