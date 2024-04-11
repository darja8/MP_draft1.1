package com.example.mp_draft10.ui.screens
//import androidx.compose.material.Icon
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mp_draft10.AppRoutes
import com.example.mp_draft10.R
import com.example.mp_draft10.classes.Post
import com.example.mp_draft10.firebase.database.AddNewUserViewModel
import com.example.mp_draft10.firebase.database.PostViewModel
import com.example.mp_draft10.ui.components.MainScreenScaffold
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun ChatScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = viewModel(),
    addNewUserViewModel: AddNewUserViewModel = viewModel()
) {

    val posts by postViewModel.posts.collectAsState()
    var usertype by remember { mutableStateOf("") }

    // Diagnostic log to verify userType is fetched correctly
    LaunchedEffect(key1 = true) {
        postViewModel.fetchAllPosts()
        usertype = addNewUserViewModel.fetchUserType().toString()
        Log.d("ChatScreen", "User type: $usertype")
    }

    val showFab = usertype == "moderator"
    MainScreenScaffold(
        navController = navController,
        fab = if (showFab) {
            {
                FloatingActionButton(onClick = { navController.navigate(AppRoutes.AddPostScreen.route) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        } else null,
    )
    {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(posts) { post ->
                PostCard(post = post, navController, postViewModel, addNewUserViewModel)
            }
            item{
                Spacer(modifier = Modifier.padding(24.dp))
            }
        }
    }
}

@Composable
fun PostCard(post: Post, navController: NavController, postViewModel: PostViewModel, addNewUserViewModel: AddNewUserViewModel) {

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    val isLiked = remember { mutableStateOf(post.likes.contains(currentUser?.uid)) }
    val isLikedColor = if (isLiked.value) Color.Red else Color.White


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.imageUrl)
                    .crossfade(true)
                    .error(R.drawable.backgroundpost1)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            // Overlay for readability
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.6f))

            )
            // Text centered at the top
            Text(
                text = post.content,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White, // Ensure text color contrasts with the dark fade
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
            // Icons at the bottom-left corner
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                IconButton(onClick = {navController.navigate(AppRoutes.PostDetail.createRoute(post.id)) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.comment_text_outline),
                        contentDescription = "Comments",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                IconButton(onClick = {
                    currentUser?.uid?.let { userId ->
                        isLiked.value = !isLiked.value // Toggle locally for immediate UI update
                        postViewModel.toggleLikePost(post.id, userId)
                    }
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.heart),
                        contentDescription = "Likes",
                        tint = isLikedColor,
                    )
                }
            }
        }
    }
}