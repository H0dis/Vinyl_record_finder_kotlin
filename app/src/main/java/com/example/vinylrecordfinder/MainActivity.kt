package com.example.vinylrecordfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.vinylrecordfinder.ui.theme.ThemeManager
import com.example.vinylrecordfinder.ui.theme.VinylRecordFinderTheme
import com.example.vinylrecordfinder.ui.theme.navigation.VinylNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkMode by ThemeManager.isDarkTheme(applicationContext).collectAsState(initial = false)

            VinylRecordFinderTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                VinylNavGraph(navController = navController)
            }
        }
    }
}
