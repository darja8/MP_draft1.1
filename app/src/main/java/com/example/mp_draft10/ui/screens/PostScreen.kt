package com.example.mp_draft10.ui.screens
import androidx.compose.foundation.Image
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mp_draft10.R
import com.example.mp_draft10.classes.Comment
import com.example.mp_draft10.classes.Post
import com.example.mp_draft10.classes.ReplyComment
import com.example.mp_draft10.firebase.database.AddNewUserViewModel
import com.example.mp_draft10.firebase.database.PostViewModel
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
                if (commentText.isNotBlank() && currentUser != null && postId != null) {
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
fun PostDetailScreen(postId: String, addNewUserViewModel: AddNewUserViewModel = viewModel(), postViewModel: PostViewModel = viewModel()) {
    var post by remember { mutableStateOf<Post?>(null) }
    val comments by postViewModel.comments.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var usertype by remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isLiked = remember(post) { mutableStateOf(post?.likes?.contains(FirebaseAuth.getInstance().currentUser?.uid) ?: false) }
    val isLikedColor = if (isLiked.value) Color.Red else Color.White // Using Gray as default

    LaunchedEffect(postId) {
        isLoading = true
        postViewModel.fetchCommentsForPost(postId)
        postViewModel.fetchPostById(postId) { fetchedPost ->
            post = fetchedPost
            isLoading = false
        }
        usertype = addNewUserViewModel.fetchUserType().toString()
    }

    if (isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else if (post != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            post!!.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .error(R.drawable.backgroundpost1)
                        .build(),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.Black.copy(alpha = 0.6f))

                )
                Text(
                    text = post!!.content,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Box(modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 130.dp)
                    .padding(16.dp)) {
                    IconButton(onClick = {
                        currentUser?.uid?.let { userId ->
                            if (isLiked != null) {
                                isLiked.value = !isLiked.value
                            } // Toggle the like state
                            postViewModel.toggleLikePost(postId, userId)
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

            LazyColumn(modifier = Modifier.padding(top = 200.dp)) {
                items(comments) { comment ->
                    CommentBubble(
                        comment = comment,
                        viewModel = viewModel(),
                        postViewModel = postViewModel,
                        postId = postId,
                        userType = usertype
                    )
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
fun CommentBubble(comment: Comment, viewModel: AddNewUserViewModel, postViewModel: PostViewModel, postId: String, userType: String) {
    var avatarImageIndex by remember { mutableStateOf<Int?>(null) }
    var backgroundColorIndex by remember { mutableStateOf<Int?>(null) }
    var isReplying by remember { mutableStateOf(false) } // State to manage reply mode
    var replyText by remember { mutableStateOf("") } // State to hold the reply text
    val focusManager = LocalFocusManager.current // To manage focus
    val currentUser = FirebaseAuth.getInstance().currentUser

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
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 6.dp),
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
                if ((userType == "moderator") || (currentUser?.uid == comment.userId)) {
                    IconButton(onClick = {
                        postViewModel.removeComment(postId, comment.commentId)
                    }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            if (isReplying) {
                TextField(
                    value = replyText,
                    onValueChange = { newText ->
                        replyText = newText
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
                                )
                                postViewModel.addReplyToComment(postId, comment.commentId, newReplyComment)
                                replyText = ""
                                focusManager.clearFocus()
                                isReplying = false
                            }
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Reply",tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        comment.replies.forEach { reply ->
            ReplyBubble(
                reply = reply,
                viewModel = viewModel,
                postViewModel = postViewModel,
                userType = userType,
                postId = postId,
                commentId = comment.commentId
            )
        }
    }
}

@Composable
fun ReplyBubble(
    reply: ReplyComment,
    viewModel: AddNewUserViewModel,
    postViewModel: PostViewModel,
    userType: String,
    postId: String,
    commentId: String
){
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
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if ((avatarImageIndex != null) && (backgroundColorIndex != null)) {
                    DisplaySavedAvatarAndColor(
                        avatarImageIndex.toString(),
                        backgroundColorIndex.toString(),
                        30
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = reply.text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                if (userType == "moderator" || currentUser?.uid == reply.userId) {
                    IconButton(onClick = {
                        postViewModel.removeReplyFromComment(postId,commentId,reply.replyId)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PostDisplay(post: Post, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageName = "backgroundpost${post.id}"
    val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(color = MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (post.id != "0") {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null, // Description not needed for background images
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Scale the image to fill the size of the Box, cropping if necessary
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.8f))
        )
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