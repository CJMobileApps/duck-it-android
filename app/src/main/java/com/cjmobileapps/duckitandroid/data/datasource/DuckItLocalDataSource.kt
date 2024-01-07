package com.cjmobileapps.duckitandroid.data.datasource

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.cjmobileapps.duckitandroid.data.datastore.DuckItDataStore
import com.cjmobileapps.duckitandroid.data.datastore.DuckItPreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DuckItLocalDataSource(
    private val duckItDataStore: DuckItDataStore
) {

    val duckItTokenFlow: Flow<String> = duckItDataStore.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[DuckItPreferencesKeys.AUTHORIZATION_DUCKIT_TOKEN] ?: ""
        }

    private suspend fun updateDuckItToken(token: String) {
        duckItDataStore.dataStore.edit { preferences ->
            preferences[DuckItPreferencesKeys.AUTHORIZATION_DUCKIT_TOKEN] = token
        }
    }

    suspend fun addDuckItToken(token: String) {
        updateDuckItToken(token)
    }

    suspend fun removeDuckItToken() {
        updateDuckItToken("")
    }
}
