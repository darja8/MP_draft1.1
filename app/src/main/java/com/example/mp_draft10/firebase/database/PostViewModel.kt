package com.example.mp_draft10.firebase.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
import java.util.UUID
import javax.inject.Inject

/**
 * The PostViewModel manages all data interactions related to posts, comments, and replies within the application.
 * It facilitates the fetching, adding, updating, and deleting of post-related data from Firebase Firestore.
 * Key functionalities include:
 * - Fetching posts and comments from Firestore and maintaining them in real-time.
 * - Adding new posts, comments, and replies, ensuring that each entry is correctly linked to its respective post or comment.
 * - Removing posts, comments, and individual replies, handling cleanup of associated data efficiently.
 * - Updating the likes for posts and managing user interactions dynamically.
 * This ViewModel leverages Kotlin Coroutines for asynchronous tasks, ensuring operations are performed efficiently off the UI thread.
 */


@HiltViewModel
class PostViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private var db = FirebaseFirestore.getInstance()
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()
    private val _post = MutableStateFlow<Post?>(null)

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

    fun fetchAllPosts() {
        val db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                val fetchedPosts = result.toObjects(Post::class.java)
                _posts.value = fetchedPosts // Update the mutable flow
            }
            .addOnFailureListener { exception ->
                Log.e("PostViewModel", "Error fetching posts: ", exception)
            }
    }


    fun addReplyToComment(postId: String, commentId: String, newReply: ReplyComment) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Generate a unique ID for the reply
                val replyId = UUID.randomUUID().toString()

                // Assuming ReplyComment is a data class, create a new instance with the replyId set
                val newReplyWithId = newReply.copy(replyId = replyId)

                val commentRef = db.collection("posts").document(postId)
                    .collection("comments").document(commentId)

                // Update the document with the new reply that includes a unique ID
                commentRef.update("replies", FieldValue.arrayUnion(newReplyWithId)).await()

                // Fetch updated comments for the post
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

    fun removeReplyFromComment(postId: String, commentId: String, replyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val commentRef = db.collection("posts").document(postId)
                    .collection("comments").document(commentId)

                val snapshot = commentRef.get().await()
                val replies = snapshot["replies"] as? List<Map<String, Any>> ?: listOf()

                val updatedReplies = replies.filterNot { it["replyId"] == replyId }

                commentRef.update("replies", updatedReplies).await()

                fetchCommentsForPost(postId)
            } catch (e: Exception) {
                Log.e("RemoveReply", "Failed to remove reply from comment: $e")
            }
        }
    }

    fun toggleLikePost(postId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postRef = db.collection("posts").document(postId)
                val postSnapshot = postRef.get().await()
                val post = postSnapshot.toObject(Post::class.java)

                post?.let {
                    if (it.likes.contains(userId)) {
                        it.likes.remove(userId) // Unlike the post
                    } else {
                        it.likes.add(userId) // Like the post
                    }
                    postRef.update("likes", it.likes).await()
                }
            } catch (e: Exception) {
                Log.e("toggleLikePost", "Error toggling post like: ", e)
            }
        }
    }
    fun fetchPostById(postId: String, onComplete: (Post?) -> Unit) {
        FirebaseFirestore.getInstance().collection("posts").document(postId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val post = document.toObject(Post::class.java)
                    _post.value = post
                    onComplete(post)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }
    fun savePostToFirestore(post: Post) {
        val db = FirebaseFirestore.getInstance()

        val documentReference = if (post.id.isEmpty()) {
            db.collection("posts").document() // Create a new document with a generated ID
        } else {
            db.collection("posts").document(post.id) // Use the existing ID
        }

        post.id = documentReference.id // Update the post's ID with the document's ID (important for new documents)

        documentReference.set(post)
            .addOnSuccessListener { Log.d("Firestore", "Post successfully written!") }
            .addOnFailureListener { e -> Log.w("Firestore", "Error writing post", e) }
    }

    fun removePost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postRef = db.collection("posts").document(postId)
                val commentsRef = postRef.collection("comments")

                // Fetch and delete all comments associated with the post
                val batchSize = 10  // Set an appropriate batch size for your needs
                var deletedCount: Int
                do {
                    // Retrieve a batch of comments
                    val snapshot = commentsRef.limit(batchSize.toLong()).get().await()
                    deletedCount = snapshot.size()
                    snapshot.documents.forEach { document ->
                        document.reference.delete().await()  // Delete each comment
                    }
                } while (deletedCount >= batchSize)  // Continue if there were enough comments to possibly fill another batch

                // After deleting the comments, delete the post document
                postRef.delete().await()

                // Update the local UI state to reflect the removal
                val updatedPosts = _posts.value.filterNot { it.id == postId }
                _posts.value = updatedPosts

                Log.d("RemovePost", "Post and all associated comments successfully removed")
            } catch (e: Exception) {
                Log.e("RemovePost", "Failed to remove post and comments: $e")
            }
        }
    }

}