Documentatie proiect Android: Vinyl Record
Finder (Jetpack Compose + Kotlin)
Scopul aplicatiei
Aplicatia Vinyl Record Finder permite utilizatorilor sa caute albume de vinil folosind API-ul
Discogs, sa vizualizeze detalii despre acestea si sa salveze albumele preferate local. Interfata este
construita cu Jetpack Compose si suporta atat mod light cat si dark.
Tehnologii utilizate
• Jetpack Compose – UI declarativ modern pentru Android
• Retrofit + GSON – pentru apeluri HTTP si parsare JSON(convertirea unui text în format
JSON intr-un obiect pe care codul il intelege.)
• Kotlinx Serialization – pentru serializare/deserializare a obiectelor
• DataStore Preferences – pentru salvare locala a temei si a albumelor favorite
• Navigation Compose – pentru rutare intre ecrane
• Coil – pentru incarcare imagini din URL
Functionalitati implementate
1. Cautare de albume (SearchScreen)
o Utilizatorul introduce o denumire de album.
o Apel API catre https://api.discogs.com/database/search?q={query}&token={token}.
o Rezultatele sunt afisate intr-o lista scrollabila cu imagine, titlu si format.
OutlinedTextField(
 value = query,
 onValueChange = { query = it },
 label = { Text("Nume album") },
 placeholder = { Text("Ex: Pink Floyd") },
 modifier = Modifier.fillMaxWidth()
)
2. Afisare rezultate (ResultsScreen)
o Listeaza rezultatele cautarii in LazyColumn.
o Pe fiecare item se poate da click pentru a merge la detalii.
o Afiseaza un loader cat timp se face fetch la date.
3. Detalii album (DetailsScreen)
o Datele sunt transmise ca obiect serializat prin Uri.encode().
o Afiseaza imagine, titlu, format, label si buton de adaugare/eliminare din favorite.
4. Favorite (FavoritesScreen)
o Afiseaza albumele salvate local, stocate in DataStore.
o Permite navigare catre ecranul de detalii.
5. Navigare intre ecrane (BottomNavBar + Navigation Compose)
o Navigarea este gestionata de NavHost in VinylNavGraph.kt.
o Se foloseste Scaffold pentru integrarea cu bara de jos.
Scaffold(
 bottomBar = { BottomNavBar(navController) }
) { padding ->
 NavHost(
 navController = navController,
 startDestination = Screen.Search.route,
 modifier = Modifier.padding(padding)
 ) {
 composable(Screen.Search.route) { SearchScreen(navController) }
 composable(Screen.Results.route + "?query={query}") { ... }
 composable("details?album={album}") { ... }
 composable(Screen.Favorites.route) { FavoritesScreen(navController) }
 composable(Screen.Settings.route) { SettingsScreen() }
 }
}
6. Tema Light/Dark
o Se foloseste DataStore pentru a salva booleanul is_dark_mode.
o Comutatorul este prezent in SettingsScreen.
o Tema este aplicata in Theme.kt in functie de valoarea salvata.
7. Verificare conexiune internet
o In SearchScreen, daca nu exista conexiune, se afiseaza mesaj de eroare:
if (!hasInternet) {
 Text("Aplicatia nu are conexiune la internet.", color = Color.Red)
}
Apeluri HTTP – API Discogs
Se foloseste Retrofit pentru request-uri la:
https://api.discogs.com/database/search
Functie:
@GET("database/search")
suspend fun searchAlbums(
 @Query("q") query: String,
 @Query("token") token: String
): DiscogsResponse
Datele sunt mapate pe clasa DiscogsAlbum.
Stocare offline: favorite si tema
Favorite – FavoritesManager.kt
• Stocare locala folosind DataStore Preferences
• Obiectele DiscogsAlbum sunt serializate in JSON cu kotlinx.serialization
val current = getFavorites(context).firstOrNull() ?: emptyList()
val updated = current + album
preferences[FAVORITES_KEY] = Json.encodeToString(updated)
Tema – ThemeManager.kt
• Se salveaza boolean is_dark_mode
val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
context.dataStore.edit { it[DARK_THEME_KEY] = true }
Structura proiectului
com.example.vinylrecordfinder
├── MainActivity.kt // initializeaza tema si navigarea
├── Screen.kt // definitie rute
├── ui/
│ ├── BottomNavBar.kt // bara de navigatie jos
│ ├── screens/
│ │ ├── SearchScreen.kt
│ │ ├── ResultsScreen.kt
│ │ ├── DetailsScreen.kt
│ │ ├── FavoritesScreen.kt
│ │ └── SettingsScreen.kt
│ ├── navigation/
│ │ └── VinylNavGraph.kt // toate rutele
│ └── theme/
│ └── Theme.kt // tema light/dark
├── data/
│ ├── local/
│ │ ├── FavoritesManager.kt // salvare/citire favorite
│ │ └── ThemeManager.kt // salvare tema
│ └── remote/
│ ├── DiscogsApi.kt
│ └── RetrofitClient.kt
Componente UI cheie
• SearchScreen.kt – input si buton de cautare
• ResultsScreen.kt – lista cu albume (thumbnail, titlu)
• DetailsScreen.kt – imagine mare + detalii + buton de favorite
• FavoritesScreen.kt – lista din DataStore cu navigare catre detalii
• SettingsScreen.kt – switch Light/Dark
• BottomNavBar.kt – navigare intre cele 3 taburi principale
Concluzie
Proiectul Vinyl Record Finder este complet functional. Include:
• cautare online prin API Discogs
• afisare detalii albume
• salvare locala a albumelor favorite
• tema personalizabila
• navigare fluida intre ecrane
