package com.cjmobileapps.duckitandroid.ui.list.viewmodel

import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState

interface DuckItListViewModel {

    fun getState(): DuckItListViewModelImpl.DuckItListState

    fun getSnackbarState(): DuckItListViewModelImpl.DuckItSnackbarState

    fun upvote(postId: String)

    fun downvote(postId: String)

    fun resetSnackbarState()

    fun userLoggedInState(): UserLoggedInState

    fun isUserLoggedInButtonClicked()

    fun getDuckItListNavRouteUiState(): DuckItListViewModelImpl.DuckItListNavRouteUi

    fun resetNavRouteUiToIdle()

    fun refresh()
}
