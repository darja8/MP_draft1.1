package com.example.mp_draft10.ui.moderator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mp_draft10.R
import com.example.mp_draft10.database.PostViewModel

@Composable
fun AddNewPostScreen(navController: NavHostController, postViewModel: PostViewModel = viewModel()) {
    var postText by remember { mutableStateOf("") }
    val imageList = listOf(
        // Make sure these resources exist in your drawable folder
        R.drawable.bee,
        R.drawable.butterfly,
        R.drawable.clover
    )
    var selectedImageIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Image and post text UI code remains the same

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Check if post text is not empty
                if (postText.isNotBlank()) {
                    postViewModel.addPost(postText)
                    // Optional: Navigate back or show a success message
                    navController.popBackStack() // Example of navigating back
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save Post")
        }
    }
}