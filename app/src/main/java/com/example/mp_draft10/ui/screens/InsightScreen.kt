package com.example.mp_draft10.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mp_draft10.AppRoutes
import com.example.mp_draft10.classes.Article
import com.example.mp_draft10.classes.MoodData
import com.example.mp_draft10.firebase.database.AddNewUserViewModel
import com.example.mp_draft10.firebase.database.ArticleViewModel
import com.example.mp_draft10.ui.articleImages
import com.example.mp_draft10.ui.components.MainScreenScaffold
import com.example.mp_draft10.ui.textColorList
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * The InsightsScreen Composable function is designed to provide users with an analytical view of their mood trends
 * over different time frames and to display categorized articles, built with Jetpack Compose.
 * It uses Firebase for real-time data fetching and updates, and Hilt for dependency injection.
 *
 * Features:
 * - Dynamically displays an animated average mood rating based on the last 30 days' mood data.
 * - Lists articles categorized into 'Daily' and 'Non-Daily' for easy navigation and reading.
 * - Allows users to view detailed articles by tapping on individual cards in a horizontally scrollable row.
 * - Integrates ViewModel to manage the retrieval and updating of mood data and articles, ensuring UI consistency and reliability.
 *
 * Requirements:
 * - Android OS 'UPSIDE_DOWN_CAKE' or newer is required due to specific API usages.
 * - Firebase setup must be correctly configured to handle data retrieval and updates securely and efficiently.
 * - Ensure that all network operations are performed asynchronously to maintain UI responsiveness and prevent blocking the main thread.
 *
 * The screen is structured to foster user engagement through interactive elements and visually appealing content presentation,
 * enhancing the user experience by providing useful insights and relevant reading materials.
 *
 * @author Daria Skrzypczak (jus27)
 * @version 1.0
 */


@SuppressLint("MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun InsightsScreen(
    addNewUserViewModel: AddNewUserViewModel = viewModel(),
    navController: NavHostController,
    articleViewModel: ArticleViewModel = hiltViewModel()
) {
    var moodRatingsMap by remember { mutableStateOf<Map<LocalDate, Int>>(emptyMap()) }
    var moodAndSymptomsList by remember { mutableStateOf<List<String>>(emptyList()) }
    var moodRatingAverage by remember { mutableStateOf<Map<LocalDate, Int>>(emptyMap()) }
    var articlesList by remember { mutableStateOf<List<Article>>(emptyList()) }
    var dailyArticlesList by remember { mutableStateOf<List<Article>>(emptyList()) }
    var nonDailyArticlesList by remember { mutableStateOf<List<Article>>(emptyList()) }
    var moodDataToday by remember { mutableStateOf<MoodData?>(null) }
    var filteredNonDailyArticles by remember { mutableStateOf<List<Article>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        moodDataToday = addNewUserViewModel.fetchMoodDataFromSpecificDay(LocalDate.now())

        articleViewModel.fetchAndCategorizeArticles(
            onSuccess = { daily, nonDaily ->
                dailyArticlesList = daily
                nonDailyArticlesList = nonDaily
                moodDataToday?.let { mood ->
                    // Ensure filtering happens after both mood data and article lists are updated
                    filteredNonDailyArticles = filterArticlesByMood(nonDaily, mood)
                }
            },
            onError = { exception ->
                println("Error retrieving articles: ${exception.message}")
            }
        )
    }


    LaunchedEffect(key1 = "moodRatings") {
        moodRatingsMap = addNewUserViewModel.fetchMoodRatingsForPast7Days()
    }

    LaunchedEffect(Unit) {
        moodRatingAverage = addNewUserViewModel.fetchMoodRatingsForPast30Days()
    }

    LaunchedEffect(moodDataToday) {
        moodDataToday?.let { mood ->
            filteredNonDailyArticles = filterArticlesByMood(nonDailyArticlesList, mood)
        }
    }

    var numberOfRatings = 0
    var total = 0

    for (item in moodRatingAverage.values){
        total += item
        numberOfRatings += 1
    }

    var averageRating = 0f

    if (numberOfRatings != 0) {
        averageRating = total.toFloat() / numberOfRatings
    }

    MainScreenScaffold(navController = navController)
    {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp) // This applies rounded corners to the Card
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.inversePrimary)
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedNumberText(averageRating)
                    }
                }
            }
            item{
                if (filteredNonDailyArticles.isNotEmpty()){
                    Text("Just for you", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    ArticlesList(filteredNonDailyArticles, navController)
                }
                Text("Daily Articles", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                ArticlesList(dailyArticlesList, navController)

            }
        }
    }
}

@Composable
fun AnimatedNumberText(targetValue: Float, animationDuration: Int = 700) {
    val animatedValue = remember { Animatable(0f) }

    LaunchedEffect(targetValue) {
        launch {
            animatedValue.animateTo(
                targetValue = targetValue,
                animationSpec = tween(durationMillis = animationDuration)
            )
        }
    }

    val animatedNumber by animatedValue.asState()

    Text(text = "Average mood rating for past 7 days", color = Color.White)
    Text(text = String.format("%.2f", animatedNumber), color = Color.White, fontSize = 45.sp)
}

@Composable
fun ArticlesList(articles: List<Article>, navController: NavHostController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(articles.size) { index ->
            ArticleCard(article = articles[index], navController)
        }
    }
}

@Composable
fun ArticleCard(article: Article, navController: NavHostController) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp)
            )
            .width(150.dp)
            .height(250.dp)
            .wrapContentWidth()
            .clickable {
                navController.navigate(AppRoutes.ArticleScreen.createRoute(article.articleId))
            },
    ) {
        Box {
            Image(
                painter = painterResource(id = articleImages[article.imageId]),
                contentDescription = "Article Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomCenter
            )
            if (article.articleTitle.isNotEmpty()) {
                Text(
                    text = article.articleTitle,
                    style = MaterialTheme.typography.headlineMedium.copy(color = textColorList[article.titleColor], fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(8.dp),
                )
            }
        }
    }
}

fun filterArticlesByMood(articles: List<Article>, mood: MoodData): List<Article> {
    return articles.filter { article ->
        article.tags.any { tag ->
            mood.moodObjects.contains(tag.tagName)
        }
    }
}