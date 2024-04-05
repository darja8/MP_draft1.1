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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.classes.MoodData
import com.example.mp_draft10.di.AppModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AddNewUserViewModel @Inject constructor(private val application: Application): AndroidViewModel(application = application) {

    val repository = AppModule.providesDatabaseRepositoryImpl()
    var userName by  mutableStateOf("")
    var userEmail by mutableStateOf("")
    var moodCounts = mutableStateOf<Map<String, Int>>(mapOf())
    var symptomCounts = mutableStateOf<Map<String, Int>>(mapOf())
    private val _moodDates = MutableLiveData<List<LocalDate>>()
    val moodDates: LiveData<List<LocalDate>> = _moodDates
    private val _moodAndSymptoms = MutableLiveData<List<String>>()
    private val _datesWithData = MutableStateFlow<List<LocalDate>>(emptyList())
    val datesWithData: StateFlow<List<LocalDate>> = _datesWithData.asStateFlow()

    init {
        fetchListOfDatesWithData()
        listenForDateChanges()
    }
    private fun listenForDateChanges() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val userId = currentUser.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("Users").document(userId).collection("Dates")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val updatedDates = snapshot?.documents?.mapNotNull { document ->
                    try {
                        LocalDate.parse(document.id, DateTimeFormatter.ISO_LOCAL_DATE)
                    } catch (e: DateTimeParseException) {
                        null
                    }
                } ?: emptyList()

                _datesWithData.value = updatedDates
            }
    }

    fun addUserDetails(userName: String, userEmail: String, userType: String) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("Users")

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId == null) {
            Log.e("AddUserDetails", "Error: No authenticated user found.")
            Toast.makeText(application, "Error: No authenticated user found.", Toast.LENGTH_SHORT).show()
            return
        }

        val avatarIndexRange = 0..28
        val colorIndexRange = 0..23

        val randomAvatarIndex = Random.nextInt(avatarIndexRange.first, avatarIndexRange.last + 1)
        val randomColorIndex = Random.nextInt(colorIndexRange.first, colorIndexRange.last + 1)

        // Now, call saveUserAvatar with the randomly generated indices
        saveUserAvatar(randomColorIndex, randomAvatarIndex)

        val user = hashMapOf(
            "username" to userName,
            "userEmail" to userEmail,
            "usertype" to userType
        )

        // Set the document ID to the user's UID
        usersCollection.document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("AddUserDetails", "User details added for user ID: $userId")
                Toast.makeText(application, "User added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("AddUserDetails", "Error adding user details", e)
                Toast.makeText(application, "Exception: $e", Toast.LENGTH_SHORT).show()
            }
    }

    suspend fun fetchUserType(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return null
        val userId = currentUser.uid
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("Users")

        return try {
            val documentSnapshot = usersCollection.document(userId).get().await()
            if (documentSnapshot.exists()) {
                documentSnapshot.getString("usertype") ?: "Unknown" // Return "Unknown" if userType is null
            } else {
                Log.e("FetchUserType", "No such user exists.")
                "Unknown" // Define a default value for user type
            }
        } catch (exception: Exception) {
            Log.e("FetchUserType", "Error fetching user type", exception)
            "Unknown" // Return the default value in case of error
        }
    }

    fun saveMoodToFirestore(date: LocalDate, moodObjects: List<String>, symptomObjects: List<String>, moodRating: Int) {
        val dB: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbUsers: CollectionReference = dB.collection("Users")

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        // Check if userEmail is empty before proceeding
        if (!userId.isNullOrEmpty()) {
            val userDocRef = dbUsers.document(userId)

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
                    Toast.makeText(application, "Saved successfully!", Toast.LENGTH_SHORT).show()
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

    fun deleteDateDocument(date: LocalDate) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: run {
            Log.e("DeleteDateDocument", "User ID is null or empty!")
            return
        }

        // Reference to the specific date document inside the user's "Dates" collection
        val dateDocRef = db.collection("Users").document(userId).collection("Dates").document(date.toString())

        // Attempt to delete the document
        dateDocRef.delete().addOnSuccessListener {
            Log.d("DeleteDateDocument", "Document successfully deleted!")
            Toast.makeText(application, "Saved successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Log.e("DeleteDateDocument", "Error deleting document", e)
        }
    }

    suspend fun fetchMoodDataFromSpecificDay(date: LocalDate): MoodData? {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return null
        val userId = currentUser.uid
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("Users").document(userId)
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

    private fun fetchListOfDatesWithData() {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser ?: return@launch
            val userId = currentUser.uid
            val db = FirebaseFirestore.getInstance()
            val datesCollectionRef = db.collection("Users").document(userId).collection("Dates")

            try {
                val snapshot = datesCollectionRef.get().await()
                val dates = snapshot.documents.mapNotNull { document ->
                    try {
                        LocalDate.parse(document.id, DateTimeFormatter.ISO_LOCAL_DATE)
                    } catch (e: DateTimeParseException) {
                        null
                    }
                }
                _datesWithData.value = dates
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching dates with data", e)
            }
        }
    }

    suspend fun fetchMoodAndSymptomsForPast30Days(): List<String> {
        val moodAndSymptomsForPast30Days = mutableListOf<String>()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return emptyList() // Early return if no user

        val db = FirebaseFirestore.getInstance()
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(29)

        try {
            val snapshot = db.collection("Users").document(userId)
                .collection("Dates")
                .whereGreaterThanOrEqualTo("date", startDate.toString())
                .whereLessThanOrEqualTo("date", endDate.toString())
                .get()
                .await()

            for (document in snapshot.documents) {
                val moodObjects = document.get("moodObjects") as? List<String> ?: emptyList()
                val symptomObjects = document.get("symptomObjects") as? List<String> ?: emptyList()
                moodAndSymptomsForPast30Days.addAll(moodObjects)
                moodAndSymptomsForPast30Days.addAll(symptomObjects)
            }
        } catch (e: Exception) {
            // Handle your error: log it, show a message to the user, etc.
            e.printStackTrace()
        }

        return moodAndSymptomsForPast30Days
    }

    suspend fun fetchMoodRatingsForPast7Days(): Map<LocalDate, Int> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyMap()
        val userId = currentUser.uid ?: return emptyMap()
        val db = FirebaseFirestore.getInstance()
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(6)
        val moodRatingsMap = mutableMapOf<LocalDate, Int>()

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            val dateString = currentDate.toString()
            val docRef = db.collection("Users").document(userId)
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

    suspend fun fetchMoodRatingsForPast30Days(): Map<LocalDate, Int> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyMap()
        val userId = currentUser.uid
        val db = FirebaseFirestore.getInstance()
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(29) // Fetch data for the past 30 days, including today
        val moodRatingsMap = mutableMapOf<LocalDate, Int>()

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            val dateString = currentDate.toString()
            val docRef = db.collection("Users").document(userId)
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

    fun saveUserAvatar(backgroundColorIndex: Int, avatarImageIndex: Int) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return

        // Convert the indices to strings
        val avatarDetails = hashMapOf(
            "backgroundColor" to backgroundColorIndex.toString(),
            "avatarImage" to avatarImageIndex.toString()
        )

        // Directly set (or update) the avatar details in the user's document
        db.collection("Users").document(userId)
            .set(mapOf("avatar" to avatarDetails), SetOptions.merge())
            .addOnSuccessListener {
                Log.d("saveUserAvatar", "Avatar details saved successfully!")
                Toast.makeText(application, "Saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("saveUserAvatar", "Error saving avatar details", e)
                Toast.makeText(application, "Failed to save avatar.", Toast.LENGTH_SHORT).show()

                // Handle failure, e.g., show an error message
            }
    }

    suspend fun fetchAvatarImageString(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return null
        val db = FirebaseFirestore.getInstance()

        try {
            val userDoc = db.collection("Users").document(userId).get().await()
            if (userDoc.exists()) {
                val avatarDetails = userDoc.data?.get("avatar") as? Map<String, Any?>
                return avatarDetails?.get("avatarImage") as? String
            }
        } catch (e: Exception) {
            Log.e("fetchAvatarImageString", "Error fetching avatar image string", e)
        }
        return null // Return null if user document doesn't exist or on failure
    }

    suspend fun fetchAvatarBackgroundString(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return null
        val db = FirebaseFirestore.getInstance()

        try {
            val userDoc = db.collection("Users").document(userId).get().await()
            if (userDoc.exists()) {
                val avatarDetails = userDoc.data?.get("avatar") as? Map<String, Any?>
                return avatarDetails?.get("backgroundColor") as? String
            }
        } catch (e: Exception) {
            Log.e("fetchAvatarBackgroundString", "Error fetching avatar background string", e)
        }
        return null // Return null if user document doesn't exist or on failure
    }

    // In AddNewUserViewModel
    fun fetchAvatarIndicesById(userId: String, onResult: (Int?, Int?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val avatarDetails = documentSnapshot.data?.get("avatar") as? Map<String, Any?>
                    val backgroundColorIndex = avatarDetails?.get("backgroundColor") as? String
                    val avatarImageIndex = avatarDetails?.get("avatarImage") as? String
                    // Assuming the stored indices are strings, convert them to Int
                    onResult(backgroundColorIndex?.toIntOrNull(), avatarImageIndex?.toIntOrNull())
                } else {
                    onResult(null, null)
                }
            }
            .addOnFailureListener {
                onResult(null, null)
            }
    }
}