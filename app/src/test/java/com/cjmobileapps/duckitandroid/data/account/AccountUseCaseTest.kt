package com.cjmobileapps.duckitandroid.data.account

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class AccountUseCaseTest : BaseTest() {

    private lateinit var accountUseCase: AccountUseCase

    @Mock
    lateinit var mockAccountRepository: AccountRepository

    private fun setupAccountUseCase() {
        accountUseCase = AccountUseCase(
            accountRepository = mockAccountRepository
        )
    }

    @Test
    fun `user not signed in then signIn then get token happy success flow`() = runTest {

        // given empty token flow
        val mockDuckItTokenFlowEmpty: Flow<String> = flow {
            emit("")
        }

        // when token flow called
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowEmpty)

        // then setup AccountUserCase
        setupAccountUseCase()

        // verify authorization token is empty
        Assertions.assertEquals(
            "",
            accountUseCase.authorizationToken
        )

        // given token flow
        val mockDuckItTokenFlowValue: Flow<String> = flow {
            emit(MockData.mockToken)
        }

        // when
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowValue)
        Mockito.`when`(mockAccountRepository.signIn(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockTokenResponseSuccess)

        // then sign in
        val signInResponse = accountUseCase.signIn(MockData.mockEmailPasswordRequest)

        // verify signInResponse
        Assertions.assertEquals(
            MockData.mockAccountStateAccountSignedInResponseWrapper,
            signInResponse
        )
    }

    @Test
    fun `user not signed in then try to signIn then return HTTP forbidden`() = runTest {

        // given empty token flow
        val mockDuckItTokenFlowEmpty: Flow<String> = flow {
            emit("")
        }

        // when token flow called
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowEmpty)

        // then setup AccountUserCase
        setupAccountUseCase()

        // verify authorization token is empty
        Assertions.assertEquals(
            "",
            accountUseCase.authorizationToken
        )

        // when
        Mockito.`when`(mockAccountRepository.signIn(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockTokenResponseErrorHttpForbidden)


        // then sign in
        val signInResponse = accountUseCase.signIn(MockData.mockEmailPasswordRequest)


        // verify signInResponse error
        Assertions.assertEquals(
            MockData.mockAccountStateErrorPasswordIncorrectResponseWrapper,
            signInResponse
        )
    }

    @Test
    fun `user not signed in then try to signIn then return generic response error`() = runTest {

        // given empty token flow
        val mockDuckItTokenFlowEmpty: Flow<String> = flow {
            emit("")
        }

        // when token flow called
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowEmpty)

        // then setup AccountUserCase
        setupAccountUseCase()

        // verify authorization token is empty
        Assertions.assertEquals(
            "",
            accountUseCase.authorizationToken
        )


        // when
        Mockito.`when`(mockAccountRepository.signIn(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockTokenResponseErrorHttpBadRequest)


        // then sign in
        val signInResponse = accountUseCase.signIn(MockData.mockEmailPasswordRequest)


        // verify signInResponse error
        Assertions.assertEquals(
            MockData.mockAccountStateGenericErrorResponseWrapper,
            signInResponse
        )
    }

    @Test
    fun `user signIn account not found then create account success flow`() = runTest {

        // given empty token flow
        val mockDuckItTokenFlowEmpty: Flow<String> = flow {
            emit("")
        }

        // when token flow called
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowEmpty)

        // then setup AccountUserCase
        setupAccountUseCase()

        // verify authorization token is empty
        Assertions.assertEquals(
            "",
            accountUseCase.authorizationToken
        )

        // when
        Mockito.`when`(mockAccountRepository.signIn(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockTokenResponseErrorHttpNotFound)
        Mockito.`when`(mockAccountRepository.signUp(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockTokenResponseSuccess)

        // then sign in
        val signInResponse = accountUseCase.signIn(MockData.mockEmailPasswordRequest)

        // verify signInResponse
        Assertions.assertEquals(
            MockData.mockAccountStateAccountCreatedResponseWrapper,
            signInResponse
        )
    }

    @Test
    fun `user signIn account not found then create account account already exists error`() =
        runTest {

            // given empty token flow
            val mockDuckItTokenFlowEmpty: Flow<String> = flow {
                emit("")
            }

            // when token flow called
            Mockito.`when`(mockAccountRepository.duckItTokenFlow())
                .thenReturn(mockDuckItTokenFlowEmpty)

            // then setup AccountUserCase
            setupAccountUseCase()

            // verify authorization token is empty
            Assertions.assertEquals(
                "",
                accountUseCase.authorizationToken
            )

            // when
            Mockito.`when`(mockAccountRepository.signIn(MockData.mockEmailPasswordRequest))
                .thenReturn(MockData.mockTokenResponseErrorHttpNotFound)
            Mockito.`when`(mockAccountRepository.signUp(MockData.mockEmailPasswordRequest))
                .thenReturn(MockData.mockTokenResponseErrorHttpConflict)

            // then sign in
            val signInResponse = accountUseCase.signIn(MockData.mockEmailPasswordRequest)


            // verify signInResponse
            Assertions.assertEquals(
                MockData.mockAccountStateAccountAlreadyExistsResponseWrapper,
                signInResponse
            )
        }

    @Test
    fun `user signIn account not found then create account some error exists`() =
        runTest {

            // given empty token flow
            val mockDuckItTokenFlowEmpty: Flow<String> = flow {
                emit("")
            }

            // when token flow called
            Mockito.`when`(mockAccountRepository.duckItTokenFlow())
                .thenReturn(mockDuckItTokenFlowEmpty)

            // then setup AccountUserCase
            setupAccountUseCase()

            // verify authorization token is empty
            Assertions.assertEquals(
                "",
                accountUseCase.authorizationToken
            )

            // when
            Mockito.`when`(mockAccountRepository.signIn(MockData.mockEmailPasswordRequest))
                .thenReturn(MockData.mockTokenResponseErrorHttpNotFound)
            Mockito.`when`(mockAccountRepository.signUp(MockData.mockEmailPasswordRequest))
                .thenReturn(MockData.mockTokenResponseErrorHttpBadRequest)

            // then sign in
            val signInResponse = accountUseCase.signIn(MockData.mockEmailPasswordRequest)

            // verify signInResponse
            Assertions.assertEquals(
                MockData.mockAccountStateGenericErrorNoMessageResponseWrapper,
                signInResponse
            )
        }

    @Test
    fun `duckItTokenFlow empty then signIn then signOut`() = runTest {

        // given empty token flow
        val mockDuckItTokenFlowEmpty: Flow<String> = flow {
            emit("")
        }

        // when token flow called
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowEmpty)

        // then setup AccountUserCase
        setupAccountUseCase()
        accountUseCase.initDuckItTokenFlow { isUserLoggedIn ->
            Assertions.assertEquals(
                false,
                isUserLoggedIn
            )
        }

        // verify authorization token is empty and user not signed in
        Assertions.assertEquals(
            "",
            accountUseCase.authorizationToken
        )
        Assertions.assertFalse(accountUseCase.isUserLoggedIn)
        Mockito.verify(mockAccountRepository, Mockito.times(1))
            .duckItTokenFlow()

        // given token flow
        val mockDuckItTokenFlowValue: Flow<String> = flow {
            emit(MockData.mockToken)
        }

        // when
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowValue)
        Mockito.`when`(mockAccountRepository.signIn(MockData.mockEmailPasswordRequest))
            .thenReturn(MockData.mockTokenResponseSuccess)

        // then sign in
        accountUseCase.initDuckItTokenFlow { isUserLoggedIn ->
            Assertions.assertEquals(
                true,
                isUserLoggedIn
            )
        }
        val signInResponse = accountUseCase.signIn(MockData.mockEmailPasswordRequest)

        // verify signInResponse
        Assertions.assertEquals(
            MockData.mockAccountStateAccountSignedInResponseWrapper,
            signInResponse
        )

        // verify authorization token is not null
        Assertions.assertEquals(
            MockData.mockToken,
            accountUseCase.authorizationToken
        )
        Assertions.assertTrue(accountUseCase.isUserLoggedIn)
        Mockito.verify(mockAccountRepository, Mockito.times(2))
            .duckItTokenFlow()

        // when
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowEmpty)

        // then sign out
        accountUseCase.initDuckItTokenFlow { isUserLoggedIn ->
            Assertions.assertEquals(
                isUserLoggedIn,
                false
            )
        }
        accountUseCase.signOut()
        Assertions.assertEquals(
            accountUseCase.authorizationToken,
            ""
        )
        Mockito.verify(mockAccountRepository, Mockito.times(3))
            .duckItTokenFlow()
        Assertions.assertFalse(accountUseCase.isUserLoggedIn)
    }

    @Test
    fun `duckItTokenFlow has token already signed in then signOut`() = runTest {

        // given empty token flow
        val mockDuckItTokenFlowValue1: Flow<String> = flow {
            emit(MockData.mockToken)
        }

        // when token flow called
        Mockito.`when`(mockAccountRepository.duckItTokenFlow())
            .thenReturn(mockDuckItTokenFlowValue1)

        // then setup AccountUserCase
        setupAccountUseCase()
        accountUseCase.initDuckItTokenFlow { isUserLoggedIn ->
            Assertions.assertEquals(
                true,
                isUserLoggedIn
            )
        }

        // verify authorization token is empty and user not signed in
        Assertions.assertEquals(
            MockData.mockToken,
            accountUseCase.authorizationToken
        )
        Assertions.assertTrue(accountUseCase.isUserLoggedIn)
        Mockito.verify(mockAccountRepository, Mockito.times(1))
            .duckItTokenFlow()

        // given empty token
        val mockDuckItTokenFlowEmpty: Flow<String> = flow {
            emit("")
        }

        // when
        Mockito.`when`(mockAccountRepository.duckItTokenFlow()).thenReturn(mockDuckItTokenFlowEmpty)

        // then sign out
        accountUseCase.initDuckItTokenFlow { isUserLoggedIn ->
            Assertions.assertEquals(
                isUserLoggedIn,
                false
            )
        }
        accountUseCase.signOut()
        Assertions.assertEquals(
            accountUseCase.authorizationToken,
            ""
        )
        Mockito.verify(mockAccountRepository, Mockito.times(2))
            .duckItTokenFlow()
        Assertions.assertFalse(accountUseCase.isUserLoggedIn)
    }
}
