package com.cjmobileapps.duckitandroid.room

import androidx.room.TypeConverter
import com.cjmobileapps.duckitandroid.data.model.Post
import com.cjmobileapps.duckitandroid.data.model.Posts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toPosts(value: String?): Posts? {
        if(value.isNullOrEmpty() || value == "null") return null
        val type = object : TypeToken<Posts>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromPosts(value: Posts): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toPostList(value: String?): List<Post> {
        if(value.isNullOrEmpty() || value == "null") return emptyList()
        val type = object : TypeToken<List<Post>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromPostList(value: List<Post>): String {
        return Gson().toJson(value)
    }
}
