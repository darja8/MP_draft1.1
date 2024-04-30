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