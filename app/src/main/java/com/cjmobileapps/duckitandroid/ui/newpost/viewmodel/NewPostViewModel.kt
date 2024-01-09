package com.cjmobileapps.duckitandroid.ui.newpost.viewmodel

import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState

interface NewPostViewModel {

    fun getState(): NewPostViewModelImpl.NewPostState

    fun getSnackbarState(): NewPostViewModelImpl.NewPostSnackbarState

    fun getHeadlineEditText(): String

    fun updateHeadlineEditText(headline: String)

    fun getImageUrlEditText(): String

    fun updateImageUrlEditText(imageUrl: String)

    fun isCreateNewPostButtonEnabled(): Boolean

    fun createNewPostButtonClicked()

    fun resetSnackbarState()

    fun getNewPostNavRouteUiState(): NewPostViewModelImpl.NewPostNavRouteUi

    fun resetNavRouteUiToIdle()

    fun userLoggedInState(): UserLoggedInState
}
