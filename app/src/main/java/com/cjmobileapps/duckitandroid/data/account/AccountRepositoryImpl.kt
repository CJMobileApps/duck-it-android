package com.cjmobileapps.duckitandroid.data.account

import com.cjmobileapps.duckitandroid.data.datasource.DuckItApiDataSource
import com.cjmobileapps.duckitandroid.data.datasource.DuckItLocalDataSource
import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest

class AccountRepositoryImpl(
    private val duckItApiDataSource: DuckItApiDataSource,
    private val duckItLocalDataSource: DuckItLocalDataSource
): AccountRepository {

    override suspend fun signIn(emailPasswordRequest: EmailPasswordRequest) = duckItApiDataSource.signIn(emailPasswordRequest)

    override suspend fun signUp(emailPasswordRequest: EmailPasswordRequest) = duckItApiDataSource.signUp(emailPasswordRequest)

    override suspend fun duckItTokenFlow() = duckItLocalDataSource.duckItTokenFlow

    override suspend fun addDuckItToken(token: String) = duckItLocalDataSource.addDuckItToken(token)

    override suspend fun removeDuckItToken() = duckItLocalDataSource.removeDuckItToken()
}
