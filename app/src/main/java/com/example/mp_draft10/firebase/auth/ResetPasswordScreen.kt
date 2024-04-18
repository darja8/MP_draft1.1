package com.example.mp_draft10.firebase.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

@Composable
fun ResetPasswordScreen(navController: NavHostController, viewModel: SignInViewModel) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var shouldSendEmail by remember { mutableStateOf(false) }

    if (shouldSendEmail && email.isNotEmpty()) {
        LaunchedEffect(email) {
            viewModel.sendPasswordResetEmail(email)
            navController.navigate("PasswordResetScreen")
            shouldSendEmail = false // Reset the trigger
        }
    }

    Column (modifier = Modifier.background(MaterialTheme.colorScheme.background)){
    }

}