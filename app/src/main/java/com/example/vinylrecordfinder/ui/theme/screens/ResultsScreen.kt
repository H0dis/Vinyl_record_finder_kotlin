package com.example.vinylrecordfinder.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.vinylrecordfinder.data.remote.DiscogsAlbum
import com.example.vinylrecordfinder.data.remote.RetrofitClient
import com.google.gson.Gson
import androidx.compose.ui.Alignment

@Composable
fun ResultsScreen(navController: NavController, query: String) {
    var albums by remember { mutableStateOf<List<DiscogsAlbum>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val token = "SpQReYYOIBOiedmRwftybayRAGZVNNxAFmPCcAOo" // Tokenul tău Discogs

    LaunchedEffect(query) {
        try {
            val response = RetrofitClient.api.searchAlbums(query, token)
            albums = response.results
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(albums) { album ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            val albumJson = Uri.encode(Gson().toJson(album))
                            navController.navigate("details?album=$albumJson")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(album.thumb),
                        contentDescription = album.title,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = album.title,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = album.format?.joinToString() ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Divider()
            }
        }
    }
}
