package com.cjmobileapps.duckitandroid.ui.login.viewmodel

import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState

interface LogInViewModel {

    fun getState(): LogInViewModelImpl.LogInState

    fun getSnackbarState(): LogInViewModelImpl.LoginSnackbarState

    fun getEmailEditText(): String

    fun updateEmailEditText(email: String)

    fun getPasswordEditText(): String

    fun updatePasswordEditText(password: String)

    fun isLogInButtonEnabled(): Boolean

    fun loginButtonClicked()

    fun resetErrorState()

    fun getLogInNavRouteUiState(): LogInViewModelImpl.LogInNavRouteUi

    fun resetNavRouteUiToIdle()

    fun userLoggedInState(): UserLoggedInState
}
