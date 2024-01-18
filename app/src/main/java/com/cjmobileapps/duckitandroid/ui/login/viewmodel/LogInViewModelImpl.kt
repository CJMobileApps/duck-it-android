package com.cjmobileapps.duckitandroid.ui.login.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.model.AccountState
import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest
import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState
import com.cjmobileapps.duckitandroid.data.model.onError
import com.cjmobileapps.duckitandroid.data.model.onSuccess
import com.cjmobileapps.duckitandroid.util.coroutine.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogInViewModelImpl @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val accountUseCase: AccountUseCase
) : ViewModel(), LogInViewModel {

    private val compositeJob = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag(tag)
            .e("coroutineExceptionHandler() error occurred: $throwable \n ${throwable.message}")
        stopLoading()
        snackbarState.value = LoginSnackbarState.ShowGenericError()
    }

    private val coroutineContext =
        compositeJob + coroutineDispatchers.main + exceptionHandler + SupervisorJob()

    private val logInState = mutableStateOf<LogInState>(LogInState.LogInLoadedState())

    private val snackbarState = mutableStateOf<LoginSnackbarState>(LoginSnackbarState.Idle)

    private val tag = LogInViewModelImpl::class.java.simpleName

    override fun getState() = logInState.value

    override fun getSnackbarState() = snackbarState.value

    override fun getEmailEditText(): String {
        val state = (getState() as LogInState.LogInLoadedState)
        return state.emailEditText.value
    }

    override fun updateEmailEditText(email: String) {
        val state = (getState() as LogInState.LogInLoadedState)
        state.emailEditText.value = email
    }

    override fun getPasswordEditText(): String {
        val state = (getState() as LogInState.LogInLoadedState)
        return state.passwordEditText.value
    }

    override fun updatePasswordEditText(password: String) {
        val state = (getState() as LogInState.LogInLoadedState)
        state.passwordEditText.value = password
    }

    override fun isLogInButtonEnabled(): Boolean {
        val state = (getState() as LogInState.LogInLoadedState)
        return state.emailEditText.value.isNotEmpty() && state.passwordEditText.value.isNotEmpty()
    }

    override fun loginButtonClicked() {
        val state = (getState() as LogInState.LogInLoadedState)

        viewModelScope.launch(coroutineContext) {
            val email = state.emailEditText.value
            val password = state.passwordEditText.value

            if (email.isEmpty() || password.isEmpty()) return@launch

            val emailPasswordRequest = EmailPasswordRequest(
                email = email,
                password = password
            )
            state.isLoading.value = true

            accountUseCase.signIn(emailPasswordRequest)
                ?.onSuccess { accountState ->
                    val loginSnackbarState = when (accountState) {
                        AccountState.AccountSignedIn -> LoginSnackbarState.AccountSignedIn
                        AccountState.AccountCreated -> LoginSnackbarState.AccountCreated
                    }

                    stopLoading()
                    snackbarState.value = loginSnackbarState
                    state.logInNavRouteUi.value = LogInNavRouteUi.GoToListScreenUi
                }
                ?.onError { error ->
                    stopLoading()
                    snackbarState.value = LoginSnackbarState.ShowGenericError(error = error)
                }
        }
    }

    override fun resetSnackbarState() {
        snackbarState.value = LoginSnackbarState.Idle
    }

    override fun getLogInNavRouteUiState(): LogInNavRouteUi {
        val state = (getState() as LogInState.LogInLoadedState)
        return state.logInNavRouteUi.value
    }

    override fun resetNavRouteUiToIdle() {
        val state = (getState() as LogInState.LogInLoadedState)
        state.logInNavRouteUi.value = LogInNavRouteUi.Idle
    }

    override fun userLoggedInState() = UserLoggedInState.DontShowUserLoggedIn

    private fun stopLoading() {
        val state = (getState() as LogInState.LogInLoadedState)
        state.isLoading.value = false
    }

    override fun isLoading(): Boolean {
        val state = (getState() as LogInState.LogInLoadedState)
        return state.isLoading.value
    }

    sealed class LogInState {

        data class LogInLoadedState(
            val emailEditText: MutableState<String> = mutableStateOf(""),
            val passwordEditText: MutableState<String> = mutableStateOf(""),
            val logInNavRouteUi: MutableState<LogInNavRouteUi> = mutableStateOf(LogInNavRouteUi.Idle),
            val isLoading: MutableState<Boolean> = mutableStateOf(false)
        ) : LogInState()
    }

    sealed class LoginSnackbarState {
        object Idle : LoginSnackbarState()

        object AccountSignedIn : LoginSnackbarState()

        object AccountCreated : LoginSnackbarState()

        data class ShowGenericError(
            val error: String? = null
        ) : LoginSnackbarState()
    }

    sealed class LogInNavRouteUi {

        object Idle : LogInNavRouteUi()

        object GoToListScreenUi : LogInNavRouteUi()
    }
}
