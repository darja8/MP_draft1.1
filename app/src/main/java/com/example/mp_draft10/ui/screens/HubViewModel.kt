package com.example.mp_draft10.ui.screens

//class ChatViewModel : ViewModel() {
//
//    private val _posts = MutableStateFlow<List<Post>>(emptyList())
//    val posts: StateFlow<List<Post>> = _posts
//
//    init {
//        fetchAllPosts()
//    }
//
//    private fun fetchAllPosts() {
//        viewModelScope.launch {
//            try {
//                val postsSnapshot = FirebaseFirestore.getInstance().collection("posts").get().await()
//                val fetchedPosts = postsSnapshot.documents.mapNotNull { documentSnapshot ->
//                    // Create Post objects from the Firestore documents, include the document ID as the post ID
//                    documentSnapshot.toObject(Post::class.java)?.copy(id = documentSnapshot.id)
//                }
//                _posts.value = fetchedPosts
//            } catch (e: Exception) {
//                // Handle any errors, e.g., logging or updating UI to show an error message
//            }
//        }
//    }
//
//    fun postComment(postId: String, commentText: String) {
//        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
//        // Create a new Comment object
//        val newComment = Comment(
//            text = commentText,
//            userId = userId,
//            timestamp = System.currentTimeMillis()
//        )
//        // Convert the Comment object to a map for Firestore
//        val commentMap = hashMapOf(
//            "text" to newComment.text,
//            "userId" to newComment.userId,
//            "timestamp" to newComment.timestamp
//        )
//        // Add the comment to Firestore under the specified post's comments collection
//        FirebaseFirestore.getInstance().collection("posts").document(postId)
//            .collection("comments").add(commentMap)
//            .addOnSuccessListener {
//                // Optionally handle success
//            }
//            .addOnFailureListener {
//                // Optionally handle failure
//            }
//    }
//
//}

//class PostViewModel : ViewModel() {
//    private val db = FirebaseFirestore.getInstance()
//
//    // Assuming Post is your data class for posts
//    fun findPostById(postId: String, onResult: (Post?) -> Unit) {
//        viewModelScope.launch {
//            val postRef = db.collection("posts").document(postId)
//            try {
//                val document = postRef.get().await()
//                val post = if (document.exists()) {
//                    document.toObject(Post::class.java)?.apply {
//                        id = document.id
//                    }
//                } else {
//                    null
//                }
//                onResult(post)
//            } catch (e: Exception) {
//                onResult(null)
//            }
//        }
//    }
//}

data class Comment(
    val text: String = "",
    val userId: String = "",
    val timestamp: Long = 0L,
)

data class Post(
    var id: String = "", // Ensure this property exists
    val content: String = "",
    var comments: List<Comment> = emptyList()
)
