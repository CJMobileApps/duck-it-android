package com.cjmobileapps.duckitandroid.ui.newpost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cjmobileapps.duckitandroid.R
import com.cjmobileapps.duckitandroid.ui.DuckItTopAppBar
import com.cjmobileapps.duckitandroid.ui.NavItem
import com.cjmobileapps.duckitandroid.ui.newpost.viewmodel.NewPostViewModel
import com.cjmobileapps.duckitandroid.ui.newpost.viewmodel.NewPostViewModelImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NewPostUi(
    navController: NavController,
    newPostViewModel: NewPostViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            DuckItTopAppBar(
                navController = navController,
                userLoggedInState = newPostViewModel.userLoggedInState()
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when (newPostViewModel.getState()) {
            is NewPostViewModelImpl.NewPostState.NewPostLoadedState -> {
                NewPostLoadedUi(
                    modifier = Modifier.padding(innerPadding),
                    newPostViewModel = newPostViewModel,
                    navController = navController,
                    isLoading = newPostViewModel.isLoading()
                )
            }
        }

        when (val state = newPostViewModel.getSnackbarState()) {
            is NewPostViewModelImpl.NewPostSnackbarState.Idle -> {}
            is NewPostViewModelImpl.NewPostSnackbarState.NewPostCreated -> {
                NewPostSnackbar(
                    message = stringResource(R.string.new_post_created),
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    newPostViewModel = newPostViewModel
                )
            }

            is NewPostViewModelImpl.NewPostSnackbarState.ShowGenericError -> {
                NewPostSnackbar(
                    message =
                    if (!state.error.isNullOrEmpty()) state.error
                    else stringResource(R.string.some_error_occurred),
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    newPostViewModel = newPostViewModel
                )
            }
        }
    }
}

@Composable
fun NewPostSnackbar(
    message: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    newPostViewModel: NewPostViewModel
) {
    LaunchedEffect(key1 = message) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = message)
            newPostViewModel.resetSnackbarState()
        }
    }
}

@Composable
fun NewPostLoadedUi(
    modifier: Modifier,
    newPostViewModel: NewPostViewModel,
    navController: NavController,
    isLoading: Boolean
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(""),
                value = newPostViewModel.getHeadlineEditText(),
                onValueChange = { newPostViewModel.updateHeadlineEditText(it) },
                label = { Text(stringResource(R.string.headline)) }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(""),
                value = newPostViewModel.getImageUrlEditText(),
                onValueChange = { newPostViewModel.updateImageUrlEditText(it) },
                label = { Text(stringResource(R.string.image_url)) }
            )

            Button(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                enabled = newPostViewModel.isCreateNewPostButtonEnabled(),
                onClick = { newPostViewModel.createNewPostButtonClicked() }
            ) {
                Text(text = stringResource(R.string.create_new_post))
            }
        }
    }

    when (newPostViewModel.getNewPostNavRouteUiState()) {
        NewPostViewModelImpl.NewPostNavRouteUi.Idle -> {}
        NewPostViewModelImpl.NewPostNavRouteUi.GoToLogInScreenUi -> {
            navController.navigate(NavItem.List.navRoute) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            newPostViewModel.resetNavRouteUiToIdle()
        }
    }
}
