package com.cjmobileapps.duckitandroid.data.duckit

import com.cjmobileapps.duckitandroid.data.datasource.DuckItApiDataSource
import com.cjmobileapps.duckitandroid.data.model.NewPostRequest

class DuckItRepositoryImpl(
    private val duckItApiDataSource: DuckItApiDataSource
) : DuckItRepository {

    override suspend fun getPosts() = duckItApiDataSource.getPosts()

    override suspend fun upvote(postId: String) = duckItApiDataSource.upvote(postId)

    override suspend fun downvote(postId: String) = duckItApiDataSource.downvote(postId)

    override suspend fun newPost(newPost: NewPostRequest, authorizationToken: String) =
        duckItApiDataSource.newPost(newPost, authorizationToken)
}
