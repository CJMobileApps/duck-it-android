package com.cjmobileapps.duckitandroid.hilt.module

import android.content.Context
import com.cjmobileapps.duckitandroid.data.account.AccountRepository
import com.cjmobileapps.duckitandroid.data.account.AccountRepositoryImpl
import com.cjmobileapps.duckitandroid.data.account.AccountUseCase
import com.cjmobileapps.duckitandroid.data.duckit.DuckItRepository
import com.cjmobileapps.duckitandroid.data.duckit.DuckItRepositoryImpl
import com.cjmobileapps.duckitandroid.data.duckit.DuckItUseCase
import com.cjmobileapps.duckitandroid.data.datasource.DuckItApiDataSource
import com.cjmobileapps.duckitandroid.data.datasource.DuckItLocalDataSource
import com.cjmobileapps.duckitandroid.data.datastore.DuckItDataStore
import com.cjmobileapps.duckitandroid.network.DuckItApi
import com.cjmobileapps.duckitandroid.util.coroutine.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun duckItApiDataSource(
        duckItApi: DuckItApi,
        coroutineDispatchers: CoroutineDispatchers
    ): DuckItApiDataSource {
        return DuckItApiDataSource(
            duckItApi = duckItApi,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    @Singleton
    @Provides
    fun duckItLocalDataSource(
        duckItDataStore: DuckItDataStore
    ): DuckItLocalDataSource {
        return DuckItLocalDataSource(
            duckItDataStore = duckItDataStore
        )
    }

    @Singleton
    @Provides
    fun duckItRepository(
        duckItApiDataSource: DuckItApiDataSource
    ): DuckItRepository {
        return DuckItRepositoryImpl(
            duckItApiDataSource = duckItApiDataSource
        )
    }

    @Singleton
    @Provides
    fun duckItUseCase(
        duckItRepository: DuckItRepository
    ): DuckItUseCase {
        return DuckItUseCase(
            duckItRepository = duckItRepository
        )
    }

    @Singleton
    @Provides
    fun accountRepository(
        duckItApiDataSource: DuckItApiDataSource,
        duckItLocalDataSource: DuckItLocalDataSource
    ): AccountRepository {
        return AccountRepositoryImpl(
            duckItApiDataSource = duckItApiDataSource,
            duckItLocalDataSource = duckItLocalDataSource
        )
    }

    @Singleton
    @Provides
    fun accountUseCase(
        accountRepository: AccountRepository
    ): AccountUseCase {
        return AccountUseCase(
            accountRepository = accountRepository
        )
    }

    @Singleton
    @Provides
    fun duckItDataStore(
        @ApplicationContext context: Context
    ): DuckItDataStore {
        return DuckItDataStore(context)
    }
}
