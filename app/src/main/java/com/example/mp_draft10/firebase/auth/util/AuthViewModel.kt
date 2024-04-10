package com.example.mp_draft10.firebase.auth.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
//    val isAuthenticated = MutableLiveData<Boolean>(false)
val isAuthenticated = MutableLiveData<Boolean>(false)

    fun logOut() {
        isAuthenticated.value = false
    }

    fun logIn() {
        isAuthenticated.value = true
    }
}