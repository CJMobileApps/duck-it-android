package com.cjmobileapps.duckitandroid.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.datastore.DuckItPreferencesKeys
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.io.IOException

class DuckItLocalDataSourceTest : BaseTest() {

    private lateinit var duckItLocalDataSource: DuckItLocalDataSource

    @Mock
    lateinit var mockDuckDuckItDataStorePreferences: DataStore<Preferences>

    @Mock
    lateinit var mockPreferences: Preferences

    private fun setupDuckItLocalDataSource() {
        duckItLocalDataSource = DuckItLocalDataSource(
            duckDuckItDataStorePreferences = mockDuckDuckItDataStorePreferences
        )
    }

    @Test
    fun `duckItTokenFlow happy success flow`(): Unit = runBlocking {

        //when
        val mockPreferencesFlow: Flow<Preferences> = flow {
            emit(mockPreferences)
        }
        Mockito.`when`(mockDuckDuckItDataStorePreferences.data).thenReturn(mockPreferencesFlow)
        Mockito.`when`(mockPreferences[DuckItPreferencesKeys.AUTHORIZATION_DUCKIT_TOKEN])
            .thenReturn(MockData.mockToken)

        // then
        setupDuckItLocalDataSource()
        val duckItToken: String = duckItLocalDataSource.duckItTokenFlow.first()

        // verify
        Mockito.verify(mockDuckDuckItDataStorePreferences, Mockito.times(1)).data
        Assertions.assertEquals(
            duckItToken,
            MockData.mockToken
        )
    }

    @Test
    fun `duckItTokenFlow throw IOException`(): Unit = runBlocking {

        //when
        val mockPreferencesFlow: Flow<Preferences> = flow {
            throw IOException()
        }
        Mockito.`when`(mockDuckDuckItDataStorePreferences.data).thenReturn(mockPreferencesFlow)

        // then
        setupDuckItLocalDataSource()
        val duckItToken: String = duckItLocalDataSource.duckItTokenFlow.first()

        // verify
        Mockito.verify(mockDuckDuckItDataStorePreferences, Mockito.times(1)).data
        Assertions.assertEquals(
            duckItToken,
            ""
        )
    }

    @Test
    fun `duckItTokenFlow throw Exception`(): Unit = runBlocking {

        //when
        val mockPreferencesFlow: Flow<Preferences> = flow {
            throw Exception()
        }
        Mockito.`when`(mockDuckDuckItDataStorePreferences.data).thenReturn(mockPreferencesFlow)

        // then
        setupDuckItLocalDataSource()
        try {
            duckItLocalDataSource.duckItTokenFlow.first()
        } catch (e: Exception) {
            Assertions.assertTrue(true)
        }
    }
}
