package com.cjmobileapps.duckitandroid.data.duckit

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.network.NetworkConstants
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    fun `fetchPosts happy success flow`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockDuckItRepository.getPosts())
            .thenReturn(MockData.mockPostsResponseSuccess)
        Mockito.`when`(mockDuckItRepository.addDuckItPostsToDB(MockData.mockPosts)).thenReturn(Unit)


        // then
        setupDuckItUseCase()
        val postsResponse = duckItUseCase.fetchPosts()

        // verify
        Assertions.assertEquals(
            MockData.mockTrueResponseWrapper,
            postsResponse
        )
        Mockito.verify(mockDuckItRepository, Mockito.times(1))
            .addDuckItPostsToDB(MockData.mockPosts)
    }

    @Test
    fun `fetchPosts return onError flow`(): Unit = runBlocking {

        // when
        Mockito.`when`(mockDuckItRepository.getPosts())
            .thenReturn(MockData.mockPostsResponseErrorHttpBadRequest)

        // then
        setupDuckItUseCase()
        val postsResponse = duckItUseCase.fetchPosts()

        // verify
        Assertions.assertEquals(
            MockData.mockBooleanResponseWrapperGenericError,
            postsResponse
        )
    }

    @Test
    fun `getPosts happy success flow`(): Unit = runBlocking {

        // given
        val mockPostsFlow: Flow<Posts> = flow {
            emit(MockData.mockPosts)
        }

        // when
        Mockito.`when`(mockDuckItRepository.getDuckItPostsFlow()).thenReturn(mockPostsFlow)

        // then
        setupDuckItUseCase()
        duckItUseCase.getPosts { posts ->

            // verify
            Assertions.assertEquals(
                MockData.mockPostsResponseWrapper,
                posts
            )
        }
    }

    @Test
    fun `getPosts throw exception`(): Unit = runBlocking {

        // given
        val mockPostsFlow: Flow<Posts> = flow {
            throw Exception("There was a problem")
        }

        // when
        Mockito.`when`(mockDuckItRepository.getDuckItPostsFlow()).thenReturn(mockPostsFlow)

        // then
        setupDuckItUseCase()
        duckItUseCase.getPosts { posts ->

            // verify
            Assertions.assertEquals(
                MockData.mockPostsResponseWrapperGenericError,
                posts
            )
        }
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
