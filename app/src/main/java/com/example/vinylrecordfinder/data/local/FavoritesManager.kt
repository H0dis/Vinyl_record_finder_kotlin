package com.example.vinylrecordfinder.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.vinylrecordfinder.data.remote.DiscogsAlbum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Extension pentru context
val Context.dataStore by preferencesDataStore(name = "favorites_store")

object FavoritesManager {
    private val FAVORITES_KEY = stringPreferencesKey("favorites_list")

    fun getFavorites(context: Context): Flow<List<DiscogsAlbum>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[FAVORITES_KEY] ?: "[]"
            Json.decodeFromString(json)
        }
    }

    suspend fun addFavorite(context: Context, album: DiscogsAlbum) {
        context.dataStore.edit { preferences ->
            val current = getFavorites(context).firstOrNull() ?: emptyList()
            val updated = current + album
            preferences[FAVORITES_KEY] = Json.encodeToString(updated)
        }
    }

    suspend fun removeFavorite(context: Context, album: DiscogsAlbum) {
        context.dataStore.edit { preferences ->
            val current = getFavorites(context).firstOrNull() ?: emptyList()
            val updated = current.filterNot { it.title == album.title }
            preferences[FAVORITES_KEY] = Json.encodeToString(updated)
        }
    }

    suspend fun isFavorite(context: Context, album: DiscogsAlbum): Boolean {
        return getFavorites(context).firstOrNull()?.any { it.title == album.title } ?: false
    }
}
