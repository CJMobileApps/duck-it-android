package com.cjmobileapps.duckitandroid.ui.list.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.duckit.DuckItUseCase
import com.cjmobileapps.duckitandroid.data.model.PostState
import com.cjmobileapps.duckitandroid.data.model.compose.UserLoggedInState
import com.cjmobileapps.duckitandroid.data.model.convertToStateObj
import com.cjmobileapps.duckitandroid.util.coroutine.CoroutineDispatchers
import com.cjmobileapps.duckitandroid.util.onError
import com.cjmobileapps.duckitandroid.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DuckItListViewModelImpl @Inject constructor(
    private val duckItUseCase: DuckItUseCase,
    private val accountUseCase: AccountUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel(), DuckItListViewModel {

    private val compositeJob = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag(tag)
            .e("coroutineExceptionHandler() error occurred: $throwable \n ${throwable.message}")
        snackbarState.value = DuckItSnackbarState.ShowGenericError()
    }

    private val coroutineContext =
        compositeJob + coroutineDispatchers.main + exceptionHandler + SupervisorJob()

    private val duckItListState = mutableStateOf<DuckItListState>(DuckItListState.LoadingState)

    private val snackbarState = mutableStateOf<DuckItSnackbarState>(DuckItSnackbarState.Idle)

    private val tag = DuckItListViewModelImpl::class.java.simpleName

    override fun getState() = duckItListState.value

    override fun getSnackbarState() = snackbarState.value

    init {
        viewModelScope.launch(coroutineContext) {

            duckItUseCase.getPosts()
                .onSuccess { posts ->
                    duckItListState.value = DuckItListState.DuckItListLoadedState(
                        posts = posts.posts.convertToStateObj()
                    )

                    viewModelScope.launch(coroutineContext) {
                        accountUseCase.initDuckItTokenFlow(onIsUserLoggedIn = { isUserLoggedIn ->
                            updateIsUserLoggedIn(isUserLoggedIn)
                        })
                    }
                }
                .onError { _, _ ->
                    snackbarState.value = DuckItSnackbarState.UnableToGetDuckItListError()
                }
        }
    }

    override fun upvote(postId: String) {
        val state = getState()
        if (state !is DuckItListState.DuckItListLoadedState) return

        if (!accountUseCase.isUserLoggedIn) {
            snackbarState.value = DuckItSnackbarState.UserNotLoggedInError()
            return
        }

        viewModelScope.launch(coroutineContext) {
            duckItUseCase.upvote(postId)
                .onSuccess { upvotes ->
                    state.posts
                        .filter { it.id == postId }
                        .map { it.upvotes.value = upvotes.upvotes }
                }
                .onError { _, _ ->
                    snackbarState.value = DuckItSnackbarState.UpvoteError()
                }
        }
    }

    override fun downvote(postId: String) {
        val state = getState()
        if (state !is DuckItListState.DuckItListLoadedState) return

        if (!accountUseCase.isUserLoggedIn) {
            snackbarState.value = DuckItSnackbarState.UserNotLoggedInError()
            return
        }

        viewModelScope.launch(coroutineContext) {
            duckItUseCase.downvote(postId)
                .onSuccess { upvotes ->
                    state.posts
                        .filter { it.id == postId }
                        .map { it.upvotes.value = upvotes.upvotes }
                }
                .onError { _, _ ->
                    snackbarState.value = DuckItSnackbarState.DownvoteError()
                }
        }
    }

    override fun resetSnackbarState() {
        snackbarState.value = DuckItSnackbarState.Idle
    }

    override fun userLoggedInState(): UserLoggedInState {
        val state = getState()
        if (state !is DuckItListState.DuckItListLoadedState) return UserLoggedInState.UserLoggedOut
        return if (state.isUserLoggedIn.value) {
            UserLoggedInState.UserLoggedIn
        } else {
            UserLoggedInState.UserLoggedOut
        }
    }

    private fun updateIsUserLoggedIn(isUserLoggedIn: Boolean) {
        val state = getState()
        if (state !is DuckItListState.DuckItListLoadedState) return
        state.isUserLoggedIn.value = isUserLoggedIn
    }

    override fun isUserLoggedInButtonClicked() {
        viewModelScope.launch(coroutineContext) {
            if (accountUseCase.isUserLoggedIn) {
                accountUseCase.signOut()
                snackbarState.value = DuckItSnackbarState.SignedOut
            } else {
                val state = getState()
                if (state !is DuckItListState.DuckItListLoadedState) return@launch

                state.duckItListNavRouteUi.value = DuckItListNavRouteUi.GoToLogInScreenUi
            }
        }
    }

    override fun resetNavRouteUiToIdle() {
        val state = getState()
        if (state !is DuckItListState.DuckItListLoadedState) return
        state.duckItListNavRouteUi.value = DuckItListNavRouteUi.Idle
    }

    override fun getDuckItListNavRouteUiState(): DuckItListNavRouteUi {
        val state = getState()
        if (state !is DuckItListState.DuckItListLoadedState) return DuckItListNavRouteUi.Idle

        return state.duckItListNavRouteUi.value
    }

    sealed class DuckItListState {

        object LoadingState : DuckItListState()

        data class DuckItListLoadedState(
            val posts: List<PostState>,
            val duckItListNavRouteUi: MutableState<DuckItListNavRouteUi> = mutableStateOf(
                DuckItListNavRouteUi.Idle
            ),
            val isUserLoggedIn: MutableState<Boolean> = mutableStateOf(false)
        ) : DuckItListState()
    }

    sealed class DuckItSnackbarState {

        object Idle : DuckItSnackbarState()

        object SignedOut : DuckItSnackbarState()

        data class ShowGenericError(
            val error: String? = null
        ) : DuckItSnackbarState()

        data class DownvoteError(
            val error: String? = null
        ) : DuckItSnackbarState()

        data class UpvoteError(
            val error: String? = null
        ) : DuckItSnackbarState()

        data class UnableToGetDuckItListError(
            val error: String? = null
        ) : DuckItSnackbarState()

        data class UserNotLoggedInError(
            val error: String? = null
        ) : DuckItSnackbarState()
    }

    sealed class DuckItListNavRouteUi {

        object Idle : DuckItListNavRouteUi()

        object GoToLogInScreenUi : DuckItListNavRouteUi()
    }
}
