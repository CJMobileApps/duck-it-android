package com.cjmobileapps.duckitandroid.data.account

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.data.datasource.DuckItApiDataSource
import com.cjmobileapps.duckitandroid.data.datasource.DuckItLocalDataSource
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class AccountRepositoryTest : BaseTest() {

    private lateinit var accountRepository: AccountRepository

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

    @Test
    fun `addDuckItToken happy flow success`() = runBlocking {

        //when
        Mockito.`when`(mockDuckItLocalDataSource.addDuckItToken(MockData.mockToken))
            .thenReturn(Unit)

        //then
        setupAccountRepository()
        accountRepository.addDuckItToken(MockData.mockToken)

        // verify
        Mockito.verify(mockDuckItLocalDataSource, Mockito.times(1))
            .addDuckItToken(MockData.mockToken)
    }

    @Test
    fun `removeDuckItToken happy flow success`() = runBlocking {

        //when
        Mockito.`when`(mockDuckItLocalDataSource.removeDuckItToken()).thenReturn(Unit)

        //then
        setupAccountRepository()
        accountRepository.removeDuckItToken()

        // verify
        Mockito.verify(mockDuckItLocalDataSource, Mockito.times(1)).removeDuckItToken()
    }

    @Test
    fun `duckItTokenFlow return token happy flow success`() = runBlocking {

        // given
        val mockLocationCoordinateFlow: Flow<String> = flow {
            emit(MockData.mockToken)
        }

        // when
        Mockito.`when`(mockDuckItLocalDataSource.duckItTokenFlow).thenReturn(mockLocationCoordinateFlow)


        // then
        setupAccountRepository()
        val duckItTokenFlow = accountRepository.duckItTokenFlow()

        // verify
        Assertions.assertEquals(
            MockData.mockToken,
            duckItTokenFlow.first()
        )
    }
}
