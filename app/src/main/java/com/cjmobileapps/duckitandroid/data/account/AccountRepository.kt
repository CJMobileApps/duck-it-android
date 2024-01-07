package com.cjmobileapps.duckitandroid.data.account

import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest
import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AccountRepository {

    suspend fun signIn(emailPasswordRequest: EmailPasswordRequest): Response<TokenResponse>

    suspend fun signUp(emailPasswordRequest: EmailPasswordRequest): Response<TokenResponse>

    suspend fun addDuckItToken(token: String)

    suspend fun removeDuckItToken()

    suspend fun duckItTokenFlow(): Flow<String>
}
