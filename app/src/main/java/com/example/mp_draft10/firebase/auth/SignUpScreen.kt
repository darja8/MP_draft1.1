package com.example.mp_draft10.firebase.auth
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.mp_draft10.documents.termsAndConditions
import com.example.mp_draft10.firebase.database.AddNewUserViewModel
import kotlinx.coroutines.launch

/**
 * Screen to allow the user to sign up and create a new account
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
    addNewUserViewModel: AddNewUserViewModel = hiltViewModel()
){
    val username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userType = "user"
    val state = viewModel.signUpState.collectAsState(initial = null)
    var passwordVisible by remember { mutableStateOf(false) }
    var isTermsAgreed by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text("Terms and Conditions", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        termsAndConditions,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    TextButton(
                        onClick = { scope.launch { sheetState.hide() } },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Close")
                    }
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Enter your credentials to register")
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    label = { Text(text = "Email") }
                )

                Spacer(modifier = Modifier.height(12.dp))

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

                        // Localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(imageVector  = image, description)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.registerUser(email, password) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isTermsAgreed) { Text("Sign Up") }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isTermsAgreed,
                        onCheckedChange = { isTermsAgreed = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "I agree to the Terms and Conditions",
                        modifier = Modifier.clickable { scope.launch { sheetState.show() } }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Already have an account? Sign in",
                    modifier = Modifier.clickable { navController.navigate(AppRoutes.SignIn.route)
                    }
                )

                LaunchedEffect(key1 = state.value?.isSuccess) {
                    scope.launch {
                        if (state.value?.isSuccess?.isNotEmpty() == true) {
                            addNewUserViewModel.addUserDetails(username, email, userType)
                            val success = state.value?.isSuccess
                            Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                            navController.navigate(AppRoutes.TodayScreen.route) // Navigate to the main route
                        }
                    }
                }

                LaunchedEffect(key1 = state.value?.isError) {
                    scope.launch {}
                    if (state.value?.isError?.isNotEmpty() == true) {
                        val error = state.value?.isError
                        Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    )
}