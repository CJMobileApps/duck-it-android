package com.cjmobileapps.duckitandroid.ui.newpost.viewmodel

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.duckit.DuckItUseCase
import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import com.cjmobileapps.duckitandroid.testutil.TestCoroutineDispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

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
    fun `headline not empty so createNewPostButtonEnabled not enabled flow`() {

        // then
        setupNewPostViewModel()
        newPostViewModel.updateHeadlineEditText(MockData.mockNewPostRequest.headline)

        // verify
        Assertions.assertFalse(newPostViewModel.isCreateNewPostButtonEnabled())
    }

    @Test
    fun `image url not empty so createNewPostButtonEnabled not enabled flow`() {

        // then
        setupNewPostViewModel()
        newPostViewModel.updateImageUrlEditText(MockData.mockNewPostRequest.image)

        // verify
        Assertions.assertFalse(newPostViewModel.isCreateNewPostButtonEnabled())
    }

    @Test
    fun `enable createNewPostButtonEnabled then createNewPostButtonClicked newPostCreated`(): Unit =
        runTest {

            // then
            setupNewPostViewModel()
            newPostViewModel.updateHeadlineEditText(MockData.mockNewPostRequest.headline)
            newPostViewModel.updateImageUrlEditText(MockData.mockNewPostRequest.image)

            val headline = newPostViewModel.getHeadlineEditText()
            val image = newPostViewModel.getImageUrlEditText()

            // verify
            Assertions.assertTrue(newPostViewModel.isCreateNewPostButtonEnabled())
            Assertions.assertEquals(
                MockData.mockNewPostRequest.headline,
                headline
            )
            Assertions.assertEquals(
                MockData.mockNewPostRequest.image,
                image
            )

            // when
            Mockito.`when`(mockDuckItUseCase.newPost(MockData.mockNewPostRequest))
                .thenReturn(MockData.mockTrueResponseWrapper)

            // then
            newPostViewModel.createNewPostButtonClicked()
            val snackbarState = newPostViewModel.getSnackbarState()
            val logInNavRouteUiState = newPostViewModel.getNewPostNavRouteUiState()

            // verify
            Assertions.assertFalse(newPostViewModel.isLoading())
            Assertions.assertTrue(snackbarState is NewPostViewModelImpl.NewPostSnackbarState.NewPostCreated)
            Assertions.assertTrue(logInNavRouteUiState is NewPostViewModelImpl.NewPostNavRouteUi.GoToLogInScreenUi)
        }

    @Test
    fun `enable createNewPostButtonEnabled then createNewPostButtonClicked newPostCreated onError`(): Unit =
        runTest {

            // then
            setupNewPostViewModel()
            newPostViewModel.updateHeadlineEditText(MockData.mockNewPostRequest.headline)
            newPostViewModel.updateImageUrlEditText(MockData.mockNewPostRequest.image)

            val headline = newPostViewModel.getHeadlineEditText()
            val image = newPostViewModel.getImageUrlEditText()

            // verify
            Assertions.assertTrue(newPostViewModel.isCreateNewPostButtonEnabled())
            Assertions.assertEquals(
                MockData.mockNewPostRequest.headline,
                headline
            )
            Assertions.assertEquals(
                MockData.mockNewPostRequest.image,
                image
            )

            // when
            Mockito.`when`(mockDuckItUseCase.newPost(MockData.mockNewPostRequest))
                .thenReturn(MockData.mockBooleanResponseWrapperGenericError)

            // then
            newPostViewModel.createNewPostButtonClicked()
            val snackbarState = newPostViewModel.getSnackbarState()
            val logInNavRouteUiState = newPostViewModel.getNewPostNavRouteUiState()

            // verify
            Assertions.assertFalse(newPostViewModel.isLoading())
            Assertions.assertTrue(snackbarState is NewPostViewModelImpl.NewPostSnackbarState.ShowGenericError)
            Assertions.assertTrue(logInNavRouteUiState is NewPostViewModelImpl.NewPostNavRouteUi.Idle)
        }

    @Test
    fun `enable createNewPostButtonEnabled then createNewPostButtonClicked newPostCreated then resetSnackbarState & resetNavRouteUiToIdle`(): Unit =
        runTest {

            // then
            setupNewPostViewModel()
            newPostViewModel.updateHeadlineEditText(MockData.mockNewPostRequest.headline)
            newPostViewModel.updateImageUrlEditText(MockData.mockNewPostRequest.image)

            val headline = newPostViewModel.getHeadlineEditText()
            val image = newPostViewModel.getImageUrlEditText()

            // verify
            Assertions.assertTrue(newPostViewModel.isCreateNewPostButtonEnabled())
            Assertions.assertEquals(
                MockData.mockNewPostRequest.headline,
                headline
            )
            Assertions.assertEquals(
                MockData.mockNewPostRequest.image,
                image
            )

            // when
            Mockito.`when`(mockDuckItUseCase.newPost(MockData.mockNewPostRequest))
                .thenReturn(MockData.mockBooleanResponseWrapperGenericError)

            // then
            newPostViewModel.createNewPostButtonClicked()
            var snackbarState = newPostViewModel.getSnackbarState()
            var logInNavRouteUiState = newPostViewModel.getNewPostNavRouteUiState()

            // verify
            Assertions.assertFalse(newPostViewModel.isLoading())
            Assertions.assertTrue(snackbarState is NewPostViewModelImpl.NewPostSnackbarState.ShowGenericError)
            Assertions.assertTrue(logInNavRouteUiState is NewPostViewModelImpl.NewPostNavRouteUi.Idle)

            // then reset
            newPostViewModel.resetSnackbarState()
            newPostViewModel.resetNavRouteUiToIdle()
            snackbarState = newPostViewModel.getSnackbarState()
            logInNavRouteUiState = newPostViewModel.getNewPostNavRouteUiState()
            Assertions.assertTrue(snackbarState is NewPostViewModelImpl.NewPostSnackbarState.Idle)
            Assertions.assertTrue(logInNavRouteUiState is NewPostViewModelImpl.NewPostNavRouteUi.Idle)
        }
}
