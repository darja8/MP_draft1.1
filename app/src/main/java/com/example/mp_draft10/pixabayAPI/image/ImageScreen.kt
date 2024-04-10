//package com.example.mp_draft10.image
//
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import coil.compose.rememberImagePainter
//import com.example.mp_draft10.auth.util.Resource
//
//@Composable
//fun ImageScreen(viewModel: ImageViewModel) {
//    val images by viewModel.images.observeAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.searchImages("cats")
//    }
//
//    when (val result = images) {
//        is Resource.Success -> {
//            LazyColumn {
//                result.data?.let {
//                    items(it.hits) { item ->
//                        ImageItem(imageUrl = item.previewURL)
//                    }
//                }
//            }
//        }
//        is Resource.Loading -> CircularProgressIndicator()
//        is Resource.Error -> Text("Error: ${result.message}")
//        else -> {}
//    }
//}
//
//@Composable
//fun ImageItem(imageUrl: String) {
//    val painter = rememberImagePainter(data = imageUrl, builder = {
//        crossfade(true)
//        listener(onError = { _, throwable ->
//            Log.e("ImageLoading", "Error loading image", throwable)
//        })
//    })
//    Image(painter = painter, contentDescription = "Image description", modifier = Modifier.size(200.dp))
//}
//
//// Sample data to simulate API response
//val mockImageData = listOf(
//    "https://cdn.pixabay.com/photo/2022/09/20/22/58/sand-7468945_960_720.jpg",
//    "https://example.com/image2.jpg"
//    // Add more mock image URLs as needed
//)
//
//// A preview Composable that uses the mock data
//@Preview(showBackground = true)
//@Composable
//fun ImageScreenPreview() {
//    // Using LazyColumn to display a list of images
//    LazyColumn {
//        items(mockImageData) { imageUrl ->
//            ImageItem(imageUrl = imageUrl)
//        }
//    }
//}
//
