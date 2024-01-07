package com.cjmobileapps.duckitandroid.data.model

data class ResponseWrapper<T>(
    val data: T? = null,
    val error: Error? = null,
)

data class Error(
    val isError: Boolean = false,
    val message: String? = null
)

inline fun <T : Any> ResponseWrapper<T>.onSuccess(action: (T) -> Unit): ResponseWrapper<T> {
    this.data?.let { data ->
        action(data)
    }

    return this
}

inline fun <T : Any> ResponseWrapper<T>.onError(action: (String?) -> Unit): ResponseWrapper<T> {
    this.error?.let { error ->
        action(error.message)
    }

    return this
}
