package com.example.mp_draft10.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mp_draft10.database.PostViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    postId: String?,
) {
    var commentText by remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser

    TextField(
        value = commentText,
        onValueChange = { commentText = it },
        placeholder = { Text("Write a comment...") },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        modifier = modifier.fillMaxWidth().padding(16.dp),
        trailingIcon = {
            IconButton(onClick = {
                if (!commentText.isBlank() && currentUser != null && postId != null) {
                    // Construct the comment with the current text, user ID, and a timestamp
                    val newComment = Comment(
                        userId = currentUser.uid,
                        text = commentText,
                        timestamp = System.currentTimeMillis()
                    )
                    // Pass the new comment to the ViewModel to be added to the post
                    postViewModel.addCommentToPost(postId, newComment)
                    commentText = "" // Clear the input field after submitting
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Comment")
            }
        }
    )
}

@Composable
fun PostDetailScreen(postId: String, navController: NavController, postViewModel: PostViewModel = viewModel()) {
    var post by remember { mutableStateOf<Post?>(null) }
    val comments by postViewModel.comments.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(postId) {
        isLoading = true
        postViewModel.fetchCommentsForPost(postId)
        postViewModel.fetchPostById(postId)?.let {
            post = it
        }
        isLoading = false
    }

    if (isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else if (post != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(bottom = 60.dp)) {
                post?.let {
                    PostDisplay(it, modifier = Modifier.padding(8.dp))
                }

                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
                LazyColumn {
                    items(comments) { comment ->
                        CommentBubble(comment = comment)
                    }
                }
            }

            CommentTextField(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                postViewModel = postViewModel,
                postId = post?.id
            )
        }
    } else {
        Text("Post not found", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CommentBubble(comment: Comment) {
    Card(
        colors = CardColors(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            Color.Gray,Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium // This provides the rounded corners
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.text,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun PostDisplay(post: Post, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(),
            verticalArrangement = Arrangement.Center ) {
            Text(
                text = post.content,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostDisplay() {
    // Define a sample Post object
    val samplePost = Post(
        content = "Break up. How to deal with emotions"
    )

    // Apply MaterialTheme for consistent styling
    MaterialTheme {
        PostDisplay(post = samplePost, modifier = Modifier.padding(1.dp))
    }
}