package com.example.mp_draft10.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mp_draft10.AppRoutes
import com.example.mp_draft10.firebase.database.AddNewUserViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * The SettingsScreen Composable function provides a settings menu for users to customize various aspects of their experience
 * within the application built with Jetpack Compose. It is designed to offer different settings options based on the user's role,
 * including exclusive moderator capabilities such as creating posts and articles. Firebase Authentication is used for user management.
 *
 * Features:
 * - Displays user-specific settings, including options to set or update avatars and, for moderators, to add posts and articles.
 * - Provides a log out option that signs the user out of the application and redirects them to the sign-in screen.
 * - Utilizes a clean and accessible UI with clear navigation cues and interactive list items for better user engagement.
 * - Leverages ViewModel to fetch user-specific settings and roles, enhancing the dynamic capabilities of the screen.
 * - Navigation is managed via NavHostController, allowing for effective user flow and back navigation within the app.
 *
 * Requirements:
 * - Firebase setup must be correctly configured to manage user authentication and role management efficiently.
 * - The application should ensure that all user interactions are handled securely, maintaining data privacy and integrity.
 * - All navigational and interactive elements should be tested for accessibility and ease of use to accommodate a broad user base.
 *
 * This screen is pivotal in enhancing user satisfaction and personalization of the app by allowing users to configure settings
 * according to their preferences and roles, thereby making it a vital component of the application's user experience strategy.
 *
 * @author Daria Skrzypczak (jus27)
 * @version 1.0
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, addNewUserViewModel: AddNewUserViewModel = hiltViewModel()) {
    var userType by remember { mutableStateOf("") }

    LaunchedEffect(userType) {
        userType = addNewUserViewModel.fetchUserType().toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (userType != "moderator"){
                ListItem(
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.AvatarSetting.route)  },
                    headlineContent = { Text("Set Avatar") },
                    trailingContent = {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Set Avatar",
                        )
                    }
                )
            }

            if(userType == "moderator"){
                Text(text = "Moderator Options",
                    Modifier
                        .align(Alignment.Start)
                        .padding(start = 15.dp))
                Divider()
                ListItem(
                    modifier = Modifier.clickable {
                    navController.navigate(AppRoutes.AddPostScreen.route)  },
                    headlineContent = { Text("Create post") },
                    trailingContent = {
                        Icon(
                            Icons.Filled.Analytics,
                            contentDescription = "Add post",
                        )
                    }
                )
                ListItem(
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.AddArticleScreen.route)  },
                    headlineContent = { Text("Add Article") },
                    trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Create Article",
                        )
                    }
                )
            }

            Divider()

            ListItem(
                modifier = Modifier.clickable {
                    FirebaseAuth.getInstance().signOut()

                    navController.navigate(AppRoutes.SignIn.route) {
                        popUpTo(AppRoutes.SignIn.route) { inclusive = true }
                    }
                },
                headlineContent = { Text("Log Out", color = MaterialTheme.colorScheme.error) },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Log Out",
                        tint = MaterialTheme.colorScheme.error // Sets the icon color to red. Replace with MaterialTheme.colorScheme.error or a custom red as needed.
                    )
                }
            )
        }
    }
}

private fun restartApp(context: android.content.Context) {
    FirebaseAuth.getInstance().signOut()
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent?.component
    val restartIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(restartIntent)
    Runtime.getRuntime().exit(0)
}

//@Preview(showBackground = true)
//@Composable
//fun SettingsScreenPreview() {
//    // Mock NavHostController. In a real scenario, you might want to navigate between composables.
//    val navController = rememberNavController()
//
//    // Mock ViewModel. You might want to display specific UI states based on ViewModel data.
//    val mockAuthViewModel = AuthViewModel() // Ensure you have a parameterless constructor or create a mock instance appropriately.
//
//    SettingsScreen(navController = navController, authViewModel = mockAuthViewModel)
//}