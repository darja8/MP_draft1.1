package com.example.mp_draft10

import SearchScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mp_draft10.data.entities.MappedImageItemModel
import com.example.mp_draft10.firebase.auth.ResetPasswordScreen
import com.example.mp_draft10.firebase.auth.SignInScreen
import com.example.mp_draft10.firebase.auth.SignInViewModel
import com.example.mp_draft10.firebase.auth.SignUpScreen
import com.example.mp_draft10.firebase.auth.SignUpViewModel
import com.example.mp_draft10.ui.moderator.AddNewArticleScreen
import com.example.mp_draft10.ui.moderator.AddNewPostScreen
import com.example.mp_draft10.ui.moderator.searchImage.ImageDetailScreen
import com.example.mp_draft10.ui.moderator.searchImage.ImageSearchViewModel
import com.example.mp_draft10.ui.screens.ArticleScreen
import com.example.mp_draft10.ui.screens.CreateAvatarScreen
import com.example.mp_draft10.ui.screens.HubScreen
import com.example.mp_draft10.ui.screens.InsightsScreen
import com.example.mp_draft10.ui.screens.PostDetailScreen
import com.example.mp_draft10.ui.screens.SettingsScreen
import com.example.mp_draft10.ui.screens.TodayScreen
import com.example.mp_draft10.ui.theme.MP_draft10Theme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MP_draft10Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val signUpViewModel: SignUpViewModel = hiltViewModel()
                    val signInViewModel: SignInViewModel = hiltViewModel()
                    val viewModel: ImageSearchViewModel = hiltViewModel()

                    val navController = rememberNavController()
                    NavigationAuthentication(
                        navController = navController,
                        signUpViewModel = signUpViewModel,
                        signInViewModel = signInViewModel,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavigationAuthentication(
    navController: NavHostController = rememberNavController(),
    signUpViewModel: SignUpViewModel,
    signInViewModel: SignInViewModel,
    viewModel: ImageSearchViewModel,
) {
    val auth = FirebaseAuth.getInstance()
    val isUserAuthenticated = remember { mutableStateOf(auth.currentUser != null) }

    DisposableEffect(key1 = auth) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            isUserAuthenticated.value = firebaseAuth.currentUser != null
        }
        auth.addAuthStateListener(listener)
        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    val startDestination = if (isUserAuthenticated.value) {
            AppRoutes.TodayScreen.route
    } else {
        AppRoutes.SignIn.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppRoutes.SignIn.route) {
            SignInScreen(navController = navController, viewModel = signInViewModel)
        }
        composable(route = AppRoutes.SignUp.route) {
            SignUpScreen(navController = navController, viewModel = signUpViewModel)
        }
        composable(route = AppRoutes.Settings.route){
            SettingsScreen(navController = navController)
        }
        composable(route = AppRoutes.AvatarSetting.route){
            CreateAvatarScreen(navController = navController)
        }
        composable("postDetail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")
            if (postId != null) {
                PostDetailScreen(postId = postId,navController, postViewModel = viewModel())
            }
        }
        composable(route = AppRoutes.TodayScreen.route){
            TodayScreen(navController = navController)
        }
        composable(route = AppRoutes.InsightsScreen.route){
            InsightsScreen(navController = navController)
        }
        composable(route = AppRoutes.HubScreen.route){
            HubScreen(navController = navController)
        }
        composable(route = AppRoutes.AddPostScreen.route){
            AddNewPostScreen(navController = navController)
        }
        composable(AppRoutes.SearchImage.route) {
            SearchScreen(viewModel, onImageClicked = { imageItem ->
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "imageItem",
                    value = imageItem
                )
                navController.navigate(AppRoutes.Details.route)
            })
        }
        composable(AppRoutes.Details.route) {
            val result =
                navController.previousBackStackEntry?.savedStateHandle?.get<MappedImageItemModel>("imageItem")
            result?.let { it1 ->
                ImageDetailScreen(result = it1) {
                    navController.navigateUp()
                }
            }
        }
        composable(AppRoutes.AddArticleScreen.route){
            AddNewArticleScreen(navController)
        }
        composable(AppRoutes.ArticleScreen.route){ backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")
            if (articleId != null) {
                ArticleScreen(articleId, navController)
            }
        }
        composable(AppRoutes.ResetPasswordScreen.route){
            ResetPasswordScreen(navController, signInViewModel)
        }
    }
}

sealed class AppRoutes(val route: String) {
    data object SignIn : AppRoutes("sign_in")
    data object SignUp : AppRoutes("sign_up")
    data object ResetPasswordScreen: AppRoutes("resetPassword")
    data object Settings : AppRoutes("settings")
    data object PostDetail : AppRoutes("postDetail/{postId}") {
        fun createRoute(postId: String) = "postDetail/$postId"
    }
    data object AvatarSetting : AppRoutes("avatarSetting")
    data object TodayScreen : AppRoutes ("today")
    data object InsightsScreen : AppRoutes ("insight")
    data object HubScreen : AppRoutes ("hub")
    data object AddPostScreen: AppRoutes ("add_post")
    data object SearchImage: AppRoutes ("search_image")
    data object Details : AppRoutes("details")
    data object AddArticleScreen: AppRoutes("add_article")
    data object ArticleScreen: AppRoutes ("article/{articleId}"){
        fun createRoute(articleId: String) = "article/$articleId"}
}

data class BottomNavItem(val route: String, val icon: Int, val title: String)
