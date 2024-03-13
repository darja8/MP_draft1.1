package com.example.mp_draft10.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mp_draft10.auth.SignInViewModel
import com.example.mp_draft10.ui.AppRoutes
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingsScreen(navController: NavHostController, viewModel: SignInViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Settings Screen")
        Button(

            onClick = {
                FirebaseAuth.getInstance().signOut().also {
                    navController.navigate(AppRoutes.SignIn.route) {
                        popUpTo(AppRoutes.Main.route) {
                            inclusive = true
                        }
                    }
                }
            }
  ) {
            Text("Log Out")
        }
    }
}