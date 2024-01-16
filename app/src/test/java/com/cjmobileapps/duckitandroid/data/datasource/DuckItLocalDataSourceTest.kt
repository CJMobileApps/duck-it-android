package com.cjmobileapps.duckitandroid.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.datastore.DuckItPreferencesKeys
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.room.DuckItDao
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import com.cjmobileapps.duckitandroid.testutil.TestCoroutineDispatchers
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

    @Mock
    lateinit var mockDuckItDao: DuckItDao

    private fun setupDuckItLocalDataSource() {
        duckItLocalDataSource = DuckItLocalDataSource(
            duckDuckItDataStorePreferences = mockDuckDuckItDataStorePreferences,
            duckItDao = mockDuckItDao,
            coroutineDispatchers = TestCoroutineDispatchers
        )
    }

    @Test
    fun `duckItTokenFlow happy success flow`(): Unit = runBlocking {

        // given
        val mockPreferencesFlow: Flow<Preferences> = flow {
            emit(mockPreferences)
        }

        // when
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

        // given
        val mockPreferencesFlow: Flow<Preferences> = flow {
            throw IOException()
        }

        // when
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

        // given
        val mockPreferencesFlow: Flow<Preferences> = flow {
            throw Exception()
        }

        // when
        Mockito.`when`(mockDuckDuckItDataStorePreferences.data).thenReturn(mockPreferencesFlow)

        // then
        setupDuckItLocalDataSource()
        try {
            duckItLocalDataSource.duckItTokenFlow.first()
        } catch (e: Exception) {
            Assertions.assertTrue(true)
        }
    }

    @Test
    fun `getDuckItPostsFlow happy success flow`(): Unit = runBlocking {

        // given
        val mockPostsFlow: Flow<Posts> = flow {
            emit(MockData.mockPosts)
        }
        Mockito.`when`(mockDuckItDao.getPosts()).thenReturn(mockPostsFlow)

        // then
        setupDuckItLocalDataSource()
        val duckItPosts = duckItLocalDataSource.getDuckItPostsFlow().first()

        // verify
        Mockito.verify(mockDuckItDao, Mockito.times(1)).getPosts()

        Assertions.assertEquals(
            duckItPosts,
            MockData.mockPosts
        )
    }

    @Test
    fun `createDuckItPosts happy success flow`(): Unit = runBlocking {

        // given
        Mockito.`when`(mockDuckItDao.deletePosts()).thenReturn(Unit)
        Mockito.`when`(mockDuckItDao.insertPosts(MockData.mockPosts)).thenReturn(Unit)

        // then
        setupDuckItLocalDataSource()
        duckItLocalDataSource.createDuckItPosts(MockData.mockPosts)

        // verify
        Mockito.verify(mockDuckItDao, Mockito.times(1)).deletePosts()
        Mockito.verify(mockDuckItDao, Mockito.times(1)).insertPosts(MockData.mockPosts)
    }
}
