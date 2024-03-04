package com.example.mp_draft10

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mp_draft10.auth.SignInScreen
import com.example.mp_draft10.auth.SignInViewModel
import com.example.mp_draft10.auth.SignUpScreen
import com.example.mp_draft10.auth.SignUpViewModel
import com.example.mp_draft10.screens.ChatScreen
import com.example.mp_draft10.screens.InsightsScreen
import com.example.mp_draft10.screens.TodayScreen
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
        composable(NavigationItem.Today.route) { TodayScreen() }
        composable(NavigationItem.Insights.route) { InsightsScreen() }
        composable(NavigationItem.Chat.route) { ChatScreen() }

    }
}