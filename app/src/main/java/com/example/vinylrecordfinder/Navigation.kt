package com.example.vinylrecordfinder

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vinylrecordfinder.ui.theme.screens.ResultsScreen
import com.example.vinylrecordfinder.ui.theme.screens.SearchScreen
import com.google.gson.Gson
import com.example.vinylrecordfinder.data.remote.DiscogsAlbum
import com.example.vinylrecordfinder.ui.theme.screens.DetailsScreen
import com.example.vinylrecordfinder.ui.theme.screens.FavoritesScreen



@Composable
fun VinylNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Search.route) {
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
        }
        composable("details?album={album}") { backStackEntry ->
            val json = backStackEntry.arguments?.getString("album") ?: ""
            val album = Gson().fromJson(Uri.decode(json), DiscogsAlbum::class.java)

            DetailsScreen(navController = navController, album = album)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController)
        }


    }
}

