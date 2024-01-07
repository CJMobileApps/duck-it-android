package com.cjmobileapps.duckitandroid.hilt.module

import com.cjmobileapps.duckitandroid.BuildConfig
import com.cjmobileapps.duckitandroid.data.datasource.DuckItLocalDataSource
import com.cjmobileapps.duckitandroid.network.DuckItApi
import com.cjmobileapps.duckitandroid.network.HeaderInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun headerInterceptor(duckItLocalDataSource: DuckItLocalDataSource): HeaderInterceptor {
        return HeaderInterceptor(duckItLocalDataSource)
    }

    @Singleton
    @Provides
    fun okHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
//            .addInterceptor(headerInterceptor) todo this was giving 403 with the token
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun duckItApi(retrofit: Retrofit): DuckItApi {
        return retrofit.create(DuckItApi::class.java)
    }
}
