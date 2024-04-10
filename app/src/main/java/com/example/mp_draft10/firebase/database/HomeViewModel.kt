package com.example.mp_draft10.firebase.database

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val application: Application) : AndroidViewModel(application = application) {

    var userList = mutableStateListOf<User?>()
    var myDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        getUserDetails()
    }

    fun getUserDetails() {
        myDatabase.collection("Users").get()
            .addOnSuccessListener { mySnapshot ->
                if (!mySnapshot.isEmpty) {
                    val list = mySnapshot.documents
                    for (items in list) {
                        val myUser: User? = items.toObject(User::class.java)
                        userList.add(myUser)
                    }
                } else {
                    Toast.makeText(
                        application,
                        "No data found in Database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    application,
                    "Failed to get the data.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}