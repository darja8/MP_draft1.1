package com.example.mp_draft10.auth

import com.example.mp_draft10.auth.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    fun loginUser(email:String, password:String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun getCurrentEmail(): String

    suspend fun getUserType(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return "unknown"
        val userId = currentUser.uid
        val db = FirebaseFirestore.getInstance()
        try {
            val userDoc = db.collection("Users").document(userId).get().await()
            return userDoc.getString("usertype") ?: "default"
        } catch (e: Exception) {
            // Log the exception or handle it as needed
            return "unknown" // Return "unknown" or any default value in case of an error
        }
    }
}