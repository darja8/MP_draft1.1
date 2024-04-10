package com.example.mp_draft10.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenScaffold(
    navController: NavHostController,
    fab: (@Composable () -> Unit)? = null,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        floatingActionButton = { if (fab != null) fab() else {} },
        content = {innerPadding ->
            pageContent(innerPadding)
        }
    )
}