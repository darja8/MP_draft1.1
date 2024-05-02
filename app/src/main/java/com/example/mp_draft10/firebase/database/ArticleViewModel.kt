package com.example.mp_draft10.firebase.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.mp_draft10.classes.Article
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * The ArticleViewModel class is responsible for managing article-related data operations with Firestore.
 * It provides functionality to save, retrieve, and delete articles, as well as categorize them into daily and non-daily articles.
 * Key operations include:
 * - Saving articles to Firestore, either creating new entries or updating existing ones based on a unique article ID.
 * - Retrieving all articles from Firestore, with additional handling to fetch articles based on their publication status (daily or non-daily).
 * - Fetching a single article by ID, useful for detailed article views.
 * - Removing articles by ID, supporting content management needs.
 * This ViewModel uses Hilt for dependency injection, ensuring that Firestore instances are efficiently managed and the Android lifecycle is respected.
 */



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

    fun removeArticle(articleId: String) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the article document
        val articleRef = db.collection("articles").document(articleId)

        articleRef.delete()
            .addOnSuccessListener {
                Log.d("ArticleViewModel", "Article successfully removed")
            }
            .addOnFailureListener { exception ->
                Log.e("ArticleViewModel", "Error removing article: ${exception.message}")
            }
    }

}