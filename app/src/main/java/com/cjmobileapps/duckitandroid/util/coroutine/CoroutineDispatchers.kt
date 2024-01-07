package com.cjmobileapps.duckitandroid.util.coroutine

import kotlin.coroutines.CoroutineContext

interface CoroutineDispatchers {
    val io: CoroutineContext
    val main: CoroutineContext
}
