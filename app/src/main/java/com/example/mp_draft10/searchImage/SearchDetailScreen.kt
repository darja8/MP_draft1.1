package com.example.mp_draft10.searchImage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mp_draft10.R
import com.example.mp_draft10.commons.ImageDownloader
import com.example.mp_draft10.data.entities.MappedImageItemModel


@Composable
internal fun ImageDetailScreen(isTwoPane:Int = 0, result: MappedImageItemModel, onBackClicked: () -> Unit) {
    val downloadManager = ImageDownloader(LocalContext.current)

    DetailScreenContent(
        modifier = Modifier.fillMaxSize(),
        item = result,
        onBackBtnClicked = onBackClicked,
        onDownloadBtnClicked = { imageUrl ->
            downloadManager.downloadFile(imageUrl)
        },
        isTwoPane
    )
}

@Composable
internal fun DetailScreenContent(
    modifier: Modifier = Modifier,
    item: MappedImageItemModel,
    onBackBtnClicked: () -> Unit,
    onDownloadBtnClicked: (String) -> Unit,
    isTwoPane: Int
) {

    Surface(modifier = modifier) {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current).data(item.largeImageURL)
                .crossfade(true).build(),
            contentDescription = item.user,
            contentScale = ContentScale.Fit
        )

        Column(
            Modifier
                .fillMaxSize()
        ) {

            if(isTwoPane==0){
                BackButton(
                    onBackClicked = onBackBtnClicked
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            val color = Color.Black.copy(0.2f)
            DetailBottomCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color),
                item = item,
                onDownloadClicked = onDownloadBtnClicked
            )
        }
    }


}

@Composable
fun BackButton(
    modifier: Modifier = Modifier, onBackClicked: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onBackClicked() }) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Favorite",
            tint = MaterialTheme.colorScheme.primary
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomCard(
    modifier: Modifier = Modifier,
    item: MappedImageItemModel,
    onDownloadClicked: (String) -> Unit
) {


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BadgedBox(
            modifier = Modifier.padding(8.dp),
            badge = {
                Badge {
                    val badgeNumber = item.likes
                    Text(badgeNumber)
                }
            }) {
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = stringResource(R.string.label_image_likes),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        BadgedBox(
            modifier = Modifier.padding(8.dp),
            badge = {
                Badge {
                    val badgeNumber = item.comments
                    Text(
                        badgeNumber,
                        modifier = Modifier.semantics {
                            contentDescription = "$badgeNumber new notifications"
                        }
                    )
                }
            }) {
            Icon(
                Icons.Outlined.Comment,
                contentDescription = "Favorite",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        BadgedBox(
            modifier = Modifier.padding(8.dp),
            badge = {
                Badge {
                    val badgeNumber = item.views
                    Text(
                        badgeNumber,
                        modifier = Modifier.semantics {
                            contentDescription = "$badgeNumber new notifications"
                        }
                    )
                }
            }) {
            Icon(
                Icons.Outlined.Person,
                contentDescription = "Favorite",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            modifier = Modifier.padding(8.dp),
            onClick = {
                onDownloadClicked(item.largeImageURL.toString())

            }
        ) {
            Icon(
                Icons.Filled.Download,
                contentDescription = stringResource(R.string.label_download_image),
                tint = Color.White
            )
        }


    }

}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MaterialTheme {
        Surface {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageDetailScreenPreview() {
    // Creating a mock instance of MappedImageItemModel with sample data
    val mockImageItem = MappedImageItemModel(
        imageId = 12345L,
        user = "SampleUser",
        url = "https://example.com/sample-image.jpg",
        likes = "250",
        downloads = "150",
        comments = "75",
        views = "1000",
        tags = listOf("Nature", "Forest", "River"),
        largeImageURL = "https://example.com/large-sample-image.jpg",
        previewURL = "https://example.com/preview-sample-image.jpg",
        userImageURL = "https://example.com/user-sample-image.jpg"
    )

    // Applying Material Theme for consistency with the app's theme
    MaterialTheme {
        // Passing the mock data and a no-op for onBackClicked
        ImageDetailScreen(
            isTwoPane = 0,
            result = mockImageItem,
            onBackClicked = {}
        )
    }
}