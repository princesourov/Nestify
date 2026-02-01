package com.MSInnovation.nestify.data.services

import com.MSInnovation.nestify.data.models.OrderData
import com.google.android.gms.tasks.Task

interface AppService {
    fun orderData(user: OrderData) : Task<Void>
}