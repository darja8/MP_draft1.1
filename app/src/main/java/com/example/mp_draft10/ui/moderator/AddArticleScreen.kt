
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mp_draft10.classes.Article
import com.example.mp_draft10.classes.ArticleTag
import com.example.mp_draft10.firebase.database.ArticleViewModel
import com.example.mp_draft10.ui.articleTags
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddNewArticleScreen(articleViewModel: ArticleViewModel = hiltViewModel()) {
    var articleText by remember { mutableStateOf("") }
    var articleTitle by remember { mutableStateOf("") }
    val imageId by remember { mutableStateOf("") }
    val selectedTags = remember { mutableStateListOf<String>() }  // Active tags selected by the user

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(MaterialTheme.colorScheme.background)) {

        TextField(
            value = articleTitle,
            onValueChange = { articleTitle = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            placeholder = { Text("Article Title") }
        )

        TextField(
            value = articleText,
            onValueChange = { articleText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 8.dp),
            placeholder = { Text("Enter article content here") }
        )

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

        Text(text = "Select Image")

        Button(
            onClick = {
                // Create an Article object
                val newArticle = Article(
                    articleTitle = articleTitle,
                    articleId = FirebaseFirestore.getInstance().collection("articles").document().id,
                    articleText = articleText,
                    tags = selectedTags.map { ArticleTag(it) },  // Correctly referencing the selectedTags for ArticleTag conversion
                    imageId = imageId
                )
                // Save the article
                articleViewModel.saveArticle(newArticle)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Article")
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
            .padding(8.dp)
    ) {
        Text(
            text = tagName,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewArticle(){
    AddNewArticleScreen()
}
