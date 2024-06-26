package com.example.mp_draft10.classes

data class Post(
    var id: String = "", // Ensure this property exists
    val content: String = "",
    var comments: List<Comment> = emptyList(),
    val likes: MutableList<String> = mutableListOf(),
    val imageUrl: String = "",
    val imageId: String = "",
    val userId: String = "",
)
