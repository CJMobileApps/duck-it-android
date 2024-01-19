package com.cjmobileapps.duckitandroid.ui.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cjmobileapps.duckitandroid.R
import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.model.PostState
import com.cjmobileapps.duckitandroid.ui.DuckItTopAppBar
import com.cjmobileapps.duckitandroid.ui.NavItem
import com.cjmobileapps.duckitandroid.ui.list.viewmodel.DuckItListViewModel
import com.cjmobileapps.duckitandroid.ui.list.viewmodel.DuckItListViewModelImpl
import com.cjmobileapps.duckitandroid.ui.list.viewmodel.DuckItListViewModelImpl.DuckItListState.*
import com.cjmobileapps.duckitandroid.ui.theme.DuckItAndroidTheme
import com.cjmobileapps.duckitandroid.ui.theme.DuckItBlackLong
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

        val pullToRefreshState = rememberPullToRefreshState()

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(pullToRefreshState.isRefreshing) {
                duckItListViewModel.refresh()
            }
        }

        Box(Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
            when (val state = duckItListViewModel.getState()) {
                is LoadingState -> {
                    LaunchedEffect(!pullToRefreshState.isRefreshing) {
                        pullToRefreshState.startRefresh()
                    }
                    DuckItListLoadingUi(modifier = Modifier.padding(innerPadding))
                }

                is DuckItListLoadedState -> {
                    DuckItListLoadedUi(
                        modifier = Modifier
                            .padding(innerPadding),
                        duckItListLoadedState = state,
                        duckItListViewModel = duckItListViewModel,
                        navController = navController,
                        pullToRefreshState = pullToRefreshState
                    )
                }
            }
            PullToRefreshContainer(
                modifier = Modifier
                    .padding(innerPadding)
                    .align(Alignment.TopCenter),
                state = pullToRefreshState,
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }

        val snackbarMessage: String? = when (val state = duckItListViewModel.getSnackbarState()) {
            is DuckItListViewModelImpl.DuckItSnackbarState.Idle -> null
            is DuckItListViewModelImpl.DuckItSnackbarState.ShowGenericError -> state.error
                ?: stringResource(R.string.some_error_occurred)

            is DuckItListViewModelImpl.DuckItSnackbarState.DownvoteError -> state.error
                ?: stringResource(R.string.error_downvoting)

            is DuckItListViewModelImpl.DuckItSnackbarState.UpvoteError -> state.error
                ?: stringResource(R.string.error_upvoting)

            is DuckItListViewModelImpl.DuckItSnackbarState.UnableToGetDuckItListError -> state.error
                ?: stringResource(R.string.unable_to_get_duckit_list)

            is DuckItListViewModelImpl.DuckItSnackbarState.UserNotLoggedInError -> state.error
                ?: stringResource(R.string.you_need_to_log_in_first)

            is DuckItListViewModelImpl.DuckItSnackbarState.SignedOut -> stringResource(R.string.user_logged_out)
        }

        if (snackbarMessage != null) {
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
            duckItListViewModel.resetSnackbarState()
        }
    }
}

@Composable
fun DuckItListLoadingUi(
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.no_ducks_found),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuckItListLoadedUi(
    modifier: Modifier,
    duckItListLoadedState: DuckItListLoadedState,
    duckItListViewModel: DuckItListViewModel,
    navController: NavController,
    pullToRefreshState: PullToRefreshState
) {
    val posts = duckItListLoadedState.posts

    DuckItListUi(
        modifier = modifier,
        posts = posts,
        lastIndex = duckItListLoadedState.posts.lastIndex,
        onUpvoteButtonClicked = { duckItListViewModel.upvote(postId = it) },
        onDownvoteButtonClicked = { duckItListViewModel.downvote(postId = it) }
    )

    when (duckItListViewModel.getDuckItListNavRouteUiState()) {
        is DuckItListViewModelImpl.DuckItListNavRouteUi.Idle -> {}
        is DuckItListViewModelImpl.DuckItListNavRouteUi.GoToLogInScreenUi -> {
            navController.navigate(NavItem.LogIn.navRoute)
            duckItListViewModel.resetNavRouteUiToIdle()
        }
    }

    if (!duckItListLoadedState.isRefreshLoading.value) {
        pullToRefreshState.endRefresh()

        LaunchedEffect(!duckItListLoadedState.isRefreshLoading.value) {
            pullToRefreshState.endRefresh()
        }
    }
}

@Composable
fun DuckItListUi(
    modifier: Modifier,
    posts: List<PostState>,
    lastIndex: Int,
    onUpvoteButtonClicked: (postId: String) -> Unit,
    onDownvoteButtonClicked: (postId: String) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(
            items = posts,
            key = { _, item: PostState ->
                item.id ?: ""
            },
            itemContent = { index, postState ->
                DuckItItemContentUi(
                    postState = postState,
                    index = index,
                    lastIndex = lastIndex,
                    onUpvoteButtonClicked = { onUpvoteButtonClicked.invoke(postState.id ?: "") },
                    onDownvoteButtonClicked = { onDownvoteButtonClicked.invoke(postState.id ?: "") }
                )
            })
    }
}

@Preview(showBackground = true)
@Composable
fun DuckItListUiPreview() = DuckItAndroidTheme {
    DuckItListUi(
        modifier = Modifier,
        posts = MockData.mockPostsStateObjs,
        lastIndex = MockData.mockPostsStateObjs.lastIndex,
        onUpvoteButtonClicked = { },
        onDownvoteButtonClicked = { }
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = DuckItBlackLong
)
@Composable
fun DuckItListUiDarkPreview() = DuckItAndroidTheme {
    DuckItListUi(
        modifier = Modifier,
        posts = MockData.mockPostsStateObjs,
        lastIndex = MockData.mockPostsStateObjs.lastIndex,
        onUpvoteButtonClicked = { },
        onDownvoteButtonClicked = { }
    )
}

@Composable
fun DuckItItemContentUi(
    postState: PostState,
    index: Int,
    lastIndex: Int,
    onUpvoteButtonClicked: () -> Unit,
    onDownvoteButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 0.dp,
                top = 4.dp,
                bottom = 16.dp
            ),
            text = postState.headline ?: "",
            style = MaterialTheme.typography.headlineSmall
        )

        AsyncImage(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(160.dp),
            model = postState.image,
            contentDescription = stringResource(R.string.duck_image),
            fallback = painterResource(id = R.drawable.duck),
            placeholder = painterResource(id = R.drawable.duck),
            error = painterResource(id = R.drawable.duck)
        )

        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onUpvoteButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward,
                    contentDescription = stringResource(R.string.upvote_button),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                modifier = Modifier.padding(0.dp),
                text = postState.upvotes.value.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(onClick = onDownvoteButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowDownward,
                    contentDescription = stringResource(R.string.downvote_button),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (index < lastIndex) {
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DuckItItemContentUiPreview() = DuckItAndroidTheme {
    DuckItItemContentUi(
        postState = MockData.mockPostsStateObjs.first(),
        index = 0,
        lastIndex = 10,
        onUpvoteButtonClicked = { },
        onDownvoteButtonClicked = { }
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = DuckItBlackLong
)
@Composable
fun NewPostMainContentUiDarkPreview() = DuckItAndroidTheme {
    DuckItItemContentUi(
        postState = MockData.mockPostsStateObjs.first(),
        index = 0,
        lastIndex = 10,
        onUpvoteButtonClicked = { },
        onDownvoteButtonClicked = { }
    )
}
