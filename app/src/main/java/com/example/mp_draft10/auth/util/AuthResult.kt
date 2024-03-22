package com.example.mp_draft10.auth.util

import com.example.mp_draft10.database.User

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Failure(val errorMessage: String) : AuthResult()
}
