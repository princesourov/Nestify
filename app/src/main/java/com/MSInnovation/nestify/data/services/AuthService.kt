package com.MSInnovation.nestify.data.services

import com.MSInnovation.nestify.data.models.UserLogIn
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AuthService {
    fun userLogin(user: UserLogIn) : Task<AuthResult>
}