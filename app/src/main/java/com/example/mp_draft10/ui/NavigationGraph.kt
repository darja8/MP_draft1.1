package com.example.mp_draft10.ui

//import com.example.mp_draft10.ui.components.NavigationItem

//import com.example.mp_draft10.ui.screens.TodayScreen

//@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//@Composable
//fun NavigationAuthentication(
//    navController: NavHostController = rememberNavController(),
//    signUpViewModel: SignUpViewModel,
//    signInViewModel: SignInViewModel
//) {
//    NavHost(
//        navController = navController,
//        startDestination = AppRoutes.SignUp.route // Start with SignUpScreen by default
//    ) {
//        composable(route = AppRoutes.SignIn.route) {
//            SignInScreen(navController = navController, viewModel = signInViewModel)
//        }
//        composable(route = AppRoutes.SignUp.route) {
//            SignUpScreen(navController = navController, viewModel = signUpViewModel)
//        }
//        composable(route = AppRoutes.Main.route) {
//            MainScreen()
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//@Composable
//fun NavBarNavigation(navController: NavHostController) {
//    NavHost(navController, startDestination = NavigationItem.Today.route) {
//        composable(NavigationItem.Today.route) { TodayScreen(navController = navController) }
//        composable(NavigationItem.Insights.route) { InsightsScreen() }
//        composable(NavigationItem.Chat.route) { ChatScreen() }
//        composable(AppRoutes.Settings.route) {
//            SettingsScreen(navController)
//        }
//    }
//}