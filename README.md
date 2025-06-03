# Vinyl Record Finder – Android App

Vinyl Record Finder este o aplicatie Android dezvoltata cu Jetpack Compose si Kotlin. Aplicatia permite cautarea si salvarea de albume de vinil folosind API-ul Discogs. Interfata este moderna, cu suport pentru tema light si dark.

## Scopul aplicatiei

Aplicatia permite utilizatorilor:
- sa caute albume de vinil prin API-ul Discogs
- sa vizualizeze detalii despre fiecare album
- sa salveze local albumele favorite
- sa comute intre tema light si dark, cu salvare persistenta

## Tehnologii utilizate

- Jetpack Compose – UI declarativ pentru Android
- Retrofit + GSON – pentru apeluri HTTP si parsare JSON
- Kotlinx Serialization – pentru serializare/deserializare obiecte
- DataStore Preferences – salvare tema si albume favorite
- Navigation Compose – navigare intre ecrane
- Coil – incarcare imagini din URL

## Functionalitati

### 1. Cautare albume (SearchScreen)

- Utilizatorul introduce un nume de album
- Se face apel HTTP la:  
  https://api.discogs.com/database/search?q={query}&token={token}
- Rezultatele sunt afisate intr-o lista scrollabila (LazyColumn)

```kotlin
OutlinedTextField(
    value = query,
    onValueChange = { query = it },
    label = { Text("Nume album") },
    placeholder = { Text("Ex: Pink Floyd") },
    modifier = Modifier.fillMaxWidth()
)
```

### 2. Afisare rezultate (ResultsScreen)

- Rezultatele sunt afisate in LazyColumn
- La click pe un item se face navigare catre detalii
- Afiseaza un loader in timpul fetch-ului

### 3. Detalii album (DetailsScreen)

- Primesti datele ca obiect serializat (Uri.encode)
- Afiseaza imagine, titlu, format, label si buton de adaugare/eliminare din favorite

### 4. Favorite (FavoritesScreen)

- Afiseaza albumele salvate local cu DataStore
- Navigare catre detalii disponibila din lista

### 5. Navigare intre ecrane

- Navigarea este definita in VinylNavGraph.kt cu NavHost
- Bottom navigation bar este inclusa in Scaffold

```kotlin
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
```

### 6. Tema Light/Dark

- Se salveaza preferinta utilizatorului in DataStore
- Comutatorul este prezent in SettingsScreen
- Tema este aplicata in Theme.kt

### 7. Verificare conexiune internet

- In SearchScreen, daca nu exista conexiune, se afiseaza mesaj de eroare

```kotlin
if (!hasInternet) {
    Text("Aplicatia nu are conexiune la internet.", color = Color.Red)
}
```

## Apeluri HTTP – API Discogs

Se foloseste Retrofit pentru apeluri catre API:

```kotlin
@GET("database/search")
suspend fun searchAlbums(
    @Query("q") query: String,
    @Query("token") token: String
): DiscogsResponse
```

Datele sunt mapate pe clasa DiscogsAlbum.

## Stocare offline: favorite si tema

### Favorite – FavoritesManager.kt

- Obiectele DiscogsAlbum sunt serializate in JSON cu kotlinx.serialization

```kotlin
val current = getFavorites(context).firstOrNull() ?: emptyList()
val updated = current + album
preferences[FAVORITES_KEY] = Json.encodeToString(updated)
```

### Tema – ThemeManager.kt

- Se salveaza un boolean pentru tema

```kotlin
val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
context.dataStore.edit { it[DARK_THEME_KEY] = true }
```

## Structura proiectului

```
com.example.vinylrecordfinder
├── MainActivity.kt
├── Screen.kt
├── ui/
│   ├── BottomNavBar.kt
│   ├── screens/
│   │   ├── SearchScreen.kt
│   │   ├── ResultsScreen.kt
│   │   ├── DetailsScreen.kt
│   │   ├── FavoritesScreen.kt
│   │   └── SettingsScreen.kt
│   ├── navigation/
│   │   └── VinylNavGraph.kt
│   └── theme/
│       └── Theme.kt
├── data/
│   ├── local/
│   │   ├── FavoritesManager.kt
│   │   └── ThemeManager.kt
│   └── remote/
│       ├── DiscogsApi.kt
│       └── RetrofitClient.kt
```

## Componente UI

- SearchScreen.kt – input + buton cautare
- ResultsScreen.kt – afisare lista albume
- DetailsScreen.kt – imagine, detalii si buton de favorite
- FavoritesScreen.kt – lista cu albumele salvate
- SettingsScreen.kt – comutator pentru tema
- BottomNavBar.kt – bara de navigare jos

## Concluzie

Proiectul este complet functional si include:
- cautare online prin API Discogs  
- afisare detalii albume  
- salvare locala a albumelor favorite  
- tema personalizabila  
- navigare fluida intre ecrane
