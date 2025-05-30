package com.example.vinylrecordfinder.ui.theme.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@Composable
fun DetailsScreen(
    navController: NavController,
    album: DiscogsAlbum
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isFavorite by remember { mutableStateOf(false) }

    // Citire initială din DataStore
    LaunchedEffect(Unit) {
        FavoritesManager.isFavorite(context, album).let {
            isFavorite = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
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
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Format: ${album.format?.joinToString() ?: "-"}")
        Text(text = "Label: ${album.label?.joinToString() ?: "-"}")

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
                Text("⭐ Adauga la favorite")
            }
        }
    }
}

