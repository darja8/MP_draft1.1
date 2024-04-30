package com.example.mp_draft10.classes

data class Article (
    var articleTitle: String = "",
    val articleId: String = "",
    var articleText: String = "",
    val tags: List<ArticleTag> = emptyList(),
    val imageId: Int = 0,
    val titleColor: Int = 0,
    var backgroundColor: Int = 0,
    var isDailyArticle: Boolean? = null
)
