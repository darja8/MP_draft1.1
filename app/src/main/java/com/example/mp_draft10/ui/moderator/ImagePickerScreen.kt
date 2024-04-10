package com.example.mp_draft10.ui.moderator

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.mp_draft10.Constants.GRID_SPAN_COUNT
import com.example.mp_draft10.auth.util.Resource
import com.example.mp_draft10.database.PostViewModel

//@Composable
//fun ImagePickerScreen(
//    viewModel: PostViewModel,
//    navController: NavController
//) {
//    // Observe the LiveData object from your ViewModel
//    val imageResponse by viewModel.images.observeAsState()
//
//    imageResponse?.getContentIfNotHandled()?.let { event ->
//        when (val response = event) {
//            is Resource.Success -> {
//                response.data?.let { data ->
//                    // Now you can use data to display images or handle success state
//                }
//            }
//            is Resource.Error -> {
//                // Handle error state
//                Text("Error loading images")
//            }
//            is Resource.Loading -> {
//                // Show a loading indicator
//                CircularProgressIndicator()
//            }
//        }
//    }
//}
//
//@Composable
//fun <ImageItem> ImageGrid(images: List<ImageItem>, onImageSelect: (String) -> Unit) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(GRID_SPAN_COUNT),
//        contentPadding = PaddingValues(8.dp)
//    ) {
//        items(images.size) { index ->
//            val image = images[index]
//            ImageItem(imageUrl = image.url, onImageClick = { onImageSelect(image.url) })
//        }
//    }
//}
//
//@Composable
//fun ImageList(images: List<String>, onImageClick: (String) -> Unit) {
//    LazyColumn { // or LazyVerticalGrid for a grid layout
//        items(images) { imageUrl ->
//            ImageItem(imageUrl = imageUrl, onImageClick = onImageClick)
//        }
//    }
//}
//
//@Composable
//fun ImageItem(imageUrl: String, onImageClick: (String) -> Unit) {
//    Box(modifier = Modifier
//        .padding(4.dp)
//        .clickable { onImageClick(imageUrl) }) {
//        Image(
//            painter = rememberImagePainter(imageUrl),
//            contentDescription = "Loaded image",
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}