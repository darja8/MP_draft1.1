package com.example.mp_draft10.auth
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mp_draft10.AppRoutes
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel(),
){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
//    var userType by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Welcome back! Please sign in")
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                label = { Text(text = "Email") },
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                label = { Text(text = "Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                }
            )
            Button(
                onClick = {
                    scope.launch {
                        viewModel.loginUser(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = "Sign In")
            }
            if (state.value?.isLoading == true) {
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Don't have an account? Sign up",
            modifier = Modifier.clickable { navController.navigate(AppRoutes.SignUp.route) })

        LaunchedEffect(key1 = state.value) {
            state.value?.let { signInState ->
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                    when (signInState.userType) {
                        "moderator" -> navController.navigate(AppRoutes.AddPostScreen.route)
                        else -> navController.navigate(AppRoutes.TodayScreen.route)
                    }
                } else if (signInState.isError != null) {
                    Toast.makeText(context, signInState.isError, Toast.LENGTH_LONG).show()
                }
            }
        }

        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {}
            if (state.value?.isError?.isNotEmpty() == true) {
                val error = state.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }
}