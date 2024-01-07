package com.cjmobileapps.duckitandroid.network

import com.cjmobileapps.duckitandroid.data.datasource.DuckItLocalDataSource
import com.cjmobileapps.duckitandroid.network.NetworkConstants.AUTHORIZATION_HEADER
import com.cjmobileapps.duckitandroid.network.NetworkConstants.BEARER
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val duckItLocalDataSource: DuckItLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val headers = Headers.Builder()

        runBlocking {
            val authorizationToken = duckItLocalDataSource.duckItTokenFlow.firstOrNull()

            if (!authorizationToken.isNullOrEmpty()) {
                headers.add(AUTHORIZATION_HEADER, BEARER + authorizationToken)
            }
        }

        val request = chain.request()
            .newBuilder()
            .headers(headers.build())
            .build()
        return try {
            chain.proceed(request)
        } catch (e: Exception) {
            chain.proceed(request)
        }
    }
}
