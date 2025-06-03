package com.example.vinylrecordfinder.ui.theme


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vinylrecordfinder.Screen

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(Screen.Search.route, Icons.Default.Search, "Căutare"),
        BottomNavItem(Screen.Favorites.route, Icons.Default.Favorite, "Favorite"),
        BottomNavItem(Screen.Settings.route, Icons.Default.Settings, "Setări"),
        //BottomNavItem(Screen.Details.route, Icons.Default.Info, "Detalii")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // DEBUG: Afiseaza ruta curenta in consola
    println("🔍 Current route: $currentRoute")

    // Afiseaza bara DOAR pe aceste rute
    if (
        currentRoute?.startsWith("search") == true ||
        currentRoute?.startsWith("favorites") == true ||
        currentRoute?.startsWith("settings") == true ||
        currentRoute?.startsWith("results") == true //||
       // currentRoute?.startsWith("detalii") == true
    ) {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute?.startsWith(item.route) == true,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Search.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) }
                )
            }
        }
    }
}
