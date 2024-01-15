package com.cjmobileapps.duckitandroid.hilt.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
import com.cjmobileapps.duckitandroid.room.DatabaseFactory
import com.cjmobileapps.duckitandroid.room.DuckItDao
import com.cjmobileapps.duckitandroid.room.DuckItDatabase
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
    fun duckDuckItDataStorePreferences(
        duckItDataStore: DuckItDataStore
    ): DataStore<Preferences> {
        return duckItDataStore.dataStore
    }

    @Singleton
    @Provides
    fun duckItLocalDataSource(
        duckDuckItDataStorePreferences: DataStore<Preferences>,
        duckItDao: DuckItDao,
        coroutineDispatchers: CoroutineDispatchers
    ): DuckItLocalDataSource {
        return DuckItLocalDataSource(
            duckDuckItDataStorePreferences = duckDuckItDataStorePreferences,
            duckItDao = duckItDao,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    @Singleton
    @Provides
    fun duckItRepository(
        duckItApiDataSource: DuckItApiDataSource,
        duckItLocalDataSource: DuckItLocalDataSource
    ): DuckItRepository {
        return DuckItRepositoryImpl(
            duckItApiDataSource = duckItApiDataSource,
            duckItLocalDataSource = duckItLocalDataSource
        )
    }

    @Singleton
    @Provides
    fun duckItUseCase(
        duckItRepository: DuckItRepository,
        accountUseCase: AccountUseCase
    ): DuckItUseCase {
        return DuckItUseCase(
            duckItRepository = duckItRepository,
            accountUseCase = accountUseCase
        )
    }

    @Singleton
    @Provides
    fun accountRepository(
        duckItApiDataSource: DuckItApiDataSource,
        duckItLocalDataSource: DuckItLocalDataSource,

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

    @Singleton
    @Provides
    fun duckItDatabase(
        @ApplicationContext context: Context
    ): DuckItDatabase {
        return DatabaseFactory.getDuckItDatabase(context)
    }

    @Singleton
    @Provides
    fun duckItDao(
        duckItDatabase: DuckItDatabase
    ): DuckItDao {
        return duckItDatabase.duckItDao()
    }
}
