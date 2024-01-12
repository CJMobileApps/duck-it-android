package com.cjmobileapps.duckitandroid.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.cjmobileapps.duckitandroid.data.datastore.DuckItPreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DuckItLocalDataSource(
    private val duckDuckItDataStorePreferences: DataStore<Preferences>
) {

    val duckItTokenFlow: Flow<String> = duckDuckItDataStorePreferences.data
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
        duckDuckItDataStorePreferences.edit { preferences ->
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
