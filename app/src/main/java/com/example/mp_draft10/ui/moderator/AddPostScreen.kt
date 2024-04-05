package com.example.mp_draft10.ui.moderator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mp_draft10.R
import com.example.mp_draft10.database.PostViewModel
import com.example.mp_draft10.ui.screens.PostCard

@Composable
fun ModeratorDashboard(navController: NavHostController, postViewModel: PostViewModel = viewModel()){
    val posts by postViewModel.posts.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Add, "Floating action button.")

            }
        }

        items(posts) { post ->
            PostCard(post = post, navController)
        }
        item{
            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}
@Composable
fun AddNewPostScreen(navController: NavHostController) {
    var postText by remember { mutableStateOf("") }
    val imageList = listOf(
        R.drawable.bee, // Ensure these resources are present in your drawable folder
        R.drawable.butterfly,
        R.drawable.clover
    )
    var selectedImageIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Post Preview at the top
        if (selectedImageIndex >= 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageList[selectedImageIndex]),
                    contentDescription = "Post Image Preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Crop the image if not fitting the Box
                )
                Text(
                    text = postText,
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        TextField(
            value = postText,
            onValueChange = { postText = it },
            label = { Text("Post Text") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Choose an Image:", style = MaterialTheme.typography.bodyMedium)

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            itemsIndexed(imageList) { index, image ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(1f)
                        .border(
                            width = 2.dp,
                            color = if (selectedImageIndex == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { selectedImageIndex = index }
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "Selected Image"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Implement your save logic here */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save Post")
        }
    }
}

@Preview(showBackground = true, name = "Add New Post Screen Preview")
@Composable
fun AddNewPostScreenPreview() {
    // Create a dummy NavHostController for preview purposes
    val dummyNavController = rememberNavController()

    // Your theme wrapper here, if you have one. For simplicity, it's omitted.
    // For example, if you're using a MaterialTheme, wrap the AddNewPostScreen call with it.
    AddNewPostScreen(navController = dummyNavController)
}
