package com.example.mp_draft10.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.ui.screens.Comment
import com.example.mp_draft10.ui.screens.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts
    val postsLiveData = MutableLiveData<List<Post>>()
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()
    val usernames = MutableLiveData<Map<String, String>>(emptyMap())

    init {
        fetchPostsFromFirestore()
    }

//    fun fetchCommentsForPost(postId: String) {
//        db.collection("posts").document(postId).get()
//            .addOnSuccessListener { documentSnapshot ->
//                val post = documentSnapshot.toObject(Post::class.java)
//                post?.comments?.let { comments ->
//                    _comments.value = comments
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.w("ViewModel", "Fetching post failed.", e)
//            }
//    }

    fun listenForComments(postId: String) {
        db.collection("posts").document(postId).collection("comments")
            .orderBy("timestamp") // Assuming there's a 'timestamp' field to sort comments
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Log the error or handle it appropriately
                    return@addSnapshotListener
                }

                val commentsList = mutableListOf<Comment>()
                if (snapshot != null) {
                    for (doc in snapshot.documents) {
                        doc.toObject(Comment::class.java)?.let { comment ->
                            commentsList.add(comment)
                        }
                    }
                }
                // Update the StateFlow with the latest list of comments
                _comments.value = commentsList
            }
    }

    fun fetchCommentsForPost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val commentsSnapshot = db.collection("posts").document(postId)
                    .collection("comments")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get()
                    .await()

                val commentsWithUsernames = commentsSnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)?.also { comment ->
                        val userDoc = FirebaseFirestore.getInstance().collection("Users").document(comment.userId).get().await()
                    }
                }
                _comments.value = commentsWithUsernames
            } catch (e: Exception) {
                Log.e("PostDetailScreen", "Error fetching comments: ", e)
            }
        }
    }


    private fun fetchPostsFromFirestore() {
        db.collection("posts")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val postList = mutableListOf<Post>()
                for (doc in snapshot!!) {
                    doc.toObject(Post::class.java)?.let {
                        postList.add(it)
                    }
                }
                _posts.value = postList
            }
    }

    fun addCommentToPost(postId: String, newComment: Comment) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val addedCommentRef = db.collection("posts").document(postId)
                    .collection("comments").add(newComment).await()
                if (addedCommentRef.id.isNotEmpty()) {
                    val updatedComments = comments.value.toMutableList().apply {
                        add(newComment)
                    }
                    _comments.value = updatedComments
                }
            } catch (e: Exception) {
                Log.e("AddComment", "Failed to add comment: $e")
            }
        }
    }

    suspend fun fetchPostById(postId: String): Post? {
        // Fetch the post from Firestore and convert it to a Post object
        return try {
            val docSnapshot = FirebaseFirestore.getInstance().collection("posts").document(postId).get().await()
            docSnapshot.toObject(Post::class.java)
        } catch (e: Exception) {
            null // Handle exception appropriately
        }
    }
}