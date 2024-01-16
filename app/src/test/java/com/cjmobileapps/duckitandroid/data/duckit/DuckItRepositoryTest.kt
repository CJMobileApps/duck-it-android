package com.cjmobileapps.duckitandroid.data.duckit

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.datasource.DuckItApiDataSource
import com.cjmobileapps.duckitandroid.data.datasource.DuckItLocalDataSource
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class DuckItRepositoryTest : BaseTest() {

    private lateinit var duckItRepository: DuckItRepository

    @Mock
    lateinit var mockDuckItApiDataSource: DuckItApiDataSource

    @Mock
    lateinit var mockDuckItLocalDataSource: DuckItLocalDataSource

    private fun setupDuckItRepository() {
        duckItRepository = DuckItRepositoryImpl(
            duckItApiDataSource = mockDuckItApiDataSource,
            duckItLocalDataSource = mockDuckItLocalDataSource
        )
    }

    @Test
    fun `getPosts happy flow success`() = runBlocking {

        //when
        Mockito.`when`(mockDuckItApiDataSource.getPosts())
            .thenReturn(MockData.mockPostResponseSuccess)

        //then
        setupDuckItRepository()
        val getPostResponse = duckItRepository.getPosts()

        // verify
        Assertions.assertEquals(MockData.mockPostResponseSuccess, getPostResponse)
    }

    @Test
    fun `upvote happy flow success`() = runBlocking {

        //when
        Mockito.`when`(mockDuckItApiDataSource.upvote(MockData.mockPostId))
            .thenReturn(MockData.mockUpVotesResponseSuccess)

        //then
        setupDuckItRepository()
        val getUpvoteResponse = duckItRepository.upvote(MockData.mockPostId)

        // verify
        Assertions.assertEquals(MockData.mockUpVotesResponseSuccess, getUpvoteResponse)
    }

    @Test
    fun `downvote happy flow success`() = runBlocking {

        //when
        Mockito.`when`(mockDuckItApiDataSource.downvote(MockData.mockPostId))
            .thenReturn(MockData.mockUpVotesResponseSuccess)

        //then
        setupDuckItRepository()
        val getDownvoteResponse = duckItRepository.downvote(MockData.mockPostId)

        // verify
        Assertions.assertEquals(MockData.mockUpVotesResponseSuccess, getDownvoteResponse)
    }

    @Test
    fun `newPost happy flow success`(): Unit = runBlocking {

        //when
        Mockito.`when`(
            mockDuckItApiDataSource.newPost(
                newPost = MockData.mockNewPostRequest,
                authorizationToken = MockData.mockToken
            )
        ).thenReturn(MockData.mockUnitResponseSuccess)

        //then
        setupDuckItRepository()
        val newPostResponse = duckItRepository.newPost(
            newPost = MockData.mockNewPostRequest,
            authorizationToken = MockData.mockToken
        )

        // verify
        Assertions.assertEquals(MockData.mockUnitResponseSuccess, newPostResponse)
        Mockito.verify(mockDuckItApiDataSource, Mockito.times(1)).newPost(
            newPost = MockData.mockNewPostRequest,
            authorizationToken = MockData.mockToken
        )
    }

    @Test
    fun `getDuckItPostsFlow happy flow success`(): Unit = runBlocking {

        // when
        val mockDuckItPostsFlow: Flow<Posts> = flow {
            emit(MockData.mockPosts)
        }
        Mockito.`when`(mockDuckItLocalDataSource.getDuckItPostsFlow())
            .thenReturn(mockDuckItPostsFlow)

        // then
        setupDuckItRepository()
        val duckItPostsFlow = duckItRepository.getDuckItPostsFlow().first()

        // verify
        Assertions.assertEquals(
            MockData.mockPosts,
            duckItPostsFlow
        )
    }

    @Test
    fun `addDuckItPostsToDB happy flow success`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockDuckItLocalDataSource.createDuckItPosts(MockData.mockPosts))
            .thenReturn(Unit)

        // then
        setupDuckItRepository()
        duckItRepository.addDuckItPostsToDB(MockData.mockPosts)

        // verify
        Mockito.verify(mockDuckItLocalDataSource, Mockito.times(1))
            .createDuckItPosts(MockData.mockPosts)
    }
}
