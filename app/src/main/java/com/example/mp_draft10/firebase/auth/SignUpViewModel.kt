package com.example.mp_draft10.firebase.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.firebase.auth.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to register a user
 */

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _signUpState = Channel<SignInState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            repository.registerUser(email, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _signUpState.send(SignInState(isSuccess = "Sign up success"))
                    }
                    is Resource.Loading -> {
                        _signUpState.send(SignInState(isLoading = true))
                    }
                    is Resource.Error -> {
                        _signUpState.send(SignInState(isError = result.message))
                    }
                }
            }
        }
    }
}