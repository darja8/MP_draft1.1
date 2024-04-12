package com.example.mp_draft10.classes

data class Article (
    val articleTitle: String = "",
    val articleId: String = "",
    val articleText: String = "",
    val tags: List<ArticleTag> = emptyList(),
    val imageId: String = ""
)
