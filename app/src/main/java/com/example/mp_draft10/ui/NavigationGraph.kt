package com.example.mp_draft10.ui

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
import com.example.mp_draft10.ui.components.NavigationItem
import com.example.mp_draft10.ui.screens.ChatScreen
import com.example.mp_draft10.ui.screens.InsightsScreen
import com.example.mp_draft10.ui.screens.SettingsScreen
import com.example.mp_draft10.ui.screens.TodayScreen

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
        composable(NavigationItem.Chat.route) { ChatScreen() }
        composable(AppRoutes.Settings.route) {
            SettingsScreen(navController)
        }
    }
}