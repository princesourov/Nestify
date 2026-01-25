package com.MSInnovation.nestify.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.MSInnovation.nestify.core.DataState
import com.MSInnovation.nestify.data.models.UserLogIn
import com.MSInnovation.nestify.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlin.toString

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val authService: AuthRepository
) : ViewModel() {

    private val _logInResponse = MutableLiveData<DataState<UserLogIn>>()
    val logInResponse: LiveData<DataState<UserLogIn>> = _logInResponse

    fun userLogin(user: UserLogIn) {


        _logInResponse.postValue(DataState.Loading())
        authService.userLogin(user).addOnSuccessListener {
            _logInResponse.postValue(DataState.Success(user))

        }.addOnFailureListener {
            _logInResponse.postValue(DataState.Error(it.message.toString()))

        }
    }
}