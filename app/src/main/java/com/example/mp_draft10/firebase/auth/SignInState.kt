package com.example.mp_draft10.firebase.auth

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = "",
    val userType: String? = null
) {

}