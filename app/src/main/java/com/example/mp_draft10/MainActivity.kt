package com.example.mp_draft10

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.mp_draft10.auth.SignInViewModel
import com.example.mp_draft10.auth.SignUpViewModel
import com.example.mp_draft10.ui.NavigationAuthentication
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Composable
    fun BuildNavigationGraph() {
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
        }
    }
}
