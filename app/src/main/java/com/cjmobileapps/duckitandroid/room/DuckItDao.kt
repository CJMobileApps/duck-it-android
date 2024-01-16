package com.cjmobileapps.duckitandroid.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cjmobileapps.duckitandroid.data.model.Posts
import kotlinx.coroutines.flow.Flow

@Dao
interface DuckItDao {

    @Query("SELECT * from posts")
    fun getPosts(): Flow<Posts>

    @Insert
    suspend fun insertPosts(posts: Posts)

    @Query("DELETE FROM posts")
    suspend fun deletePosts()
}
