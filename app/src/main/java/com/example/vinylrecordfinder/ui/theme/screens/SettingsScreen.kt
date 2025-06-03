package com.example.vinylrecordfinder.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vinylrecordfinder.ui.theme.ThemeManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isDark = ThemeManager.isDarkTheme(context).collectAsState(initial = false)

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Setări", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Temă întunecată")
            Switch(
                checked = isDark.value,
                onCheckedChange = {
                    scope.launch {
                        ThemeManager.setDarkTheme(context, it)
                    }
                }
            )
        }
    }
}
