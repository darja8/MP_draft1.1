package com.example.mp_draft10

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    data object Today : NavigationItem("today", R.drawable.calendar, "Today")
    data object Insights : NavigationItem("insights", R.drawable.chart, "Insights")
    data object Chat : NavigationItem("chat", R.drawable.chat, "Chat")
}