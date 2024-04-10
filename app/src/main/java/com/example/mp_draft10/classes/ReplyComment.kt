package com.example.mp_draft10.classes

data class ReplyComment(
    val text: String = "",
    val userId: String = "",
    val timestamp: Long = 0L,
    val replyId: String = "",
)