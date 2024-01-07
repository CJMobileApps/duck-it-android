package com.cjmobileapps.duckitandroid.data.datasource

import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import com.cjmobileapps.duckitandroid.data.model.Upvotes
import com.cjmobileapps.duckitandroid.network.DuckItApi
import com.cjmobileapps.duckitandroid.util.coroutine.CoroutineDispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class DuckItApiDataSource(
    private val duckItApi: DuckItApi,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    suspend fun getPosts(): Response<Posts> {
        return withContext(coroutineDispatchers.io) {
            duckItApi
                .getPostsAsync()
                .await()
        }
    }

    suspend fun upvote(postId: String): Response<Upvotes> {
        return withContext(coroutineDispatchers.io) {
            duckItApi
                .upvoteAsync(postId)
                .await()
        }
    }

    suspend fun downvote(postId: String): Response<Upvotes> {
        return withContext(coroutineDispatchers.io) {
            duckItApi
                .downvoteAsync(postId)
                .await()
        }
    }

    suspend fun signIn(emailPasswordRequest: EmailPasswordRequest): Response<TokenResponse> {
        return withContext(coroutineDispatchers.io) {
            duckItApi
                .signInAsync(emailPasswordRequest)
                .await()
        }
    }

    suspend fun signUp(emailPasswordRequest: EmailPasswordRequest): Response<TokenResponse> {
        return withContext(coroutineDispatchers.io) {
            duckItApi
                .signUpAsync(emailPasswordRequest)
                .await()
        }
    }
}
