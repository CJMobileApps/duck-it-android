package com.cjmobileapps.duckitandroid.ui.login.viewmodel

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import com.cjmobileapps.duckitandroid.testutil.TestCoroutineDispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class LogInViewModelTest : BaseTest() {

    private lateinit var logInViewModel: LogInViewModel

    @Mock
    private lateinit var mockAccountUseCase: AccountUseCase

    private fun setupLogInViewModel() {
        logInViewModel = LogInViewModelImpl(
            coroutineDispatchers = TestCoroutineDispatchers,
            accountUseCase = mockAccountUseCase
        )
    }

    @Test
    fun `userLoggedInState is UserLoggedInState DontShowUserLoggedIn`() {
        setupLogInViewModel()
        Assertions.assertTrue(logInViewModel.userLoggedInState() == UserLoggedInState.DontShowUserLoggedIn)
    }

    @Test
    fun `init logInButtonEnabled not enabled flow`() {

        // then
        setupLogInViewModel()

        // verify
        Assertions.assertFalse(logInViewModel.isLogInButtonEnabled())
    }

    @Test
    fun `email not empty but logInButtonEnabled not enabled flow`() {

        // then
        setupLogInViewModel()
        logInViewModel.updateEmailEditText(MockData.mockEmailPasswordRequest.email)

        // verify
        Assertions.assertFalse(logInViewModel.isLogInButtonEnabled())
    }

    @Test
    fun `password not empty but logInButtonEnabled not enabled flow`() {

        // then
        setupLogInViewModel()
        logInViewModel.updatePasswordEditText(MockData.mockEmailPasswordRequest.password)

        // verify
        Assertions.assertFalse(logInViewModel.isLogInButtonEnabled())
    }


    @Test
    fun `enable logInButtonEnabled then loginButtonClicked accountCreated`(): Unit = runTest {

        // then
        setupLogInViewModel()
        logInViewModel.updateEmailEditText(MockData.mockEmailPasswordRequest.email)
        logInViewModel.updatePasswordEditText(MockData.mockEmailPasswordRequest.password)

        val email = logInViewModel.getEmailEditText()
        val password = logInViewModel.getPasswordEditText()

        // verify
        Assertions.assertTrue(logInViewModel.isLogInButtonEnabled())
        Assertions.assertEquals(
            MockData.mockEmailPasswordRequest.email,
            email
        )
        Assertions.assertEquals(
            MockData.mockEmailPasswordRequest.password,
            password
        )

        // when
        Mockito.`when`(mockAccountUseCase.signIn(MockData.mockEmailPasswordRequest)).thenReturn(MockData.mockAccountStateAccountCreatedResponseWrapper)

        // then
        logInViewModel.loginButtonClicked()
        val snackbarState = logInViewModel.getSnackbarState()
        val logInNavRouteUiState = logInViewModel.getLogInNavRouteUiState()

        // verify
        Assertions.assertFalse(logInViewModel.isLoading())
        Assertions.assertTrue(snackbarState is LogInViewModelImpl.LoginSnackbarState.AccountCreated)
        Assertions.assertTrue(logInNavRouteUiState is LogInViewModelImpl.LogInNavRouteUi.GoToListScreenUi)
    }


}
