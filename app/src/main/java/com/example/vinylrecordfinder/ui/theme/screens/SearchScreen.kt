package com.example.vinylrecordfinder.ui.theme.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vinylrecordfinder.Screen

@Composable
fun SearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Vinyl Record Finder",
            style = MaterialTheme.typography.headlineMedium
        )
    Column {
        val context = LocalContext.current
        if (!isConnected(context)) {
            Text(
                text = "⚠️ Aplicația nu are conexiune la internet",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

    }
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Nume album") },
            placeholder = { Text("Ex: Pink Floyd") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (query.isNotBlank()) {
                    val encoded = Uri.encode(query)
                    navController.navigate(Screen.Results.route + "?query=$encoded")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = "Cauta")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cauta")
        }
    }
}
@SuppressLint("ServiceCast")
fun isConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    @Suppress("DEPRECATION")
    return connectivityManager.activeNetworkInfo?.isConnected == true
}
