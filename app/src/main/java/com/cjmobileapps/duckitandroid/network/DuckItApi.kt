package com.cjmobileapps.duckitandroid.network

import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest
import com.cjmobileapps.duckitandroid.data.model.NewPostRequest
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import com.cjmobileapps.duckitandroid.data.model.Upvotes
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface DuckItApi {

    @GET("posts")
    fun getPostsAsync(): Deferred<Response<Posts>>

    @POST("posts/{postId}/upvote")
    fun upvoteAsync(
        @Path("postId") postId: String
    ): Deferred<Response<Upvotes>>

    @POST("posts/{postId}/downvote")
    fun downvoteAsync(
        @Path("postId") postId: String
    ): Deferred<Response<Upvotes>>

    @POST("signin")
    fun signInAsync(
        @Body emailPasswordRequest: EmailPasswordRequest
    ): Deferred<Response<TokenResponse>>

    @POST("signup")
    fun signUpAsync(
        @Body emailPasswordRequest: EmailPasswordRequest
    ): Deferred<Response<TokenResponse>>

    @POST("posts")
    fun newPostAsync(
        @Header(NetworkConstants.AUTHORIZATION_HEADER) authorizationToken: String,
        @Body newPost: NewPostRequest
    ): Deferred<Response<Unit>>
}
