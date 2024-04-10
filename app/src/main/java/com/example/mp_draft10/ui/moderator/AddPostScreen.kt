package com.example.mp_draft10.ui.moderator

import SearchScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.mp_draft10.data.entities.MappedImageItemModel
import com.example.mp_draft10.firebase.database.PostViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddNewPostScreen(navController: NavHostController, postViewModel: PostViewModel = hiltViewModel()) {
    var postText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var selectedImageItem by remember { mutableStateOf<MappedImageItemModel?>(null) }

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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            if (selectedImageItem != null){
                selectedImageItem?.let { imageItem ->
                    ImagePreviewSection(item = imageItem)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }else{
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

            Button(
                onClick = {
                    // Save the post logic here
                    if (postText.isNotBlank()) {
                        postViewModel.addPost(postText)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Save Post")
            }
        }
    }
}

@Composable
fun ImagePreviewSection(item: MappedImageItemModel) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(8.dp)) {
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