package com.example.mp_draft10.ui.moderator

import SearchScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mp_draft10.classes.Post
import com.example.mp_draft10.data.entities.MappedImageItemModel
import com.example.mp_draft10.firebase.database.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

/**
 * This file defines the AddNewPostScreen, a Composable function within the moderator UI package.
 * It provides functionality for moderators to create and upload new posts to the community platform.
 * Users can enter text for the post and optionally add an image from a searchable gallery, which
 * utilizes a modal bottom sheet for image selection. The screen is built using Jetpack Compose,
 * leveraging Material Design components for modern and responsive UI elements.
 *
 * The process includes:
 * - A text field for entering post content.
 * - An image selection mechanism powered by a modal bottom sheet where users can pick an image.
 * - Buttons to add images and save the post, which then uploads the post data to Firestore using the PostViewModel.
 * - Use of Firebase Authentication to associate posts with the current user's ID.
 * - Navigation control to return to the previous screen or pop the back stack upon successful post submission.
 *
 * This screen is part of the community management tools designed to enhance content moderation and engagement.
 *
 * @author Daria Skrzypczk
 * @version 1.0
 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddNewPostScreen(navController: NavHostController, postViewModel: PostViewModel = hiltViewModel()) {
    var postText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var selectedImageItem by remember { mutableStateOf<MappedImageItemModel?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                SearchScreen(onImageClicked = { selectedImage ->
                    // Update the selectedImageItem with the selected image
                    selectedImageItem = selectedImage
                    coroutineScope.launch {
                        sheetState.hide() // Hide the bottom sheet
                    }
                })
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetElevation = 8.dp,
            sheetBackgroundColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                if (selectedImageItem != null) {
                    selectedImageItem?.let { imageItem ->
                        ImagePreviewSection(item = imageItem)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    Spacer(modifier = Modifier.height(200.dp))
                }
                TextField(
                    value = postText,
                    onValueChange = { postText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    placeholder = { Text("Enter post content here...") }
                )

                Button(
                    onClick = { coroutineScope.launch { sheetState.show() } },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Add Image")
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        if (postText.isNotBlank() && selectedImageItem != null) {

                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val userId = currentUser?.uid

                            val post = selectedImageItem!!.largeImageURL?.let {
                                Post(
                                    content = postText,
                                    imageUrl = it,
                                    imageId = selectedImageItem!!.previewURL.toString(), // Assuming `selectedImageItem` has an ID property
                                    userId = userId.toString()
                                )
                            }
                            if (post != null) {
                                postViewModel.savePostToFirestore(post)
                            }
                            coroutineScope.launch {
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Save Post")
                }
            }
        }
    }
}

@Composable
fun ImagePreviewSection(item: MappedImageItemModel) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(8.dp))
        {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.largeImageURL)
                .crossfade(true)
                .build(),
            contentDescription = "Selected image preview",
            modifier = Modifier.fillMaxSize()
        )
    }
}