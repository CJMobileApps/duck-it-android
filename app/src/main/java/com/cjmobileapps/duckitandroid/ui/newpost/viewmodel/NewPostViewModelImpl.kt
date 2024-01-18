package com.cjmobileapps.duckitandroid.ui.newpost.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.duckit.DuckItUseCase
import com.cjmobileapps.duckitandroid.data.model.NewPostRequest
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
class NewPostViewModelImpl @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val accountUseCase: AccountUseCase,
    private val duckItUseCase: DuckItUseCase
) : ViewModel(), NewPostViewModel {

    private val compositeJob = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag(tag)
            .e("coroutineExceptionHandler() error occurred: $throwable \n ${throwable.message}")
        stopLoading()
        snackbarState.value = NewPostSnackbarState.ShowGenericError()
    }

    private val coroutineContext =
        compositeJob + coroutineDispatchers.main + exceptionHandler + SupervisorJob()

    private val newPostState = mutableStateOf<NewPostState>(NewPostState.NewPostLoadedState())

    private val snackbarState = mutableStateOf<NewPostSnackbarState>(NewPostSnackbarState.Idle)


    private val tag = NewPostViewModelImpl::class.java.simpleName

    override fun getState() = newPostState.value

    override fun getSnackbarState() = snackbarState.value

    override fun getHeadlineEditText(): String {
        val state = (getState() as NewPostState.NewPostLoadedState)
        return state.headlineEditText.value
    }

    override fun updateHeadlineEditText(headline: String) {
        val state = (getState() as NewPostState.NewPostLoadedState)
        state.headlineEditText.value = headline
    }

    override fun getImageUrlEditText(): String {
        val state = (getState() as NewPostState.NewPostLoadedState)
        return state.imageUrlEditText.value
    }

    override fun updateImageUrlEditText(imageUrl: String) {
        val state = (getState() as NewPostState.NewPostLoadedState)
        state.imageUrlEditText.value = imageUrl
    }

    override fun isCreateNewPostButtonEnabled(): Boolean {
        val state = (getState() as NewPostState.NewPostLoadedState)
        return state.headlineEditText.value.isNotEmpty() && state.imageUrlEditText.value.isNotEmpty()
    }

    override fun createNewPostButtonClicked() {
        viewModelScope.launch(coroutineContext) {
            val state = (getState() as NewPostState.NewPostLoadedState)
            val headline = state.headlineEditText.value
            val imageUrl = state.imageUrlEditText.value

            state.isLoading.value = true

            val newPost = NewPostRequest(
                headline = headline,
                image = imageUrl
            )

            duckItUseCase
                .newPost(newPost)
                .onSuccess {
                    if (it) {
                        snackbarState.value = NewPostSnackbarState.NewPostCreated
                        stopLoading()
                        state.newPostNavRouteUi.value = NewPostNavRouteUi.GoToLogInScreenUi
                    }
                }
                .onError { error ->
                    stopLoading()
                    snackbarState.value = NewPostSnackbarState.ShowGenericError(error = error)
                }

        }
    }

    override fun resetSnackbarState() {
        snackbarState.value = NewPostSnackbarState.Idle
    }

    override fun getNewPostNavRouteUiState(): NewPostNavRouteUi {
        val state = (getState() as NewPostState.NewPostLoadedState)
        return state.newPostNavRouteUi.value
    }

    override fun resetNavRouteUiToIdle() {
        val state = (getState() as NewPostState.NewPostLoadedState)
        state.newPostNavRouteUi.value = NewPostNavRouteUi.Idle
    }

    override fun userLoggedInState() = UserLoggedInState.DontShowUserLoggedIn

    override fun isLoading(): Boolean {
        val state = (getState() as NewPostState.NewPostLoadedState)
        return state.isLoading.value
    }

    private fun stopLoading() {
        val state = (getState() as NewPostState.NewPostLoadedState)
        state.isLoading.value = false
    }

    private fun updateIsUserLoggedIn(isUserLoggedIn: Boolean) {
        val state = (getState() as NewPostState.NewPostLoadedState)
        state.isUserLoggedIn.value = isUserLoggedIn
    }

    init {
        viewModelScope.launch(coroutineContext) {
            accountUseCase.initDuckItTokenFlow(onIsUserLoggedIn = { isUserLoggedIn ->
                updateIsUserLoggedIn(isUserLoggedIn)
            })
        }
    }

    sealed class NewPostState {

        data class NewPostLoadedState(
            val headlineEditText: MutableState<String> = mutableStateOf(""),
            val imageUrlEditText: MutableState<String> = mutableStateOf(""),
            val newPostNavRouteUi: MutableState<NewPostNavRouteUi> = mutableStateOf(
                NewPostNavRouteUi.Idle
            ),
            val isUserLoggedIn: MutableState<Boolean> = mutableStateOf(false),
            val isLoading: MutableState<Boolean> = mutableStateOf(false)
        ) : NewPostState()
    }

    sealed class NewPostSnackbarState {

        object Idle : NewPostSnackbarState()

        data class ShowGenericError(
            val error: String? = null
        ) : NewPostSnackbarState()

        object NewPostCreated : NewPostSnackbarState()
    }

    sealed class NewPostNavRouteUi {

        object Idle : NewPostNavRouteUi()

        object GoToLogInScreenUi : NewPostNavRouteUi()
    }
}
