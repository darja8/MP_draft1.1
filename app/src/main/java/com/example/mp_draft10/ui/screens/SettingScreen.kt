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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mp_draft10.AppRoutes
import com.example.mp_draft10.auth.util.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current

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
            ListItem(
                modifier = Modifier.clickable { /* TODO: Implement avatar setting action */ },
                headlineContent = { Text("Set Avatar") },
                trailingContent = {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Set Avatar",
                        // Apply tint if needed, for example, using MaterialTheme to keep the icon in line with your app's theme.
                    )
                }
            )
            Divider()

            ListItem(
                modifier = Modifier.clickable {
                    FirebaseAuth.getInstance().signOut()
                    restartApp(context)
                    navController.navigate(AppRoutes.SignIn.route) {
                        popUpTo(AppRoutes.SignIn.route) { inclusive = true }
                    }
                },
                headlineContent = { Text("Log Out", color = MaterialTheme.colorScheme.error) },
                trailingContent = {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Log Out",
                        tint = MaterialTheme.colorScheme.error // Sets the icon color to red. Replace with MaterialTheme.colorScheme.error or a custom red as needed.
                    )
                }
            )

        }
    }
}

private fun restartApp(context: android.content.Context) {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent?.component
    val restartIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(restartIntent)
    Runtime.getRuntime().exit(0)
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    // Mock NavHostController. In a real scenario, you might want to navigate between composables.
    val navController = rememberNavController()

    // Mock ViewModel. You might want to display specific UI states based on ViewModel data.
    val mockAuthViewModel = AuthViewModel() // Ensure you have a parameterless constructor or create a mock instance appropriately.

    SettingsScreen(navController = navController, authViewModel = mockAuthViewModel)
}