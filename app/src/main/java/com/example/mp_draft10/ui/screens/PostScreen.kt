package com.example.mp_draft10.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mp_draft10.database.AddNewUserViewModel
import com.example.mp_draft10.database.PostViewModel
import com.example.mp_draft10.ui.DisplaySavedAvatarAndColor
import com.google.firebase.auth.FirebaseAuth


@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    postId: String?,
    parentId: String? = null,
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
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        trailingIcon = {
            IconButton(onClick = {
                if (!commentText.isBlank() && currentUser != null && postId != null) {
                    val newComment = Comment(
                        userId = currentUser.uid,
                        text = commentText,
                        timestamp = System.currentTimeMillis(),
                        parentId = parentId // Include the parentId
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
    var showReplyDialog by remember { mutableStateOf(false) }
    var replyToCommentId by remember { mutableStateOf<String?>(null) }

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
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)) {
                post?.let {
                    PostDisplay(it, modifier = Modifier.padding(8.dp))
                }

                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
                if (showReplyDialog) {
                    AlertDialog(
                        onDismissRequest = { showReplyDialog = false },
                        title = { Text("Reply to Comment") },
                        text = {
                            CommentTextField(
                                postViewModel = postViewModel,
                                postId = postId,
                                parentId = replyToCommentId,
                            )
                        },
                        confirmButton = {},
                        dismissButton = { Button(onClick = { showReplyDialog = false }) { Text("Cancel") } }
                    )
                }
                LazyColumn {
                    items(comments) { comment ->
                        CommentBubble(
                            comment = comment,
                            viewModel = viewModel(),
                            onReplyClick = {
                                replyToCommentId = comment.commentId
                                showReplyDialog = true // Show the reply dialog
                            }
                        )
                    }
                }
            }

            CommentTextField(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                postViewModel = postViewModel,
                postId = post?.id,
                parentId = replyToCommentId // Pass the parentId for replies
            )

            // Reset replyToCommentId when the comment is posted
            if (replyToCommentId != null) {
                LaunchedEffect(key1 = replyToCommentId) {
                    // Reset after posting or canceling reply
                    replyToCommentId = null
                }
            }
        }
    } else {
        Text("Post not found", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CommentBubble(comment: Comment, viewModel: AddNewUserViewModel, onReplyClick: () -> Unit) {
    var avatarImageIndex by remember { mutableStateOf<Int?>(null) }
    var backgroundColorIndex by remember { mutableStateOf<Int?>(null) }

    // Fetch avatar indices based on the user email associated with the comment
    LaunchedEffect(comment.userId) {
        viewModel.fetchAvatarIndicesById(comment.userId) { bgIndex, avatarIndex ->
            backgroundColorIndex = bgIndex
            avatarImageIndex = avatarIndex
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium, // This provides the rounded corners
        // backgroundColor = MaterialTheme.colorScheme.secondaryContainer // Adjust the background color as needed
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Ensures the Row takes up the full width
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically // Centers children vertically within the Row
        ) {
            if (avatarImageIndex != null && backgroundColorIndex != null) {
                DisplaySavedAvatarAndColor(avatarImageIndex.toString(), backgroundColorIndex.toString(), 35)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = comment.text,
                    style = MaterialTheme.typography.bodyMedium,
                    // Make sure the text color contrasts well with the Card's background color
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Justify
                )
            }
            IconButton(onClick = onReplyClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Reply,
                    contentDescription = "Reply to Comment",
                    // Adjust the icon color to match your theme or to indicate interactability
                    tint = MaterialTheme.colorScheme.primary
                )
            }
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