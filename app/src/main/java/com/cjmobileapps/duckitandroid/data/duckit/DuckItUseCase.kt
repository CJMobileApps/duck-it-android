package com.cjmobileapps.duckitandroid.data.duckit

import com.cjmobileapps.duckitandroid.data.StringConstants
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.model.Error
import com.cjmobileapps.duckitandroid.data.model.NewPostRequest
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.data.model.ResponseWrapper
import com.cjmobileapps.duckitandroid.network.NetworkConstants
import com.cjmobileapps.duckitandroid.util.onError
import com.cjmobileapps.duckitandroid.util.onSuccess
import kotlinx.coroutines.flow.collectLatest

class DuckItUseCase(
    private val duckItRepository: DuckItRepository,
    private val accountUseCase: AccountUseCase
) {

    suspend fun getPosts(onPostsResponse: (ResponseWrapper<Posts>) -> Unit) {
        try {
            duckItRepository.getDuckItPostsFlow().collectLatest { posts ->
                onPostsResponse(ResponseWrapper(data = posts))
            }
        } catch (e: Exception) {
            onPostsResponse(ResponseWrapper(error = Error(isError = true, message = e.message)))
        }
    }

    suspend fun fetchPosts(): ResponseWrapper<Boolean> {
        var responseWrapper: ResponseWrapper<Boolean> = ResponseWrapper()

        duckItRepository.getPosts()
            .onSuccess { posts ->
                responseWrapper = try {
                    duckItRepository.addDuckItPostsToDB(posts)
                    ResponseWrapper(data = true)
                } catch (e: Exception) {
                    ResponseWrapper(error = Error(isError = true, message = e.message))

                }
            }
            .onError { error, _ ->
                responseWrapper = ResponseWrapper(error = Error(isError = true, message = error))
            }

        return responseWrapper
    }


    suspend fun upvote(postId: String) = duckItRepository.upvote(postId)

    suspend fun downvote(postId: String) = duckItRepository.downvote(postId)

    suspend fun newPost(newPost: NewPostRequest): ResponseWrapper<Boolean> {
        var responseWrapper: ResponseWrapper<Boolean> = ResponseWrapper()

        if (!accountUseCase.isUserLoggedIn || accountUseCase.authorizationToken.isEmpty()) {
            return ResponseWrapper(
                error = Error(
                    isError = true,
                    message = StringConstants.accountNotLoggedIn
                )
            )
        }

        val authorizationToken = NetworkConstants.BEARER + accountUseCase.authorizationToken

        duckItRepository.newPost(newPost = newPost, authorizationToken = authorizationToken)
            .onSuccess {
                responseWrapper = ResponseWrapper(data = true)
            }
            .onError { error, _ ->
                responseWrapper = ResponseWrapper(error = Error(isError = true, message = error))
            }

        return responseWrapper
    }
}
