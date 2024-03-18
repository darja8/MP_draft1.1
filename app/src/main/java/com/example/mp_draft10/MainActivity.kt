package com.example.mp_draft10

//import com.example.mp_draft10.ui.screens.PostDetailScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mp_draft10.auth.SignInScreen
import com.example.mp_draft10.auth.SignInViewModel
import com.example.mp_draft10.auth.SignUpScreen
import com.example.mp_draft10.auth.SignUpViewModel
import com.example.mp_draft10.ui.components.BottomNavigationBar
import com.example.mp_draft10.ui.screens.ChatScreen
import com.example.mp_draft10.ui.screens.InsightsScreen
import com.example.mp_draft10.ui.screens.PostDetailScreen
import com.example.mp_draft10.ui.screens.SettingsScreen
import com.example.mp_draft10.ui.theme.MP_draft10Theme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import drawable.TodayScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MP_draft10Theme {
                val db = Firebase.firestore
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }

}
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun BuildNavigationGraph() {
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

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavBarNavigation(navController = navController)
        }
    }
}

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    data object Today : NavigationItem("today", R.drawable.calendar, "Today")
    data object Insights : NavigationItem("insights", R.drawable.chart, "Insights")
    data object Chat : NavigationItem("chat", R.drawable.chat, "Chat")
}

fun NavController.navigateTo(route: AppRoutes) {
    navigate(route.route)
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavigationAuthentication(
    navController: NavHostController = rememberNavController(),
    signUpViewModel: SignUpViewModel,
    signInViewModel: SignInViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SignUp.route // Start with SignUpScreen by default
    ) {
        composable(route = AppRoutes.SignIn.route) {
            SignInScreen(navController = navController, viewModel = signInViewModel)
        }
        composable(route = AppRoutes.SignUp.route) {
            SignUpScreen(navController = navController, viewModel = signUpViewModel)
        }
        composable(route = AppRoutes.Main.route) {
            MainScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavBarNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Today.route) {
        composable(NavigationItem.Today.route) { TodayScreen(navController = navController) }
        composable(NavigationItem.Insights.route) { InsightsScreen() }
        composable(NavigationItem.Chat.route) { ChatScreen(navController) }
        composable(AppRoutes.Settings.route) {
            SettingsScreen(navController)
        }
        composable("postDetail/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")
            if (postId != null) {
                PostDetailScreen(postId = postId, navController = navController, postViewModel = viewModel())
            }
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
}
