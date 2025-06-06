package com.example.vinylrecordfinder.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.serialization.Serializable

// Model de răspuns
data class DiscogsResponse(
    val results: List<DiscogsAlbum>
)
@Serializable
data class DiscogsAlbum(
    val title: String,
    val thumb: String,
    val format: List<String>?,
    val label: List<String>?,
    val resource_url: String? = null,
    val country: String? = null,
    val year: Int? = null

)

interface DiscogsApi {
    @GET("database/search")
    suspend fun searchAlbums(
        @Query("q") query: String,
        @Query("token") token: String,
//        @Query("country") country: String? = null,
//        @Query("year") year: String? = null
    ): DiscogsResponse
}
