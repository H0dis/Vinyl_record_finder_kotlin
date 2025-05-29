package com.example.vinylrecordfinder.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.vinylrecordfinder.data.remote.DiscogsAlbum

@Composable
fun DetailsScreen(
    navController: NavController,
    album: DiscogsAlbum,
    isFavorite: Boolean,
    onAddToFavorites: () -> Unit,
    onRemoveFromFavorites: () -> Unit
) {
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

        Text(
            text = "Format: ${album.format?.joinToString() ?: "Necunoscut"}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Label: ${album.label?.joinToString() ?: "Necunoscut"}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!isFavorite) {
            Button(
                onClick = onAddToFavorites,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("⭐ Adauga la favorite")
            }
        } else {
            OutlinedButton(
                onClick = onRemoveFromFavorites,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🗑 Scoate din favorite")
            }
        }
    }
}
