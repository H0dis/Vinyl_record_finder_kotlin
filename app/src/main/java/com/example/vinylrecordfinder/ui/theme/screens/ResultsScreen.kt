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
                items(albums.filter { it.title.isNotBlank() && (it.format != null || it.label != null || it.year != null) }) { album ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                val albumJson = Uri.encode(Gson().toJson(album))
                                navController.navigate("details?album=$albumJson")
                            },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(album.thumb),
                                contentDescription = album.title,
                                modifier = Modifier
                                    .size(72.dp)
                                    .padding(end = 12.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = album.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                if (!album.format.isNullOrEmpty()) {
                                    Row {
                                        Text("Format: ", style = MaterialTheme.typography.bodyMedium)
                                        Text(album.format.joinToString(), style = MaterialTheme.typography.bodyMedium)
                                    }
                                }

                                if (!album.label.isNullOrEmpty()) {
                                    Row {
                                        Text("Label: ", style = MaterialTheme.typography.bodyMedium)
                                        Text(album.label.joinToString(), style = MaterialTheme.typography.bodyMedium)
                                    }
                                }

                                if (!album.country.isNullOrBlank()) {
                                    Row {
                                        Text("Țara: ", style = MaterialTheme.typography.bodyMedium)
                                        Text(album.country, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }

                                if (album.year != null) {
                                    Row {
                                        Text("An: ", style = MaterialTheme.typography.bodyMedium)
                                        Text(album.year.toString(), style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
   }
}
