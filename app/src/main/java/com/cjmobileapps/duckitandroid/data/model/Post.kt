package com.cjmobileapps.duckitandroid.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.annotations.SerializedName

data class Posts(
    @SerializedName("Posts") val posts: List<Post>
)
data class Post(
    val id: String?,
    val headline: String?,
    val image: String?,
    var upvotes: Int?,
    val author: String?
)

data class PostState(
    val id: String?,
    val headline: String?,
    val image: String?,
    var upvotes: MutableState<Int?>,
    val author: String?
)

fun List<Post>.convertToStateObj(): List<PostState> {
    return this.map { it.convertToStateObj() }
}
fun Post.convertToStateObj(): PostState {
    return PostState(
        id = this.id,
        headline = this.headline,
        image = this.image,
        upvotes = mutableStateOf(this.upvotes),
        author = this.author
    )
}
