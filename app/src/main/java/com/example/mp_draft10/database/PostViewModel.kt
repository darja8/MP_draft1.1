package com.example.mp_draft10.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mp_draft10.classes.Comment
import com.example.mp_draft10.classes.Post
import com.example.mp_draft10.classes.ReplyComment

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val application: Application
    // Inject other dependencies here if needed
) : AndroidViewModel(application) {

    private var db = FirebaseFirestore.getInstance()
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts
    val postsLiveData = MutableLiveData<List<Post>>()
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()
    val usernames = MutableLiveData<Map<String, String>>(emptyMap())

    init {
        fetchPostsFromFirestore()
    }

    fun fetchCommentsForPost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val commentsSnapshot = db.collection("posts").document(postId)
                    .collection("comments")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get()
                    .await()

                val commentsWithUsernamesAndReplies = commentsSnapshot.documents.mapNotNull { doc ->
                    val comment = doc.toObject(Comment::class.java)
                    comment?.let {
                        val userDoc = FirebaseFirestore.getInstance().collection("Users").document(it.userId).get().await()
                        val username = userDoc.toObject(User::class.java)?.username

                    }
                    comment // Return the comment, now potentially with added username or processed replies
                }
                _comments.value = commentsWithUsernamesAndReplies
            } catch (e: Exception) {
                Log.e("PostDetailScreen", "Error fetching comments: ", e)
            }
        }
    }

    fun fetchPostsFromFirestore() {
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
                // Generate a new document reference with an auto-generated ID within the "comments" collection
                val newCommentRef = db.collection("posts").document(postId)
                    .collection("comments").document() // This creates a new document reference with a unique ID

                // Include the auto-generated ID in your comment object
                val commentWithId = newComment.copy(commentId = newCommentRef.id)

                // Set the comment data on this new document reference, including the auto-generated ID
                newCommentRef.set(commentWithId).await()

                // Assuming you have a way to update your UI or local data structure after successfully adding a comment
                val updatedComments = comments.value.toMutableList().apply {
                    add(commentWithId)
                }
                _comments.value = updatedComments

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

    fun addReplyToComment(postId: String, commentId: String, newReply: ReplyComment) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val commentRef = db.collection("posts").document(postId)
                    .collection("comments").document(commentId)

                commentRef.update("replies", FieldValue.arrayUnion(newReply)).await()

                fetchCommentsForPost(postId)

            } catch (e: Exception) {
                Log.e("AddReply", "Failed to add reply to comment: $e")
                // Consider providing user feedback here
            }
        }
    }

    fun removeComment(postId: String, commentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Path to the specific comment document
                val commentRef = db.collection("posts").document(postId)
                    .collection("comments").document(commentId)

                // Delete the comment document
                commentRef.delete().await()
                 val updatedComments = _comments.value.filterNot { it.commentId == commentId }
                _comments.value = updatedComments

            } catch (e: Exception) {
                Log.e("RemoveComment", "Failed to remove comment: $e")
            }
        }
    }

    fun setFirestoreInstance(firestore: FirebaseFirestore) {
        db = firestore
    }
}