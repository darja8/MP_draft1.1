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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    postId: String?
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
                        timestamp = System.currentTimeMillis()
                    )
                    postViewModel.addCommentToPost(postId, newComment)
                    commentText = ""
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Comment", tint = MaterialTheme.colorScheme.onBackground)
            }
        }
    )
}

@Composable
fun PostDetailScreen(postId: String, navController: NavController, postViewModel: PostViewModel = viewModel()) {
    var post by remember { mutableStateOf<Post?>(null) }
    val comments by postViewModel.comments.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

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

                Divider()

                LazyColumn {
                    items(comments) { comment ->
                        CommentBubble(
                            comment = comment,
                            viewModel = viewModel(),
                            postViewModel = postViewModel,
                            postId = postId
                        )
                        Column (modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)){
                            comment.replies.forEach { reply ->
                                ReplyBubble(reply = reply, viewModel = viewModel())
                            }
                        }

                    }
                }
            }
            CommentTextField(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                postViewModel = postViewModel,
                postId = post?.id,
            )
        }
    } else {
        Text("Post not found", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CommentBubble(comment: Comment, viewModel: AddNewUserViewModel, postViewModel: PostViewModel, postId: String) {
    var avatarImageIndex by remember { mutableStateOf<Int?>(null) }
    var backgroundColorIndex by remember { mutableStateOf<Int?>(null) }
    var isReplying by remember { mutableStateOf(false) } // State to manage reply mode
    var replyText by remember { mutableStateOf("") } // State to hold the reply text
    val focusManager = LocalFocusManager.current // To manage focus
    val currentUser = FirebaseAuth.getInstance().currentUser

    // This state and coroutine scope are used to request focus on the TextField when it becomes visible.
    val textFieldFocusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

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
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (avatarImageIndex != null && backgroundColorIndex != null) {
                    DisplaySavedAvatarAndColor(avatarImageIndex.toString(), backgroundColorIndex.toString(), 35)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = comment.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Start
                    )
                }

                IconButton(onClick = {
                    isReplying = !isReplying
                    // Reset reply text when opening the TextField
                    if (isReplying) replyText = ""
                    coroutineScope.launch {
                        if (isReplying) {
                            delay(100) // Small delay to ensure everything is ready
                            textFieldFocusRequester.requestFocus()
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Reply,
                        contentDescription = "Reply to Comment",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            // Conditionally display the TextField for reply
            if (isReplying) {
                TextField(
                    value = replyText,
                    onValueChange = { newText ->
                        replyText = newText // Update the reply text state with each change
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(textFieldFocusRequester),
                    placeholder = { Text("Write a reply...") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            if (!replyText.isBlank() && currentUser != null) {
                                val newReplyComment = ReplyComment(
                                    userId = currentUser.uid,
                                    text = replyText.trim(),
                                    timestamp = System.currentTimeMillis(),
                                    // Ensure `commentId` is initialized correctly in `ReplyComment` if needed.
                                )
                                postViewModel.addReplyToComment(postId, comment.commentId, newReplyComment)
                                replyText = "" // Resetting reply text
                                focusManager.clearFocus() // Clearing focus to hide the keyboard
                                isReplying = false // Hide the TextField by toggling `isReplying` to false
                                // Optionally, show a toast or feedback message here
                            }
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Reply",tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ReplyBubble(reply: ReplyComment, viewModel: AddNewUserViewModel) {
    var avatarImageIndex by remember { mutableStateOf<Int?>(null) }
    var backgroundColorIndex by remember { mutableStateOf<Int?>(null) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    LaunchedEffect(reply.userId) {
        viewModel.fetchAvatarIndicesById(reply.userId) { bgIndex, avatarIndex ->
            backgroundColorIndex = bgIndex
            avatarImageIndex = avatarIndex
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f) // Fill 90% of the width
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if ((avatarImageIndex != null) && (backgroundColorIndex != null)) {
                    DisplaySavedAvatarAndColor(avatarImageIndex.toString(), backgroundColorIndex.toString(), 30)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = reply.text,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
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

    MaterialTheme {
        PostDisplay(post = samplePost, modifier = Modifier.padding(1.dp))
    }
}
