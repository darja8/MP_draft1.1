package com.example.mp_draft10

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
import com.example.mp_draft10.auth.SignInScreen
import com.example.mp_draft10.auth.SignInViewModel
import com.example.mp_draft10.auth.SignUpScreen
import com.example.mp_draft10.auth.SignUpViewModel
import com.example.mp_draft10.ui.screens.ChatScreen
import com.example.mp_draft10.ui.screens.CreateAvatarScreen
import com.example.mp_draft10.ui.screens.InsightsScreen
import com.example.mp_draft10.ui.screens.PostDetailScreen
import com.example.mp_draft10.ui.screens.SettingsScreen
import com.example.mp_draft10.ui.theme.MP_draft10Theme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import drawable.TodayScreen

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
                    val navController = rememberNavController()
                    NavigationAuthentication(
                        navController = navController,
                        signUpViewModel = signUpViewModel,
                        signInViewModel = signInViewModel
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
    signInViewModel: SignInViewModel
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
            CreateAvatarScreen()
        }
        composable("postDetail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")
            if (postId != null) {
                PostDetailScreen(postId = postId, navController = navController, postViewModel = viewModel())
            }
        }
        composable(route = AppRoutes.TodayScreen.route){
            TodayScreen(navController = navController)
        }
        composable(route = AppRoutes.InsightsScreen.route){
            InsightsScreen(navController = navController)
        }
        composable(route = AppRoutes.HubScreen.route){
            ChatScreen(navController = navController)
        }
    }
}

sealed class AppRoutes(val route: String) {
    data object SignIn : AppRoutes("sign_in")
    data object SignUp : AppRoutes("sign_up")
    data object Main : AppRoutes("main")
    data object Settings : AppRoutes("settings")
    data object PostDetail : AppRoutes("postDetail/{postId}") {
        fun createRoute(postId: String) = "postDetail/$postId"
    }
    data object AvatarSetting : AppRoutes("avatarSetting")
    data object TodayScreen : AppRoutes ("today")
    data object InsightsScreen : AppRoutes ("insight")
    data object HubScreen : AppRoutes ("hub")
}

data class BottomNavItem(val route: String, val icon: Int, val title: String)
