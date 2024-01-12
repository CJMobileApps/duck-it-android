package com.cjmobileapps.duckitandroid.util.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

object CoroutineDispatchersImpl : CoroutineDispatchers {
    override val io = Dispatchers.IO
    override val main = Dispatchers.Main
    override val ioExecutor = Dispatchers.IO.asExecutor()
}
