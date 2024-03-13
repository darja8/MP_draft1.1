package com.example.mp_draft10.ui.screens
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HubViewModel : ViewModel() {
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    init {
        fetchComments()
    }

    private fun fetchComments() {
        viewModelScope.launch {
            FirebaseFirestore.getInstance().collection("posts").document("YOUR_POST_ID")
                .collection("comments").orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Handle the error
                        return@addSnapshotListener
                    }

                    val commentList = mutableListOf<Comment>()
                    for (doc in snapshot!!) {
                        val comment = doc.toObject(Comment::class.java)
                        commentList.add(comment)
                    }
                    _comments.value = commentList
                }
        }
    }

    fun postComment(commentText: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val comment = hashMapOf(
            "text" to commentText,
            "timestamp" to System.currentTimeMillis(),
            "userId" to userId
        )
        FirebaseFirestore.getInstance().collection("posts").document("YOUR_POST_ID")
            .collection("comments").add(comment)
            .addOnSuccessListener {
                // Optionally handle success
            }
            .addOnFailureListener {
                // Optionally handle failure
            }
    }
}

data class Comment(
    val text: String = "",
    val userId: String = "",
    val timestamp: Long = 0L
)
