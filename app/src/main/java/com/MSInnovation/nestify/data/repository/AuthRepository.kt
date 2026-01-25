package com.MSInnovation.nestify.data.repository


import com.MSInnovation.nestify.data.models.UserLogIn
import com.MSInnovation.nestify.data.services.AuthService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val qAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : AuthService {

    override fun userLogin(user: UserLogIn): Task<AuthResult> {

        return qAuth.signInWithEmailAndPassword(user.email, user.password)
    }

}