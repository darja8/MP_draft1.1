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
import com.example.mp_draft10.ui.screens.CreateAvatarScreen
import com.example.mp_draft10.ui.screens.InsightsScreen
import com.example.mp_draft10.ui.screens.PostDetailScreen
import com.example.mp_draft10.ui.screens.SettingsScreen
import com.example.mp_draft10.ui.theme.MP_draft10Theme
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
    data object Chat : NavigationItem("chat", R.drawable.chat, "Community")
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
        startDestination = AppRoutes.SignIn.route // Start with SignUpScreen by default
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
//
//@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//@Composable
//fun NavigationAuthentication(
//    navController: NavHostController = rememberNavController(),
//    signUpViewModel: SignUpViewModel,
//    signInViewModel: SignInViewModel
//) {
//    // State to keep track of user authentication status
//    var isAuthenticated by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }
//    val authStateListener = remember {
//        FirebaseAuth.AuthStateListener { firebaseAuth ->
//            isAuthenticated = firebaseAuth.currentUser != null
//        }
//    }
//
//    // Remember navController to prevent recomposition issues
//    val navControllerState = rememberNavController()
//
//    DisposableEffect(key1 = Unit) {
//        // Add the auth state listener when the composable enters the composition
//        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
//
//        // Remove the auth state listener when the composable leaves the composition
//        onDispose {
//            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
//        }
//    }
//
//    val startDestination = if (isAuthenticated) AppRoutes.Main.route else AppRoutes.SignUp.route
//
//    LaunchedEffect(key1 = isAuthenticated) {
//        // Navigate based on authentication status and clear back stack appropriately
//        if (isAuthenticated) {
//            navControllerState.navigate(AppRoutes.Main.route) {
//                popUpTo(navControllerState.graph.startDestinationId) {
//                    inclusive = true
//                }
//            }
//        } else {
//            navControllerState.navigate(AppRoutes.SignUp.route) {
//                popUpTo(navControllerState.graph.startDestinationId) {
//                    inclusive = true
//                }
//            }
//        }
//    }
//
//    NavHost(
//        navController = navControllerState,
//        startDestination = startDestination
//    ) {
//        composable(route = AppRoutes.SignIn.route) {
//            SignInScreen(navController = navControllerState, viewModel = signInViewModel)
//        }
//        composable(route = AppRoutes.SignUp.route) {
//            SignUpScreen(navController = navControllerState, viewModel = signUpViewModel)
//        }
//        composable(route = AppRoutes.Main.route) {
//            MainScreen()
//        }
//    }
//}


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
        composable("avatarSetting"){
            CreateAvatarScreen()
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
}
