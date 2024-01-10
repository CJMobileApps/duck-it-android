package com.cjmobileapps.duckitandroid.data.account

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.datasource.DuckItApiDataSource
import com.cjmobileapps.duckitandroid.data.datasource.DuckItLocalDataSource
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class AccountRepositoryTest : BaseTest() {

    lateinit var accountRepository: AccountRepository

    @Mock
    lateinit var mockDuckItApiDataSource: DuckItApiDataSource

    @Mock
    lateinit var mockDuckItLocalDataSource: DuckItLocalDataSource

    private fun setupAccountRepository() {
        accountRepository = AccountRepositoryImpl(
            duckItApiDataSource = mockDuckItApiDataSource,
            duckItLocalDataSource = mockDuckItLocalDataSource
        )
    }

    @Test
    fun `signIn happy flow success`() = runBlocking {

        //when
        Mockito.`when`(mockDuckItApiDataSource.signIn(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockTokenResponseSuccess)

        //then
        setupAccountRepository()
        val tokenResponse = accountRepository.signIn(MockData.mockEmailPasswordRequest)

        // verify
        Assertions.assertEquals(MockData.mockTokenResponseSuccess, tokenResponse)
    }

    @Test
    fun `signUp happy flow success`() = runBlocking {

        //when
        Mockito.`when`(mockDuckItApiDataSource.signUp(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockTokenResponseSuccess)

        //then
        setupAccountRepository()
        val tokenResponse = accountRepository.signUp(MockData.mockEmailPasswordRequest)

        // verify
        Assertions.assertEquals(MockData.mockTokenResponseSuccess, tokenResponse)
    }


//    suspend fun addDuckItToken(token: String)
//
//    suspend fun removeDuckItToken()
//
//    suspend fun duckItTokenFlow(): Flow<String>
}
