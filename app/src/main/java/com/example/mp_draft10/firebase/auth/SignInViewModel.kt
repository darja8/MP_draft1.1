package com.example.mp_draft10.firebase.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.firebase.auth.util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sign in ViewModel do allow user sign in
 */

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
    fun sendPasswordResetEmail(email: String) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    _signInState.send() = SignInState(isSuccess = "Reset link sent to your email")
                } else {
//                    _signInState.value = SignInState(isError = "Failed to send reset email: ${task.exception?.message}")
                }
            }
    }
}