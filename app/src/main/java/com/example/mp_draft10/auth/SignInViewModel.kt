package com.example.mp_draft10.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.auth.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class SignInViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    fun loginUser(email: String, password: String) {

        viewModelScope.launch {
            repository.loginUser(email, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val userType = repository.getUserType()
                        _signInState.send(SignInState(isSuccess = "Sign in success", userType = userType))
                    }
                    is Resource.Loading -> {
                        _signInState.send(SignInState(isLoading = true))
                    }
                    is Resource.Error -> {
                        _signInState.send(SignInState(isError = result.message))
                    }
                }
            }
        }
    }
}