package com.cjmobileapps.duckitandroid.data.duckit

class DuckItUseCase(
    private val duckItRepository: DuckItRepository
) {
    suspend fun getPosts() = duckItRepository.getPosts()

    suspend fun upvote(postId: String) = duckItRepository.upvote(postId)

    suspend fun downvote(postId: String) = duckItRepository.downvote(postId)
}
