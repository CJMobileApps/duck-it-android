package com.cjmobileapps.duckitandroid.data.duckit

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.network.NetworkConstants
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class DuckItUseCaseTest : BaseTest() {

    private lateinit var duckItUseCase: DuckItUseCase

    @Mock
    private lateinit var mockDuckItRepository: DuckItRepository

    @Mock
    private lateinit var mockAccountUseCase: AccountUseCase
    private fun setupDuckItUseCase() {
        duckItUseCase = DuckItUseCase(
            duckItRepository = mockDuckItRepository,
            accountUseCase = mockAccountUseCase
        )
    }

    @Test
    fun `getPosts happy success flow`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockDuckItRepository.getPosts()).thenReturn(MockData.mockPostResponseSuccess)

        // then
        setupDuckItUseCase()
        val getPostResponse = duckItUseCase.getPosts({

        })

        // verify
        Assertions.assertEquals(
            MockData.mockPostResponseSuccess,
            getPostResponse
        )
    }

    @Test
    fun `upvote happy success flow`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockDuckItRepository.upvote(MockData.mockPostId))
            .thenReturn(MockData.mockUpVotesResponseSuccess)

        // then
        setupDuckItUseCase()
        val upvoteResponse = duckItUseCase.upvote(MockData.mockPostId)

        // verify
        Assertions.assertEquals(
            MockData.mockUpVotesResponseSuccess,
            upvoteResponse
        )
    }

    @Test
    fun `downvote happy success flow`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockDuckItRepository.downvote(MockData.mockPostId))
            .thenReturn(MockData.mockUpVotesResponseSuccess)

        // then
        setupDuckItUseCase()
        val downvoteResponse = duckItUseCase.downvote(MockData.mockPostId)

        // verify
        Assertions.assertEquals(
            MockData.mockUpVotesResponseSuccess,
            downvoteResponse
        )
    }

    @Test
    fun `newPost happy success flow`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(true)
        Mockito.`when`(mockAccountUseCase.authorizationToken).thenReturn(MockData.mockToken)
        Mockito.`when`(
            mockDuckItRepository.newPost(
                newPost = MockData.mockNewPostRequest,
                authorizationToken = NetworkConstants.BEARER + mockAccountUseCase.authorizationToken
            )
        )
            .thenReturn(MockData.mockUnitResponseSuccess)

        // then
        setupDuckItUseCase()
        val newPostResponse = duckItUseCase.newPost(MockData.mockNewPostRequest)

        // verify
        Assertions.assertEquals(
            MockData.mockTrueResponseWrapper,
            newPostResponse
        )
    }

    @Test
    fun `newPost user not logged in return error`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(false)
        Mockito.`when`(mockAccountUseCase.authorizationToken).thenReturn(MockData.mockToken)
        Mockito.`when`(
            mockDuckItRepository.newPost(
                newPost = MockData.mockNewPostRequest,
                authorizationToken = NetworkConstants.BEARER + mockAccountUseCase.authorizationToken
            )
        )
            .thenReturn(MockData.mockUnitResponseSuccess)

        // then
        setupDuckItUseCase()
        val newPostResponse = duckItUseCase.newPost(MockData.mockNewPostRequest)

        // verify
        Assertions.assertEquals(
            MockData.mockAccountNotLoggedInResponseWrapper,
            newPostResponse
        )
    }

    @Test
    fun `newPost user token empty return error`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(true)
        Mockito.`when`(mockAccountUseCase.authorizationToken).thenReturn("")
        Mockito.`when`(
            mockDuckItRepository.newPost(
                newPost = MockData.mockNewPostRequest,
                authorizationToken = NetworkConstants.BEARER + mockAccountUseCase.authorizationToken
            )
        )
            .thenReturn(MockData.mockUnitResponseSuccess)

        // then
        setupDuckItUseCase()
        val newPostResponse = duckItUseCase.newPost(MockData.mockNewPostRequest)

        // verify
        Assertions.assertEquals(
            MockData.mockAccountNotLoggedInResponseWrapper,
            newPostResponse
        )
    }

    @Test
    fun `newPost throw generic error`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(true)
        Mockito.`when`(mockAccountUseCase.authorizationToken).thenReturn(MockData.mockToken)
        Mockito.`when`(
            mockDuckItRepository.newPost(
                newPost = MockData.mockNewPostRequest,
                authorizationToken = NetworkConstants.BEARER + mockAccountUseCase.authorizationToken
            )
        )
            .thenReturn(MockData.mockUnitErrorHttpNotFound)

        // then
        setupDuckItUseCase()
        val newPostResponse = duckItUseCase.newPost(MockData.mockNewPostRequest)

        // verify
        Assertions.assertEquals(
            MockData.mockAccountBooleanStateGenericErrorResponseWrapper,
            newPostResponse
        )
    }
}
