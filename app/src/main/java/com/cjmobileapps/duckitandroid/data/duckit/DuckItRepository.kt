package com.cjmobileapps.duckitandroid.data.duckit

import com.cjmobileapps.duckitandroid.data.model.NewPostRequest
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.cjmobileapps.duckitandroid.data.model.Upvotes
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DuckItRepository {

    suspend fun getPosts(): Response<Posts>

    suspend fun upvote(postId: String): Response<Upvotes>

    suspend fun downvote(postId: String): Response<Upvotes>

    suspend fun newPost(newPost: NewPostRequest, authorizationToken: String): Response<Unit>

    suspend fun getDuckItPostsFlow(): Flow<Posts>

    suspend fun addDuckItPostsToDB(posts: Posts)
}
