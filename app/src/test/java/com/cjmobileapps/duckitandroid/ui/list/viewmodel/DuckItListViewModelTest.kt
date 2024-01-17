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
    fun `init happy flow`(): Unit = runTest {

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
}
