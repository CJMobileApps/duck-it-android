package com.cjmobileapps.duckitandroid.ui.list.viewmodel

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.duckit.DuckItUseCase
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.data.model.ResponseWrapper
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import com.cjmobileapps.duckitandroid.testutil.TestCoroutineDispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.given
import java.io.IOException

class DuckItListViewModelTest : BaseTest() {

    private lateinit var duckItListViewModel: DuckItListViewModel

    @Mock
    private lateinit var mockDuckItUseCase: DuckItUseCase

    @Mock
    private lateinit var mockAccountUseCase: AccountUseCase

    private val postsResponseWrapperArgumentCaptor =
        argumentCaptor<(ResponseWrapper<Posts>) -> Unit>()

    private val duckItTokenFlowResponseWrapperArgumentCaptor =
        argumentCaptor<(isUserLoggedIn: Boolean) -> Unit>()

    private fun setupDuckItListViewModel() {
        duckItListViewModel = DuckItListViewModelImpl(
            duckItUseCase = mockDuckItUseCase,
            accountUseCase = mockAccountUseCase,
            coroutineDispatchers = TestCoroutineDispatchers
        )
    }

    @Test
    fun `init getPosts onSuccess happy flow`(): Unit = runTest {

        // then init setup
        setupDuckItListViewModel()
        var duckItListState = duckItListViewModel.getState()

        // verify in loading state
        Assertions.assertTrue((duckItListState is DuckItListViewModelImpl.DuckItListState.LoadingState))

        // when
        Mockito.`when`(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture()))
            .thenReturn(Unit)

        Mockito.`when`(
            mockAccountUseCase.initDuckItTokenFlow(duckItTokenFlowResponseWrapperArgumentCaptor.capture())
        )
            .thenReturn(Unit)

        // then
        setupDuckItListViewModel()
        postsResponseWrapperArgumentCaptor.firstValue.invoke(MockData.mockPostsResponseWrapper)
        duckItTokenFlowResponseWrapperArgumentCaptor.firstValue.invoke(true)
        duckItListState = duckItListViewModel.getState()

        // verify
        Assertions.assertTrue((duckItListState is DuckItListViewModelImpl.DuckItListState.DuckItListLoadedState))
        if ((duckItListState !is DuckItListViewModelImpl.DuckItListState.DuckItListLoadedState)) return@runTest

        duckItListState.posts.forEachIndexed { index, postState ->
            Assertions.assertEquals(
                MockData.mockPostsStateObjs[index].id,
                postState.id
            )
            Assertions.assertEquals(
                MockData.mockPostsStateObjs[index].headline,
                postState.headline
            )
            Assertions.assertEquals(
                MockData.mockPostsStateObjs[index].image,
                postState.image
            )
            Assertions.assertEquals(
                MockData.mockPostsStateObjs[index].upvotes.value,
                postState.upvotes.value
            )
            Assertions.assertEquals(
                MockData.mockPostsStateObjs[index].author,
                postState.author
            )
        }

        Assertions.assertTrue(duckItListState.isUserLoggedIn.value)
    }

    @Test
    fun `init getPosts throw CoroutineException flow`(): Unit = runTest {

        // then init setup
        setupDuckItListViewModel()
        var duckItListState = duckItListViewModel.getState()

        // verify in loading state
        Assertions.assertTrue((duckItListState is DuckItListViewModelImpl.DuckItListState.LoadingState))

        // when
        given(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture())).willAnswer {
            throw IOException("There was a problem")
        }

        // then
        setupDuckItListViewModel()
        duckItListState = duckItListViewModel.getState()
        val snackbarState = duckItListViewModel.getSnackbarState()

        // verify
        Assertions.assertTrue((duckItListState is DuckItListViewModelImpl.DuckItListState.LoadingState))
        Assertions.assertTrue((snackbarState is DuckItListViewModelImpl.DuckItSnackbarState.ShowGenericError))
    }

    @Test
    fun `init getPosts onError flow`(): Unit = runTest {

        // then init setup
        setupDuckItListViewModel()
        var duckItListState = duckItListViewModel.getState()

        // verify in loading state
        Assertions.assertTrue((duckItListState is DuckItListViewModelImpl.DuckItListState.LoadingState))

        // when
        Mockito.`when`(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture()))
            .thenReturn(Unit)

        Mockito.`when`(
            mockAccountUseCase.initDuckItTokenFlow(duckItTokenFlowResponseWrapperArgumentCaptor.capture())
        )
            .thenReturn(Unit)

        // then
        setupDuckItListViewModel()
        postsResponseWrapperArgumentCaptor.firstValue.invoke(MockData.mockPostsResponseWrapperGenericError)
        duckItListState = duckItListViewModel.getState()
        val snackbarState = duckItListViewModel.getSnackbarState()

        // verify
        Assertions.assertTrue((duckItListState is DuckItListViewModelImpl.DuckItListState.DuckItListLoadedState))
        if ((duckItListState !is DuckItListViewModelImpl.DuckItListState.DuckItListLoadedState)) return@runTest
        Assertions.assertTrue(duckItListState.posts.isEmpty())
        Assertions.assertTrue((snackbarState is DuckItListViewModelImpl.DuckItSnackbarState.UnableToGetDuckItListError))
    }

    @Test
    fun `upvote isUserLoggedIn happy flow`(): Unit = runTest {

        // when
        Mockito.`when`(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture()))
            .thenReturn(Unit)

        Mockito.`when`(
            mockAccountUseCase.initDuckItTokenFlow(duckItTokenFlowResponseWrapperArgumentCaptor.capture())
        )
            .thenReturn(Unit)
        Mockito.`when`(mockDuckItUseCase.upvote(MockData.mockPostId)).thenReturn(MockData.mockUpVotesResponseSuccess)
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(false)

        // then
        setupDuckItListViewModel()
        postsResponseWrapperArgumentCaptor.firstValue.invoke(MockData.mockPostsResponseWrapper)
        duckItTokenFlowResponseWrapperArgumentCaptor.firstValue.invoke(true)
        duckItListViewModel.upvote(MockData.mockPostId)

        // verify
        Mockito.verify(mockDuckItUseCase, Mockito.times(0)).upvote(MockData.mockPostId)
    }

    @Test
    fun `upvote isUserLoggedIn false flow`(): Unit = runTest {

        // when
        Mockito.`when`(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture()))
            .thenReturn(Unit)

        Mockito.`when`(
            mockAccountUseCase.initDuckItTokenFlow(duckItTokenFlowResponseWrapperArgumentCaptor.capture())
        )
            .thenReturn(Unit)
        Mockito.`when`(mockDuckItUseCase.upvote(MockData.mockPostId)).thenReturn(MockData.mockUpVotesResponseSuccess)
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(false)

        // then
        setupDuckItListViewModel()
        postsResponseWrapperArgumentCaptor.firstValue.invoke(MockData.mockPostsResponseWrapper)
        duckItTokenFlowResponseWrapperArgumentCaptor.firstValue.invoke(true)
        duckItListViewModel.upvote(MockData.mockPostId)
        val snackbarState = duckItListViewModel.getSnackbarState()

        // verify
        Mockito.verify(mockDuckItUseCase, Mockito.times(0)).upvote(MockData.mockPostId)
        Assertions.assertTrue((snackbarState is DuckItListViewModelImpl.DuckItSnackbarState.UserNotLoggedInError))
    }

    @Test
    fun `upvote onError flow`(): Unit = runTest {

        // when
        Mockito.`when`(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture()))
            .thenReturn(Unit)

        Mockito.`when`(
            mockAccountUseCase.initDuckItTokenFlow(duckItTokenFlowResponseWrapperArgumentCaptor.capture())
        )
            .thenReturn(Unit)
        Mockito.`when`(mockDuckItUseCase.upvote(MockData.mockPostId)).thenReturn(MockData.mockUpVotesResponseErrorHttpNotFound)
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(true)

        // then
        setupDuckItListViewModel()
        postsResponseWrapperArgumentCaptor.firstValue.invoke(MockData.mockPostsResponseWrapper)
        duckItTokenFlowResponseWrapperArgumentCaptor.firstValue.invoke(true)
        duckItListViewModel.upvote(MockData.mockPostId)
        val snackbarState = duckItListViewModel.getSnackbarState()

        // verify
        Mockito.verify(mockDuckItUseCase, Mockito.times(1)).upvote(MockData.mockPostId)
        Assertions.assertTrue((snackbarState is DuckItListViewModelImpl.DuckItSnackbarState.UpvoteError))
    }

    @Test
    fun `downvote isUserLoggedIn happy flow`(): Unit = runTest {

        // when
        Mockito.`when`(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture()))
            .thenReturn(Unit)

        Mockito.`when`(
            mockAccountUseCase.initDuckItTokenFlow(duckItTokenFlowResponseWrapperArgumentCaptor.capture())
        )
            .thenReturn(Unit)
        Mockito.`when`(mockDuckItUseCase.downvote(MockData.mockPostId)).thenReturn(MockData.mockUpVotesResponseSuccess)
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(false)

        // then
        setupDuckItListViewModel()
        postsResponseWrapperArgumentCaptor.firstValue.invoke(MockData.mockPostsResponseWrapper)
        duckItTokenFlowResponseWrapperArgumentCaptor.firstValue.invoke(true)
        duckItListViewModel.downvote(MockData.mockPostId)

        // verify
        Mockito.verify(mockDuckItUseCase, Mockito.times(0)).downvote(MockData.mockPostId)
    }

    @Test
    fun `downvote isUserLoggedIn false flow`(): Unit = runTest {

        // when
        Mockito.`when`(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture()))
            .thenReturn(Unit)

        Mockito.`when`(
            mockAccountUseCase.initDuckItTokenFlow(duckItTokenFlowResponseWrapperArgumentCaptor.capture())
        )
            .thenReturn(Unit)
        Mockito.`when`(mockDuckItUseCase.downvote(MockData.mockPostId)).thenReturn(MockData.mockUpVotesResponseSuccess)
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(false)

        // then
        setupDuckItListViewModel()
        postsResponseWrapperArgumentCaptor.firstValue.invoke(MockData.mockPostsResponseWrapper)
        duckItTokenFlowResponseWrapperArgumentCaptor.firstValue.invoke(true)
        duckItListViewModel.downvote(MockData.mockPostId)
        val snackbarState = duckItListViewModel.getSnackbarState()

        // verify
        Mockito.verify(mockDuckItUseCase, Mockito.times(0)).downvote(MockData.mockPostId)
        Assertions.assertTrue((snackbarState is DuckItListViewModelImpl.DuckItSnackbarState.UserNotLoggedInError))
    }

    @Test
    fun `downvote onError flow`(): Unit = runTest {

        // when
        Mockito.`when`(mockDuckItUseCase.getPosts(postsResponseWrapperArgumentCaptor.capture()))
            .thenReturn(Unit)

        Mockito.`when`(
            mockAccountUseCase.initDuckItTokenFlow(duckItTokenFlowResponseWrapperArgumentCaptor.capture())
        )
            .thenReturn(Unit)
        Mockito.`when`(mockDuckItUseCase.downvote(MockData.mockPostId)).thenReturn(MockData.mockUpVotesResponseErrorHttpNotFound)
        Mockito.`when`(mockAccountUseCase.isUserLoggedIn).thenReturn(true)

        // then
        setupDuckItListViewModel()
        postsResponseWrapperArgumentCaptor.firstValue.invoke(MockData.mockPostsResponseWrapper)
        duckItTokenFlowResponseWrapperArgumentCaptor.firstValue.invoke(true)
        duckItListViewModel.downvote(MockData.mockPostId)
        val snackbarState = duckItListViewModel.getSnackbarState()

        // verify
        Mockito.verify(mockDuckItUseCase, Mockito.times(1)).downvote(MockData.mockPostId)
        Assertions.assertTrue((snackbarState is DuckItListViewModelImpl.DuckItSnackbarState.DownvoteError))
    }
}
