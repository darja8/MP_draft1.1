package com.example.mp_draft10.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mp_draft10.classes.Article
import com.example.mp_draft10.firebase.database.AddNewUserViewModel
import com.example.mp_draft10.firebase.database.ArticleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun ArticleScreen(articleId: String, navController: NavHostController, articleViewModel: ArticleViewModel = hiltViewModel(), addNewUserViewModel: AddNewUserViewModel = hiltViewModel()) {
    val (article, setArticle) = remember { mutableStateOf<Article?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var userType by remember { mutableStateOf("") }

    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(userType) {
        userType = addNewUserViewModel.fetchUserType().toString()
    }
    LaunchedEffect(articleId) {
        articleViewModel.getArticleById(articleId) { fetchedArticle ->
            fetchedArticle?.let {
                setArticle(it)
                // Only update title and text when not in editing mode to avoid overwriting user input.
                if (!isEditing) {
                    title = it.articleTitle
                    text = it.articleText
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Article" else "Article Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (userType == "moderator") {  // Assuming userType is stored in the article or another accessible location
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    showMenu = false
                                    isEditing = !isEditing  // Toggle edit mode
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Remove") },
                                onClick = {
                                    showMenu = false
                                    articleViewModel.removeArticle(articleId)
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                if (isEditing && article != null) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Text") },
                        modifier = Modifier.fillMaxWidth().height(250.dp)
                    )
                    Button(
                        onClick = {
                            isEditing = false
                            // Update article with new text and title, and save it
                            val updatedArticle = article.copy(articleTitle = title, articleText = text)
                            articleViewModel.saveArticle(updatedArticle)
                        }
                    ) {
                        Text("Save")
                    }
                } else {
                    article?.let {
                        Text(
                            text = it.articleTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = it.articleText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } ?: Text("Loading article...")
                }
            }
        }
    }
}
