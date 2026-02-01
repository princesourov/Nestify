package com.MSInnovation.nestify.data.repository

import com.MSInnovation.nestify.core.Nodes
import com.MSInnovation.nestify.data.models.OrderData
import com.MSInnovation.nestify.data.services.AppService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AppRepository @Inject constructor(private val db: FirebaseFirestore
) : AppService {
    override fun orderData(user: OrderData): Task<Void> {
        return db.collection(Nodes.ORDER_DATA).document().set(user)
    }
}