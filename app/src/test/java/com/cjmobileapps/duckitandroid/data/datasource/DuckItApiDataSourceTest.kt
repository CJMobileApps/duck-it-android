package com.cjmobileapps.duckitandroid.data.datasource

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.network.DuckItApi
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import com.cjmobileapps.duckitandroid.testutil.TestCoroutineDispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class DuckItApiDataSourceTest : BaseTest() {

    private lateinit var duckItApiDataSource: DuckItApiDataSource

    @Mock
    lateinit var mockDuckItApi: DuckItApi

    private fun setupDuckItApiDataSource() {
        duckItApiDataSource = DuckItApiDataSource(
            duckItApi = mockDuckItApi,
            coroutineDispatchers = TestCoroutineDispatchers
        )
    }

    @Test
    fun `signIn happy success flow`(): Unit = runBlocking {

        //when
        Mockito.`when`(mockDuckItApi.signInAsync(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockDeferredTokenResponseSuccess)

        // then
        setupDuckItApiDataSource()
        val signInResponse = duckItApiDataSource.signIn(MockData.mockEmailPasswordRequest)

        // verify
        Assertions.assertEquals(
            MockData.mockTokenResponseSuccess,
            signInResponse
        )
    }

    @Test
    fun `signUp happy success flow`(): Unit = runBlocking {

        //when
        Mockito.`when`(mockDuckItApi.signUpAsync(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockDeferredTokenResponseSuccess)

        // then
        setupDuckItApiDataSource()
        val signUpResponse = duckItApiDataSource.signUp(MockData.mockEmailPasswordRequest)

        // verify
        Assertions.assertEquals(
            MockData.mockTokenResponseSuccess,
            signUpResponse
        )
    }

    @Test
    fun `newPost happy success flow`(): Unit = runBlocking {

        //when
        Mockito.`when`(mockDuckItApi.newPostAsync(MockData.mockToken, MockData.mockNewPostRequest))
            .thenReturn(MockData.mockDeferredUnitResponseSuccess)

        // then
        setupDuckItApiDataSource()
        val newPostResponse = duckItApiDataSource.newPost(
            newPost = MockData.mockNewPostRequest,
            authorizationToken = MockData.mockToken
        )

        // verify
        Assertions.assertEquals(
            MockData.mockUnitResponseSuccess,
            newPostResponse
        )
    }

    @Test
    fun `getPosts happy success flow`(): Unit = runBlocking {

        //when
        Mockito.`when`(mockDuckItApi.getPostsAsync())
            .thenReturn(MockData.mockDeferredPostsResponseSuccess)

        // then
        setupDuckItApiDataSource()
        val postsResponse = duckItApiDataSource.getPosts()

        // verify
        Assertions.assertEquals(
            MockData.mockPostsResponseSuccess,
            postsResponse
        )
    }

    @Test
    fun `upvote happy success flow`(): Unit = runBlocking {

        //when
        Mockito.`when`(mockDuckItApi.upvoteAsync(MockData.mockPostId))
            .thenReturn(MockData.mockDeferredUpVotesResponseSuccess)

        // then
        setupDuckItApiDataSource()
        val upvoteResponse = duckItApiDataSource.upvote(MockData.mockPostId)

        // verify
        Assertions.assertEquals(
            MockData.mockUpVotesResponseSuccess,
            upvoteResponse
        )
    }

    @Test
    fun `downvote happy success flow`(): Unit = runBlocking {

        //when
        Mockito.`when`(mockDuckItApi.downvoteAsync(MockData.mockPostId))
            .thenReturn(MockData.mockDeferredUpVotesResponseSuccess)

        // then
        setupDuckItApiDataSource()
        val downvoteResponse = duckItApiDataSource.downvote(MockData.mockPostId)

        // verify
        Assertions.assertEquals(
            MockData.mockUpVotesResponseSuccess,
            downvoteResponse
        )
    }
}
