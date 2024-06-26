package com.example.mp_draft10.commons

import SearchScreen
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mp_draft10.data.entities.MappedImageItemModel
import com.example.mp_draft10.ui.moderator.searchImage.ImageDetailScreen
import com.example.mp_draft10.ui.moderator.searchImage.ImageSearchViewModel
import com.example.mp_draft10.ui.moderator.searchImage.ImageSearchWithDetailTwoPane


@Composable
fun Navigation(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: ImageSearchViewModel = hiltViewModel()
    val replyUiState = viewModel.uiState.collectAsState().value
    val navController = rememberNavController()

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            NavigationBuilder(viewModel, navController)
        }

        WindowWidthSizeClass.Medium -> {
            ImageSearchWithDetailTwoPane(viewModel, replyUiState)
        }

        WindowWidthSizeClass.Expanded -> {
            ImageSearchWithDetailTwoPane(viewModel, replyUiState)
        }

        else -> {
            NavigationBuilder(viewModel, navController)

        }
    }
}


@Composable
fun NavigationBuilder(viewModel: ImageSearchViewModel, navController: NavHostController) {

    NavHost(navController = navController, startDestination = Destinations.Home.path) {
        composable(Destinations.Home.path) {
            SearchScreen(viewModel, onImageClicked = { imageItem ->
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "imageItem",
                    value = imageItem
                )
                navController.navigate(Destinations.Details.path)
            })
        }

        composable(Destinations.Details.path) {
            val result =
                navController.previousBackStackEntry?.savedStateHandle?.get<MappedImageItemModel>("imageItem")
            result?.let { it1 ->
                ImageDetailScreen(result = it1) {
                    navController.navigateUp()
                }
            }
            /*
             val parentEntry = remember(backStackEntry) {
                 navController.getBackStackEntry(AppScreens.Start.name)
             }
            val parentViewModel = hiltViewModel<ImageSearchViewModel>(parentEntry)
            parentViewModel.uiState.collectAsState().value.currentImageNode?.let {
                 SearchImageDetails(it){
                     navController.navigateUp()
                 }
             }*/


        }

    }

}

sealed class Destinations(val path: String, val icon: ImageVector? = null) {

    object Home : Destinations("home")
    object Details : Destinations("details")

}

/*enum class AppScreens {
    Start,
    ImageDetail
}*/

/*
val dummyImageItem = com.example.mp_draft10.ui.moderator.ImageItem(
    webformatHeight = 426,
    imageWidth = 4752,
    previewHeight = 99,
    webformatURL = "https://pixabay.com/get/g62eef811a34bd9c15c9269ec772b3729e13b4c75a1578286df917a2397618f7c81e92e56163c97ff272a7e863e09f406_640.jpg",
    userImageURL = "https://cdn.pixabay.com/user/2012/03/08/00-13-48-597_250x250.jpg",
    previewURL = "https://cdn.pixabay.com/photo/2010/12/13/10/05/berries-2277_150.jpg",
    comments = 389,
    imageHeight = 3168,
    tags = "berries,fruits, food",
    previewWidth = 150,
    downloads = 502431,
    collections = 1780,
    largeImageURL = "https://pixabay.com/get/gd633530a25ff1a68c565f37e278c215b78650926392edf43841971bfc710c6dd449b49794fd4d786d7f7e926a6006695_1280.jpg",
    views = 944821,
    likes = 1889
)*/
