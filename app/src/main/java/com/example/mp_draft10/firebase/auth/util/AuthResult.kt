package com.example.mp_draft10.firebase.auth.util

import com.example.mp_draft10.firebase.database.User

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Failure(val errorMessage: String) : AuthResult()
}
