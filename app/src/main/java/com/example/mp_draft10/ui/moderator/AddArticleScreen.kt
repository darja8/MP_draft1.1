
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mp_draft10.classes.Article
import com.example.mp_draft10.classes.ArticleTag
import com.example.mp_draft10.firebase.database.ArticleViewModel
import com.example.mp_draft10.ui.articleImages
import com.example.mp_draft10.ui.articleTags
import com.example.mp_draft10.ui.textColorList
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddNewArticleScreen(articleViewModel: ArticleViewModel = hiltViewModel()) {
    var articleText by remember { mutableStateOf("") }
    var articleTitle by remember { mutableStateOf("") }
    val imageId by remember { mutableStateOf("") }
    val selectedTags = remember { mutableStateListOf<String>() }  // Active tags selected by the user
//    var selectedImageId by remember { mutableStateOf<Int?>(null) }
    var textColor by remember { mutableStateOf(Color.Black) }
    var cardBackgroundColor by remember { mutableStateOf(Color.White) }
    var showTextColorPicker by remember { mutableStateOf(false) }
    var showBackgroundColorPicker by remember { mutableStateOf(false) }
    var previewTextColor by remember { mutableStateOf(Color.Black) }
    var selectedTextColorIndex by remember { mutableIntStateOf(0) }
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }
    var isDailyArticle by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp))
                    .width(150.dp)
                    .height(250.dp)
                    .wrapContentWidth(),
            ) {
                Box {
                    // Use selectedImageIndex to retrieve the image resource ID from articleImages
                    selectedImageIndex?.let { index ->
                        val imageResId = articleImages.getOrNull(index)  // Safely retrieve the image ID using the index
                        imageResId?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = "Selected Article Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillWidth,
                                alignment = Alignment.BottomCenter
                            )
                        }
                    }
                    if (articleTitle.isNotEmpty()) {
                        Text(
                            text = articleTitle,
                            style = MaterialTheme.typography.headlineMedium.copy(color = previewTextColor, fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(8.dp),
                        )
                    }
                }
            }
        }

        item { 
            Spacer(modifier = Modifier.height(25.dp))
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                TextField(
                    value = articleTitle,
                    onValueChange = { articleTitle = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Article Title") },
                    textStyle = LocalTextStyle.current.copy(color = textColor)
                )

                IconButton(onClick = { showTextColorPicker = true }) {
                    Icon(
                        imageVector = Icons.Filled.ColorLens,
                        contentDescription = "Change Text Color",
                        tint =  previewTextColor
                    )
                }
            }

            ColorPickerDialog(
                showDialog = showTextColorPicker,
                onDismiss = { showTextColorPicker = false },
                onSelectColor = { index ->
                    previewTextColor = textColorList[index]
                    selectedTextColorIndex = index
                }
            )
        }

        item {
            TextField(
                value = articleText,
                onValueChange = { articleText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(bottom = 8.dp),
                placeholder = { Text("Enter article content here") }
            )
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Is this a daily article?")
                Switch(
                    checked = isDailyArticle,
                    onCheckedChange = {
                        Log.d("SwitchCheck", "Before change: $isDailyArticle to $it")
                        isDailyArticle = it
                        Log.d("SwitchCheck", "After change: $isDailyArticle")
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                )
            }
        }

        if (!isDailyArticle) {
            item {
                Text(text = "Select Tags")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    articleTags.forEach { tag ->
                        ArticleTagView(tagName = tag.tagName, isSelected = selectedTags.contains(tag.tagName)) { tagName ->
                            if (selectedTags.contains(tagName)) {
                                selectedTags.remove(tagName)
                            } else if (selectedTags.size < 3) {
                                selectedTags.add(tagName)
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(text = "Select Image")
            ImageSelectionRow(articleImages, selectedImageIndex) { imageId ->
                selectedImageIndex = imageId
            }
        }

        item {
            Button(
                onClick = {
                    // Check if an image index is selected
                    selectedImageIndex?.let { index ->
                        val newArticle = Article(
                            articleTitle = articleTitle,
                            articleId = FirebaseFirestore.getInstance().collection("articles").document().id,
                            articleText = articleText,
                            tags = selectedTags.map { ArticleTag(it) },
                            imageId = index,
                            titleColor = selectedTextColorIndex,
                            isDailyArticle = isDailyArticle
                        )
                        // Save the article
                        articleViewModel.saveArticle(newArticle)
                    }
                },
            ) {
                Text("Add Article")
            }
        }
    }
}

@Composable
fun ArticleTagView(tagName: String, isSelected: Boolean, onTagSelected: (String) -> Unit) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .clickable { onTagSelected(tagName) }
            .padding(4.dp)
    ) {
        Text(
            text = tagName,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun ImageSelectionRow(
    imageList: List<Int>,
    selectedImageIndex: Int?,
    onImageSelected: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        imageList.forEachIndexed { index, imageId ->
            val painter = painterResource(id = imageId)
            ImageItem(painter, selectedImageIndex == index) {
                onImageSelected(index)  // Pass the index instead of the imageId
            }
        }
    }
}

@Composable
fun ImageItem(
    painter: Painter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Image(
            painter = painter,
            contentDescription = "Article Image",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ColorPickerDialog(showDialog: Boolean, onDismiss: () -> Unit, onSelectColor: (Int) -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Choose Color")
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),  // Adjust number of columns based on desired grid size
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    itemsIndexed(textColorList) { index, color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                                .background(color)
                                .clickable {
                                    onSelectColor(index)
                                    onDismiss()
                                }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewArticle(){
    AddNewArticleScreen()
}
