package com.example.mp_draft10.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenScaffold(
    navController: NavHostController,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        content = {innerPadding ->
            pageContent(innerPadding)
        }
    )
}

//NavHost(
//            navController = navController,
//            startDestination = AppRoutes.TodayScreen.route,
//        ) {
//            composable(AppRoutes.TodayScreen.route) {
//                TodayScreen(navController, addNewUserViewModel = hiltViewModel())
//            }
//            // Define other destinations
//            composable(AppRoutes.InsightsScreen.route) {
//                InsightsScreen()
//            }
//            composable(AppRoutes.HubScreen.route){
//                ChatScreen(navController = navController)
//            }
//        }