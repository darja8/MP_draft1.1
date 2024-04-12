package com.example.mp_draft10.firebase.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mp_draft10.classes.Article
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    fun saveArticle(article: Article) {
        // Get the instance of the Firestore database
        val db = FirebaseFirestore.getInstance()

        // Prepare the document reference. If you want to create a new document with a unique ID each time, use:
        val documentReference = if (article.articleId.isBlank()) {
            db.collection("articles").document()
        } else {
            db.collection("articles").document(article.articleId)
        }

        // Set the data in Firestore
        documentReference.set(article)
            .addOnSuccessListener {
                // Handle success, perhaps by informing the user the save was successful
                println("Article successfully saved!")
            }
            .addOnFailureListener { e ->
                // Handle failure, such as by displaying an error message to the user
                println("Error saving article: ${e.message}")
            }
    }
}