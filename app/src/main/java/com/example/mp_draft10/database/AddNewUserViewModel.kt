package com.example.mp_draft10.database

//import com.example.mp_draft10.model.MoodRating
//import com.example.mp_draft10.model.MoodRating.*
import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.mp_draft10.di.AppModule
import com.example.mp_draft10.ui.screens.MoodData
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

    suspend fun fetchMoodDataFromSpecificDay(date: LocalDate): MoodData? {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return null
        val userEmail = currentUser.email ?: return null
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("Users").document(userEmail)
            .collection("Dates").document(date.toString())

        return try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) {
                val moodRating = snapshot.getLong("moodRating")?.toInt() ?: 0
                val moodObjects = snapshot.get("moodObjects") as? List<String> ?: emptyList()
                val symptomObjects = snapshot.get("symptomObjects") as? List<String> ?: emptyList()

                MoodData(date = date, moodRating = moodRating, moodObjects = moodObjects, symptomObjects = symptomObjects)
            } else {
                // Return MoodData with default values if no data found
                MoodData(date = date, moodRating = 0, moodObjects = emptyList(), symptomObjects = emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching mood data", e)
            null
        }
    }

    suspend fun fetchMoodRatingsForPast7Days(): Map<LocalDate, Int> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyMap()
        val userEmail = currentUser.email ?: return emptyMap()
        val db = FirebaseFirestore.getInstance()
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(6)
        val moodRatingsMap = mutableMapOf<LocalDate, Int>()

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            val dateString = currentDate.toString()
            val docRef = db.collection("Users").document(userEmail)
                .collection("Dates").document(dateString)

            try {
                val snapshot = docRef.get().await()
                if (snapshot.exists()) {
                    val moodRating = snapshot.getLong("moodRating")?.toInt() ?: 0
                    moodRatingsMap[currentDate] = moodRating
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching mood data for $currentDate", e)
            }
            currentDate = currentDate.plusDays(1)
        }

        return moodRatingsMap
    }



}
