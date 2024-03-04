package com.example.mp_draft10

sealed class AppRoutes(val route: String) {
    data object SignIn : AppRoutes("sign_in")
    data object SignUp : AppRoutes("sign_up")
    data object Main : AppRoutes("main")

}

