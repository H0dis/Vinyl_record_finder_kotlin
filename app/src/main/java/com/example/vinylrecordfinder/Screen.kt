package com.example.vinylrecordfinder


sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Results : Screen("results")
    object Details : Screen("details")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
}
