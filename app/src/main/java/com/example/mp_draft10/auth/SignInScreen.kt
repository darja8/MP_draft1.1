package com.example.mp_draft10.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel()
){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Enter your credentials to register")
        TextField(value = email, onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp), singleLine = true)
        Text(text = "Email")

        Spacer(modifier = Modifier.height(16.dp))

//        Text(text = "Enter your credentials to register")
        TextField(value = password, onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp), singleLine = true)
        Text(text = "Password")
    }

    Button(onClick = {
        scope.launch {
            viewModel.loginUser(email, password)
        }
    }, modifier = Modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp)) {
        Text(text = "Sign Up ")
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        if (state.value?.isLoading == true){
            CircularProgressIndicator()
        }
    }
    Text(text = "Already have an account? sign in")
    Text(text = "or connect with")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        IconButton(onClick = { /*TODO*/ }) {

        }
    }
}
