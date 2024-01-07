package com.cjmobileapps.duckitandroid.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DuckItPreferencesKeys {
    val AUTHORIZATION_DUCKIT_TOKEN = stringPreferencesKey("authorization_duckit_token")
}
class DuckItDataStore(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(
        name = DUCKIT_PREFERENCES_NAME
    )

    val dataStore by lazy { context.dataStore }

    companion object {
        private const val DUCKIT_PREFERENCES_NAME = "duckit_preferences"
    }
}
