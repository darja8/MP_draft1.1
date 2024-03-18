package com.example.mp_draft10.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mp_draft10.NavigationItem


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Today,
        NavigationItem.Insights,
        NavigationItem.Chat,
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            BottomNavigationItem(
                icon = {
                    val iconColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else Color.Gray
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = iconColor
                    )
                },
                label = {
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else Color.Gray // Manually setting the color for unselected labels
                    Text(
                        text = item.title,
                        color = textColor
                    )
                },
                selectedContentColor = Color.Transparent,
                unselectedContentColor = Color.Transparent,
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
