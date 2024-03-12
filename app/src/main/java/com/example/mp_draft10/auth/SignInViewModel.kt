package com.example.mp_draft10.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.auth.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    val email: MutableState<String> = mutableStateOf("")


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            repository.loginUser(email, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _signInState.send(SignInState(isSuccess = "Sign in success"))
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
