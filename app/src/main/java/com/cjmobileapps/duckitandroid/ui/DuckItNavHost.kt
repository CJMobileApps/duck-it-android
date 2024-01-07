package com.cjmobileapps.duckitandroid.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cjmobileapps.duckitandroid.ui.newpost.viewmodel.NewPostViewModel
import com.cjmobileapps.duckitandroid.ui.newpost.viewmodel.NewPostViewModelImpl
import com.cjmobileapps.duckitandroid.ui.list.DuckItListUi
import com.cjmobileapps.duckitandroid.ui.list.viewmodel.DuckItListViewModel
import com.cjmobileapps.duckitandroid.ui.list.viewmodel.DuckItListViewModelImpl
import com.cjmobileapps.duckitandroid.ui.login.LogInUi
import com.cjmobileapps.duckitandroid.ui.login.viewmodel.LogInViewModel
import com.cjmobileapps.duckitandroid.ui.login.viewmodel.LogInViewModelImpl
import com.cjmobileapps.duckitandroid.ui.newpost.NewPostUi
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavigationGraph(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    NavHost(navController = navController, startDestination = NavItem.List.navRoute) {
        composable(NavItem.List.navRoute) {
            val duckItListViewModel: DuckItListViewModel = hiltViewModel<DuckItListViewModelImpl>()

            DuckItListUi(
                navController = navController,
                duckItListViewModel = duckItListViewModel,
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState
            )
        }
        composable(NavItem.NewPost.navRoute) {
            val newPostViewModel: NewPostViewModel = hiltViewModel<NewPostViewModelImpl>()

            NewPostUi(
                navController = navController,
                newPostViewModel = newPostViewModel
            )
        }
        composable(NavItem.LogIn.navRoute) {
            val logInViewModel: LogInViewModel = hiltViewModel<LogInViewModelImpl>()

            LogInUi(
                navController = navController,
                logInViewModel = logInViewModel,
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

sealed class NavItem(
    val navRoute: String
) {
    object List : NavItem(navRoute = "nav_list")

    object NewPost : NavItem(navRoute = "nav_new_post")

    object LogIn : NavItem(navRoute = "nav_log_in")
}
