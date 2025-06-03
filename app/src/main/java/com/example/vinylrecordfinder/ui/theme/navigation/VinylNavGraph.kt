package com.example.vinylrecordfinder.ui.theme.navigation


import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vinylrecordfinder.Screen
import com.example.vinylrecordfinder.data.remote.DiscogsAlbum
import com.example.vinylrecordfinder.ui.theme.BottomNavBar
import com.example.vinylrecordfinder.ui.theme.screens.DetailsScreen
import com.example.vinylrecordfinder.ui.theme.screens.FavoritesScreen
import com.example.vinylrecordfinder.ui.theme.screens.ResultsScreen
import com.example.vinylrecordfinder.ui.theme.screens.SearchScreen
import com.example.vinylrecordfinder.ui.theme.screens.SettingsScreen
import com.google.gson.Gson

@Composable
fun VinylNavGraph(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute?.startsWith(Screen.Search.route) == true ||
            currentRoute?.startsWith(Screen.Results.route) == true ||
            currentRoute?.startsWith(Screen.Favorites.route) == true ||
            currentRoute?.startsWith(Screen.Settings.route) == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Search.route) {
                SearchScreen(navController)
            }
            composable(Screen.Results.route + "?query={query}") { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                ResultsScreen(navController, query)
            }
            composable("details?album={album}") { backStackEntry ->
                val json = backStackEntry.arguments?.getString("album") ?: ""
                val album = Gson().fromJson(Uri.decode(json), DiscogsAlbum::class.java)
                DetailsScreen(navController = navController, album = album)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController)
            }
        }
        }
    }