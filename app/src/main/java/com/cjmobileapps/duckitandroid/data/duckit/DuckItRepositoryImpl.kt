package com.cjmobileapps.duckitandroid.data.duckit

import com.cjmobileapps.duckitandroid.data.datasource.DuckItApiDataSource
import com.cjmobileapps.duckitandroid.data.datasource.DuckItLocalDataSource
import com.cjmobileapps.duckitandroid.data.model.NewPostRequest
import com.cjmobileapps.duckitandroid.data.model.Posts

class DuckItRepositoryImpl(
    private val duckItApiDataSource: DuckItApiDataSource,
    private val duckItLocalDataSource: DuckItLocalDataSource
) : DuckItRepository {

    override suspend fun getPosts() = duckItApiDataSource.getPosts()

    override suspend fun upvote(postId: String) = duckItApiDataSource.upvote(postId)

    override suspend fun downvote(postId: String) = duckItApiDataSource.downvote(postId)

    override suspend fun newPost(newPost: NewPostRequest, authorizationToken: String) =
        duckItApiDataSource.newPost(newPost, authorizationToken)

    override suspend fun getDuckItPostsFlow() = duckItLocalDataSource.getDuckItPostsFlow()

    override suspend fun addDuckItPostsToDB(posts: Posts) =
        duckItLocalDataSource.createDuckItPosts(posts)
}
