package com.cjmobileapps.duckitandroid.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cjmobileapps.duckitandroid.R
import com.cjmobileapps.duckitandroid.data.model.PostState
import com.cjmobileapps.duckitandroid.ui.DuckItTopAppBar
import com.cjmobileapps.duckitandroid.ui.NavItem
import com.cjmobileapps.duckitandroid.ui.list.viewmodel.DuckItListViewModel
import com.cjmobileapps.duckitandroid.ui.list.viewmodel.DuckItListViewModelImpl
import com.cjmobileapps.duckitandroid.ui.list.viewmodel.DuckItListViewModelImpl.DuckItListState.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuckItListUi(
    navController: NavController,
    duckItListViewModel: DuckItListViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            DuckItTopAppBar(
                navController = navController,
                userLoggedInState = duckItListViewModel.userLoggedInState(),
                onIsUserLoggedInButtonClick = {
                    duckItListViewModel.isUserLoggedInButtonClicked()
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when (val state = duckItListViewModel.getState()) {
            is LoadingState -> {
                DuckItListLoadingUi(modifier = Modifier.padding(innerPadding))
            }

            is DuckItListLoadedState -> {
                DuckItListLoadedUi(
                    modifier = Modifier.padding(innerPadding),
                    duckItListLoadedState = state,
                    duckItListViewModel = duckItListViewModel,
                    navController = navController
                )
            }
        }

        val snackbarMessage: String? = when (val state = duckItListViewModel.getSnackbarState()) {
            is DuckItListViewModelImpl.DuckItSnackbarState.Idle -> null
            is DuckItListViewModelImpl.DuckItSnackbarState.ShowGenericError -> state.error ?: stringResource(R.string.some_error_occurred)
            is DuckItListViewModelImpl.DuckItSnackbarState.DownvoteError -> state.error ?: stringResource(R.string.error_downvoting)
            is DuckItListViewModelImpl.DuckItSnackbarState.UpvoteError -> state.error ?: stringResource(R.string.error_upvoting)
            is DuckItListViewModelImpl.DuckItSnackbarState.UnableToGetDuckItListError -> state.error ?: stringResource(R.string.unable_to_get_duckit_list)
            is DuckItListViewModelImpl.DuckItSnackbarState.UserNotLoggedInError -> state.error ?: stringResource(R.string.you_need_to_log_in_first)
            is DuckItListViewModelImpl.DuckItSnackbarState.SignedOut -> stringResource(R.string.user_logged_out)
        }

        if(snackbarMessage != null) {
            DuckItListSnackbar(
                message = snackbarMessage,
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                duckItListViewModel = duckItListViewModel
            )
        }
    }
}

@Composable
fun DuckItListSnackbar(
    message: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    duckItListViewModel: DuckItListViewModel
) {
    LaunchedEffect(key1 = message) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = message)
            duckItListViewModel.resetErrorState()
        }
    }
}

@Composable
fun DuckItListLoadingUi(
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.no_ducks_found),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun DuckItListLoadedUi(
    modifier: Modifier,
    duckItListLoadedState: DuckItListLoadedState,
    duckItListViewModel: DuckItListViewModel,
    navController: NavController
) {
    val posts = duckItListLoadedState.posts

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(
            items = posts,
            key = { _, item: PostState ->
                item.id ?: ""
            },
            itemContent = { index, item ->
                Column(
                    modifier = Modifier.padding(0.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 0.dp,
                            top = 0.dp,
                            bottom = 16.dp
                        ),
                        text = item.headline ?: "",
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    AsyncImage(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(160.dp),
                        model = item.image,
                        contentDescription = stringResource(R.string.duck_image),
                        fallback = painterResource(id = R.drawable.duck_svgrepo_com),
                        placeholder = painterResource(id = R.drawable.duck_svgrepo_com),
                        error = painterResource(id = R.drawable.duck_svgrepo_com)
                    )

                    Row(
                        modifier = Modifier.padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            duckItListViewModel.upvote(postId = item.id ?: "")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowUpward,
                                contentDescription = stringResource(R.string.upvote_button)
                            )
                        }

                        Text(
                            modifier = Modifier.padding(0.dp),
                            text = item.upvotes.value.toString(),
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        IconButton(onClick = {
                            duckItListViewModel.downvote(postId = item.id ?: "")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDownward,
                                contentDescription = stringResource(R.string.downvote_button)
                            )
                        }
                    }

                    if (index < duckItListLoadedState.posts.lastIndex) {
                        Divider()
                    }
                }
            })
    }

    when (duckItListViewModel.getDuckItListNavRouteUiState()) {
        is DuckItListViewModelImpl.DuckItListNavRouteUi.Idle -> {}
        is DuckItListViewModelImpl.DuckItListNavRouteUi.GoToLogInScreenUi -> {
            navController.navigate(NavItem.LogIn.navRoute)
            duckItListViewModel.resetNavRouteUiToIdle()
        }
    }
}
