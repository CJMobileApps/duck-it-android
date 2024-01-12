package com.cjmobileapps.duckitandroid.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.datastore.DuckItDataStore
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

class DuckItLocalDataSourceTest : BaseTest() {

    private lateinit var duckItLocalDataSource: DuckItLocalDataSource

    @Mock
    lateinit var mockDuckItDataStore: DuckItDataStore

    @Mock
    lateinit var mockDataStore: DataStore<Preferences>

    @Mock
    lateinit var mockPreferences: Preferences

    private fun setupDuckItLocalDataSource() {
            duckItLocalDataSource = DuckItLocalDataSource(
                duckItDataStore = mockDuckItDataStore
            )
    }

    @Test
    fun `duckItTokenFlow happy success flow`(): Unit = runBlocking {

        //when
        Mockito.`when`(mockDuckItDataStore.dataStore)
            .thenReturn(mockDataStore)
        val mockPreferencesFlow: Flow<Preferences> = flow {
            emit(mockPreferences)
        }
        Mockito.`when`(mockDataStore.data).thenReturn(mockPreferencesFlow)
        Mockito.`when`(mockPreferences[DuckItPreferencesKeys.AUTHORIZATION_DUCKIT_TOKEN]).thenReturn(MockData.mockToken)

        // then
        setupDuckItLocalDataSource()
        val duckItToken: String = duckItLocalDataSource.duckItTokenFlow.first()

        // verify
        Mockito.verify(mockDataStore, Mockito.times(1)).data
        Assertions.assertEquals(
            duckItToken,
            MockData.mockToken
        )
    }
}
