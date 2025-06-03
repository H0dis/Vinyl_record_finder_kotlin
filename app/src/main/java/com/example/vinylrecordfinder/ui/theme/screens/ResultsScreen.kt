package com.example.vinylrecordfinder.ui.theme.screens

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

    val token = "cqNCTHYUoBrAahVpJVPAQmgEeEoSbBhqlUwZdNIK"

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

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        albums.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nu s-au găsit rezultate pentru „$query”.")
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(albums) { album ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                val albumJson = Uri.encode(Gson().toJson(album))
                                navController.navigate("details?album=$albumJson")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(album.thumb),
                                contentDescription = album.title,
                                modifier = Modifier
                                    .size(72.dp)
                                    .aspectRatio(1f),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = album.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Format: ${album.format?.joinToString() ?: "-"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                album.label?.let {
                                    Text(
                                        text = "Label: ${it.joinToString()}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
