package com.example.mp_draft10.classes


data class Comment(
    val text: String = "",
    val userId: String = "",
    val timestamp: Long = 0L,
    val commentId: String = "",
    val replies: List<ReplyComment> = emptyList()
)