package com.example.vinylrecordfinder.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.vinylrecordfinder.data.local.FavoritesManager
import com.example.vinylrecordfinder.data.remote.DiscogsAlbum
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBack(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Înapoi")
            }
        }
    )
}

@Composable
fun DetailsScreen(navController: NavController, album: DiscogsAlbum) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isFavorite by remember { mutableStateOf(false) }

    // Verifică dacă e deja la favorite
    LaunchedEffect(Unit) {
        isFavorite = FavoritesManager.isFavorite(context, album)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarWithBack(title = "Detalii album") {
            navController.popBackStack()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(album.thumb),
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = album.title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InfoRow("🎧 Format", album.format?.joinToString() ?: "-")
                InfoRow("🏷️ Label", album.label?.joinToString() ?: "-")
                InfoRow("🌍 Țara", album.country ?: "-")
                InfoRow("📅 An", album.year?.toString() ?: "-")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isFavorite) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            FavoritesManager.removeFavorite(context, album)
                            isFavorite = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🗑 Scoate din favorite")
                }
            } else {
                Button(
                    onClick = {
                        scope.launch {
                            FavoritesManager.addFavorite(context, album)
                            isFavorite = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("⭐ Adaugă la favorite")
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
