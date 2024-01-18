package com.cjmobileapps.duckitandroid.ui.newpost.viewmodel

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.duckit.DuckItUseCase
import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import com.cjmobileapps.duckitandroid.testutil.TestCoroutineDispatchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock

class NewPostViewModelTest : BaseTest() {

    private lateinit var newPostViewModel: NewPostViewModel

    @Mock
    private lateinit var mockAccountUseCase: AccountUseCase

    @Mock
    private lateinit var mockDuckItUseCase: DuckItUseCase

    private fun setupNewPostViewModel() {
        newPostViewModel = NewPostViewModelImpl(
            coroutineDispatchers = TestCoroutineDispatchers,
            accountUseCase = mockAccountUseCase,
            duckItUseCase = mockDuckItUseCase
        )
    }

    @Test
    fun `userLoggedInState is  UserLoggedInState DontShowUserLoggedIn`() {

        // then
        setupNewPostViewModel()

        // verify
        Assertions.assertTrue(newPostViewModel.userLoggedInState() == UserLoggedInState.DontShowUserLoggedIn)
    }

    @Test
    fun `init isCreateNewPostButtonEnabled not enabled flow`() {

        // then
        setupNewPostViewModel()

        // verify
        Assertions.assertFalse(newPostViewModel.isCreateNewPostButtonEnabled())
    }

    @Test
    fun `headline not empty so logInButtonEnabled not enabled flow`() {

        // then
        setupNewPostViewModel()
        newPostViewModel.updateHeadlineEditText(MockData.mockNewPostRequest.headline)

        // verify
        Assertions.assertFalse(newPostViewModel.isCreateNewPostButtonEnabled())
    }

    @Test
    fun `image url not empty so logInButtonEnabled not enabled flow`() {

        // then
        setupNewPostViewModel()
        newPostViewModel.updateImageUrlEditText(MockData.mockNewPostRequest.image)

        // verify
        Assertions.assertFalse(newPostViewModel.isCreateNewPostButtonEnabled())
    }
}
