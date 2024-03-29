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

        // then
        setupLogInViewModel()

        // verify
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
    fun `email not empty so logInButtonEnabled not enabled flow`() {

        // then
        setupLogInViewModel()
        logInViewModel.updateEmailEditText(MockData.mockEmailPasswordRequest.email)

        // verify
        Assertions.assertFalse(logInViewModel.isLogInButtonEnabled())
    }

    @Test
    fun `password not empty so logInButtonEnabled not enabled flow`() {

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

    @Test
    fun `enable logInButtonEnabled then loginButtonClicked accountSignedIn`(): Unit = runTest {

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
        Mockito.`when`(mockAccountUseCase.signIn(MockData.mockEmailPasswordRequest)).thenReturn(MockData.mockAccountStateAccountSignedInResponseWrapper)

        // then
        logInViewModel.loginButtonClicked()
        val snackbarState = logInViewModel.getSnackbarState()
        val logInNavRouteUiState = logInViewModel.getLogInNavRouteUiState()

        // verify
        Assertions.assertFalse(logInViewModel.isLoading())
        Assertions.assertTrue(snackbarState is LogInViewModelImpl.LoginSnackbarState.AccountSignedIn)
        Assertions.assertTrue(logInNavRouteUiState is LogInViewModelImpl.LogInNavRouteUi.GoToListScreenUi)
    }

    @Test
    fun `enable logInButtonEnabled then loginButtonClicked signIn onError`(): Unit = runTest {

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
        Mockito.`when`(mockAccountUseCase.signIn(MockData.mockEmailPasswordRequest)).thenReturn(MockData.mockAccountStateGenericErrorResponseWrapper)

        // then
        logInViewModel.loginButtonClicked()
        val snackbarState = logInViewModel.getSnackbarState()
        val logInNavRouteUiState = logInViewModel.getLogInNavRouteUiState()

        // verify
        Assertions.assertFalse(logInViewModel.isLoading())
        Assertions.assertTrue(snackbarState is LogInViewModelImpl.LoginSnackbarState.ShowGenericError)
        Assertions.assertFalse(logInNavRouteUiState is LogInViewModelImpl.LogInNavRouteUi.GoToListScreenUi)
    }
    @Test
    fun `enable logInButtonEnabled then loginButtonClicked accountSignedIn then resetSnackbarState & resetNavRouteUiToIdle`(): Unit = runTest {

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
        Mockito.`when`(mockAccountUseCase.signIn(MockData.mockEmailPasswordRequest)).thenReturn(MockData.mockAccountStateAccountSignedInResponseWrapper)

        // then
        logInViewModel.loginButtonClicked()
        var snackbarState = logInViewModel.getSnackbarState()
        var logInNavRouteUiState = logInViewModel.getLogInNavRouteUiState()

        // verify
        Assertions.assertFalse(logInViewModel.isLoading())
        Assertions.assertTrue(snackbarState is LogInViewModelImpl.LoginSnackbarState.AccountSignedIn)
        Assertions.assertTrue(logInNavRouteUiState is LogInViewModelImpl.LogInNavRouteUi.GoToListScreenUi)

        // then reset
        logInViewModel.resetSnackbarState()
        logInViewModel.resetNavRouteUiToIdle()
        snackbarState = logInViewModel.getSnackbarState()
        logInNavRouteUiState = logInViewModel.getLogInNavRouteUiState()

        // verify
        Assertions.assertTrue(snackbarState is LogInViewModelImpl.LoginSnackbarState.Idle)
        Assertions.assertTrue(logInNavRouteUiState is LogInViewModelImpl.LogInNavRouteUi.Idle)
    }
}
