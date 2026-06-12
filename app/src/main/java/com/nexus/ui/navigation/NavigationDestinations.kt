package com.nexus.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object Stats : Screen("stats")
    object Add : Screen("add")
}