package com.cjmobileapps.duckitandroid.hilt.module

import com.cjmobileapps.duckitandroid.util.coroutine.CoroutineDispatchers
import com.cjmobileapps.duckitandroid.util.coroutine.CoroutineDispatchersImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CoroutinesModule {
    @Singleton
    @Provides
    fun coroutinesDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersImpl
    }
}
