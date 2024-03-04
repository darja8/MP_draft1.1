package com.example.mp_draft10

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mp_draft10.auth.SignInViewModel
import com.example.mp_draft10.auth.SignUpViewModel
import com.example.mp_draft10.screens.TodayScreen
import com.example.mp_draft10.ui.theme.MP_draft10Theme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MP_draft10Theme {
                val db = Firebase.firestore
                // A surface container using the 'background' color from the theme
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
//                    MainScreen()
                }
            }
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//@Composable
//fun MainScreen() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = { BottomNavigationBar(navController) }
//    ) { innerPadding ->
//        Box(modifier = Modifier.padding(innerPadding)) {
//            Navigation(navController = navController)
//        }
//    }
//}

//@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//        MainScreen()
//}

//@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//@Composable
//fun Navigation(navController: NavHostController) {
//    NavHost(navController, startDestination = NavigationItem.Today.route) {
//        composable(NavigationItem.Today.route) { TodayScreen() }
//        composable(NavigationItem.Insights.route) { InsightsScreen() }
//        composable(NavigationItem.Chat.route) { ChatScreen() }
//    }
//}

//@Composable
//fun Navigation(navController: NavHostController = rememberNavController()) {
//    NavHost(navController, startDestination = AppRoutes.SignUp.route) {
//        composable(route = AppRoutes.SignIn.route) {
//            SignInScreen()
//        }
//        composable(route = AppRoutes.SignUp.route) {
//            SignUpScreen()
//        }
//    }
//}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Today,
        NavigationItem.Insights,
        NavigationItem.Chat,
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            BottomNavigationItem(
                icon = {
                    val iconColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else Color.Gray // Manually setting the color for unselected items
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = iconColor
                    )
                },
                label = {
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else Color.Gray // Manually setting the color for unselected labels
                    Text(
                        text = item.title,
                        color = textColor
                    )
                },
                selectedContentColor = Color.Transparent, // Using Transparent to avoid overriding manual settings
                unselectedContentColor = Color.Transparent, // Using Transparent to avoid overriding manual settings
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun BottomNavigationBarPreview() {
//
//}