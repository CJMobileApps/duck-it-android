package com.cjmobileapps.duckitandroid.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.cjmobileapps.duckitandroid.data.datastore.DuckItPreferencesKeys
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.room.DuckItDao
import com.cjmobileapps.duckitandroid.util.coroutine.CoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class DuckItLocalDataSource(
    private val duckDuckItDataStorePreferences: DataStore<Preferences>,
    private val duckItDao: DuckItDao,
    private val coroutineDispatchers: CoroutineDispatchers
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

    suspend fun getDuckItPostsFlow(): Flow<Posts> {
        return withContext(coroutineDispatchers.io) {
            duckItDao.getPosts()
        }
    }

    suspend fun createDuckItPosts(posts: Posts) {
        withContext(coroutineDispatchers.io) {
            duckItDao.deletePosts()
            duckItDao.insertPosts(posts)
        }
    }
}
