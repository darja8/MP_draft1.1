package com.example.mp_draft10.database

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import com.example.mp_draft10.auth.util.Resource
import com.example.mp_draft10.remote.PixabayAPI
import com.example.mp_draft10.remote.responses.ImageResponse
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseRepositoryImpl: DatabaseRepository {
    @SuppressLint("SuspiciousIndentation")
    override fun addUserDetails(addNewUserViewModel: AddNewUserViewModel, application: Application) {
        val dB: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbUsers: CollectionReference = dB.collection("Users")

        val users = User(username = addNewUserViewModel.userName, userEmail = addNewUserViewModel.userEmail)

        dbUsers.add(users).addOnSuccessListener {
            Toast.makeText(application, "User added successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(application, "Exception: $e", Toast.LENGTH_SHORT).show()
        }
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        lateinit var pixabayAPI: PixabayAPI
        return try{
            val response = pixabayAPI.searchForImage(imageQuery)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?:Resource.Error("Unknown Error", null)
            } else{
                Resource.Error("Unknown Error", null)
            }
        }catch (e: Exception){
            Resource.Error("Couldn't reach the server", null)
        }
    }
}