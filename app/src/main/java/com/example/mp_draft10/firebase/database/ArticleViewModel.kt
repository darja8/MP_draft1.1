package com.example.mp_draft10.firebase.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.mp_draft10.classes.Article
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    fun saveArticle(article: Article) {
        val db = FirebaseFirestore.getInstance()

        val documentReference = if (article.articleId.isBlank()) {
            db.collection("articles").document()
        } else {
            db.collection("articles").document(article.articleId)
        }

        documentReference.set(article)
            .addOnSuccessListener {
                println("Article successfully saved!")
            }
            .addOnFailureListener { e ->
                println("Error saving article: ${e.message}")
            }
    }

    fun retrieveAllArticles(onSuccess: (List<Article>) -> Unit, onError: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val source = Source.SERVER

        db.collection("articles")
            .get(source)
            .addOnSuccessListener { querySnapshot ->
                val articles = querySnapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(Article::class.java)?.apply {
                            isDailyArticle = document.getBoolean("isDailyArticle") ?: false
                            backgroundColor = document.data?.get("backgroundColor")?.toString()?.toIntOrNull() ?: 0
                            Log.d("ArticleFetch", "Fetched: $articleTitle with daily status: $isDailyArticle")
                        }
                    } catch (e: Exception) {
                        Log.e("ArticleFetchError", "Error parsing article", e)
                        null
                    }
                }
                onSuccess(articles)
            }

            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
    fun getArticleById(articleId: String, onResult: (Article) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("articles").document(articleId).get()
            .addOnSuccessListener { documentSnapshot ->
                val article = documentSnapshot.toObject(Article::class.java)
                article?.let { onResult(it) }
            }
            .addOnFailureListener {
                Log.e("ArticleViewModel", "Error fetching article: ${it.message}")
            }
    }

    fun fetchAndCategorizeArticles(onSuccess: (List<Article>, List<Article>) -> Unit, onError: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("articles")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val allArticles = querySnapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(Article::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
                val dailyArticles = allArticles.filter { it.isDailyArticle == true }
                val nonDailyArticles = allArticles.filter { it.isDailyArticle == false }

                onSuccess(dailyArticles, nonDailyArticles)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

}