package com.example.mp_draft10.database

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.mp_draft10.di.AppModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddNewUserViewModel @Inject constructor(private val application: Application): AndroidViewModel(application = application) {

    val repository = AppModule.providesDatabaseRepositoryImpl()
    var userName by  mutableStateOf("")
    var userEmail by mutableStateOf("")

    fun addUserDetails(userEmail: String) {
        val dB: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbUsers: CollectionReference = dB.collection("Users")

        val user = hashMapOf(
            "username" to userName, // Assuming userName is a global variable or accessible in this context
            "userEmail" to userEmail
        )

        // Set document ID to user's email
        dbUsers.document(userEmail)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User details added successfully!")
                Toast.makeText(application, "User added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding user details", e)
                Toast.makeText(application, "Exception: $e", Toast.LENGTH_SHORT).show()
            }
    }


    fun saveMoodToFirestore(date: LocalDate, moodObjects: List<String>, symptomObjects: List<String>, moodRating: Int) {
        val dB: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbUsers: CollectionReference = dB.collection("Users")

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email

        // Check if userEmail is empty before proceeding
        if (!userEmail.isNullOrEmpty()) {
            val userDocRef = dbUsers.document(userEmail)

            // Create a data object to save
            val data = hashMapOf<String, Any>(
                "date" to date.toString(),  // Convert LocalDate to String
                "moodRating" to moodRating, // Include mood rating
                "moodObjects" to moodObjects,
                "symptomObjects" to symptomObjects
            )

            // Add the data to the user document
            userDocRef.collection("Dates")
                .document(date.toString())
                .set(data)
                .addOnSuccessListener {
                    // Handle success
                    Log.d(TAG, "Mood saved successfully!")
                    Toast.makeText(application, "Mood saved!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    // Handle failure
                    Log.e(TAG, "Error saving mood", e)
                    Toast.makeText(application, "Failed to save mood", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Handle case where userEmail is null or empty
            Log.e(TAG, "User email is null or empty!")
            Toast.makeText(application, "User email is null or empty!", Toast.LENGTH_SHORT).show()
        }
    }
    // Function to fetch mood ratings for the past 7 days from Firestore
    suspend fun fetchMoodDataForPast7Days(): List<Pair<LocalDate, Int>> {
        val dB: FirebaseFirestore = FirebaseFirestore.getInstance()
        val currentUserEmail = /* fetch current user email */ ""

        // Assuming the data is stored in a collection named "MoodData" under the user's document
        val moodDataCollection = dB.collection("Users").document(currentUserEmail)
            .collection("MoodData")

        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(6) // Get the date 7 days ago

        val moodDataList = mutableListOf<Pair<LocalDate, Int>>()

        // Query Firestore for mood data for the past 7 days
        moodDataCollection
            .whereGreaterThanOrEqualTo("date", startDate.toString())
            .whereLessThanOrEqualTo("date", endDate.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val date = LocalDate.parse(document["date"] as String)
                    val moodRating = (document["moodRating"] as Long).toInt()
                    moodDataList.add(date to moodRating)
                }
            }
            .await() // Await for the asynchronous task to complete

        return moodDataList
    }

    // Function to fetch symptom data for the past 30 days from Firestore
    suspend fun fetchSymptomDataForPast30Days(): Map<String, Int> {
        val dB: FirebaseFirestore = FirebaseFirestore.getInstance()
        val currentUserEmail = /* fetch current user email */ ""

        // Assuming the data is stored in a collection named "SymptomData" under the user's document
        val symptomDataCollection = dB.collection("Users").document(currentUserEmail)
            .collection("SymptomData")

        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(29) // Get the date 30 days ago

        val symptomDataMap = mutableMapOf<String, Int>()

        // Query Firestore for symptom data for the past 30 days
        symptomDataCollection
            .whereGreaterThanOrEqualTo("date", startDate.toString())
            .whereLessThanOrEqualTo("date", endDate.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val symptoms = document["symptoms"] as List<String>
                    symptoms.forEach { symptom ->
                        symptomDataMap[symptom] = (symptomDataMap[symptom] ?: 0) + 1
                    }
                }
            }
            .await() // Await for the asynchronous task to complete

        return symptomDataMap
    }
}
