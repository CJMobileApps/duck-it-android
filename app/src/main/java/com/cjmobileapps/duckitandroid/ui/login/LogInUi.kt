package com.cjmobileapps.duckitandroid.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cjmobileapps.duckitandroid.R
import com.cjmobileapps.duckitandroid.ui.DuckItTopAppBar
import com.cjmobileapps.duckitandroid.ui.NavItem
import com.cjmobileapps.duckitandroid.ui.login.viewmodel.LogInViewModel
import com.cjmobileapps.duckitandroid.ui.login.viewmodel.LogInViewModelImpl
import com.cjmobileapps.duckitandroid.ui.views.EmailPasswordUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInUi(
    navController: NavController,
    logInViewModel: LogInViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            DuckItTopAppBar(
                navController = navController,
                userLoggedInState = logInViewModel.userLoggedInState()
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when (logInViewModel.getState()) {
            is LogInViewModelImpl.LogInState.LogInLoadedState -> {
                LogInLoadedUi(
                    modifier = Modifier.padding(innerPadding),
                    logInViewModel = logInViewModel,
                    navController = navController
                )
            }
        }

        when (val state = logInViewModel.getSnackbarState()) {
            is LogInViewModelImpl.LoginSnackbarState.Idle -> {}
            is LogInViewModelImpl.LoginSnackbarState.ShowGenericError -> {
                LogInSnackbar(
                    message = state.error ?: stringResource(R.string.some_error_occurred),
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    logInViewModel = logInViewModel
                )
            }

            LogInViewModelImpl.LoginSnackbarState.AccountCreated -> {
                LogInSnackbar(
                    message = stringResource(R.string.account_created),
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    logInViewModel = logInViewModel
                )
            }

            LogInViewModelImpl.LoginSnackbarState.AccountSignedIn -> {
                LogInSnackbar(
                    message = stringResource(R.string.account_signed_in),
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    logInViewModel = logInViewModel
                )
            }
        }
    }
}

@Composable
fun LogInSnackbar(
    message: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    logInViewModel: LogInViewModel
) {
    LaunchedEffect(key1 = message) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = message)
            logInViewModel.resetErrorState()
        }
    }
}

@Composable
fun LogInLoadedUi(
    modifier: Modifier,
    logInViewModel: LogInViewModel,
    navController: NavController
) {
    Column(modifier = modifier.fillMaxWidth()) {
        EmailPasswordUi(
            modifier = modifier,
            emailText = logInViewModel.getEmailEditText(),
            onEmailValueChange = {
                logInViewModel.updateEmailEditText(it)
            },
            passwordText = logInViewModel.getPasswordEditText(),
            onPasswordValueChange = {
                logInViewModel.updatePasswordEditText(it)
            },
            loginButtonText = stringResource(R.string.log_in),
            isLogInButtonEnabled = logInViewModel.isLogInButtonEnabled(),
            loginButtonClicked = { logInViewModel.loginButtonClicked() }
        )
    }

    when (logInViewModel.getLogInNavRouteUiState()) {
        is LogInViewModelImpl.LogInNavRouteUi.Idle -> {}
        is LogInViewModelImpl.LogInNavRouteUi.GoToListScreenUi -> {
            navController.navigate(NavItem.List.navRoute) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            logInViewModel.resetNavRouteUiToIdle()
        }
    }
}
