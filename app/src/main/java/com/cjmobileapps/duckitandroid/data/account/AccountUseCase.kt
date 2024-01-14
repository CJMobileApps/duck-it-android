package com.cjmobileapps.duckitandroid.data.account

import com.cjmobileapps.duckitandroid.data.StringConstants
import com.cjmobileapps.duckitandroid.data.model.AccountState
import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest
import com.cjmobileapps.duckitandroid.data.model.Error
import com.cjmobileapps.duckitandroid.data.model.ResponseWrapper
import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import com.cjmobileapps.duckitandroid.util.onError
import com.cjmobileapps.duckitandroid.util.onSuccess
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.net.HttpURLConnection

class AccountUseCase(
    private val accountRepository: AccountRepository
) {

    private val tag = AccountUseCase::class.java.simpleName

    var isUserLoggedIn = false

    var authorizationToken = ""

    suspend fun initDuckItTokenFlow(onIsUserLoggedIn: (isUserLoggedIn: Boolean) -> Unit) {
        accountRepository.duckItTokenFlow().collectLatest { token ->
            if (token.isNotEmpty()) {
                this.authorizationToken = token
                isUserLoggedIn = true
                onIsUserLoggedIn.invoke(true)
            } else {
                isUserLoggedIn = false
                onIsUserLoggedIn.invoke(false)
            }
        }
    }

    suspend fun signIn(emailPasswordRequest: EmailPasswordRequest): ResponseWrapper<AccountState>? {
        var responseWrapper: ResponseWrapper<AccountState>? = null

        accountRepository.signIn(emailPasswordRequest)
            .onSuccess { token ->
                responseWrapper =
                    addDuckItToken(token = token, accountState = AccountState.AccountSignedIn)
            }
            .onError { error, code ->
                responseWrapper = when (code) {
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        ResponseWrapper(
                            error = Error(
                                isError = true,
                                message = StringConstants.passwordIncorrect
                            )
                        )
                    }

                    HttpURLConnection.HTTP_NOT_FOUND -> {
                        Timber.d(tag, StringConstants.accountNotFoundTryToCreateAccount)
                        signUp(emailPasswordRequest)
                    }

                    else -> {
                        ResponseWrapper(error = Error(isError = true, message = error))
                    }
                }
            }

        return responseWrapper
    }

    suspend fun signOut() {
        removeDuckItToken()
    }

    private suspend fun signUp(emailPasswordRequest: EmailPasswordRequest): ResponseWrapper<AccountState>? {
        var responseWrapper: ResponseWrapper<AccountState>? = null

        accountRepository.signUp(emailPasswordRequest)
            .onSuccess { token ->
                responseWrapper =
                    addDuckItToken(token = token, accountState = AccountState.AccountCreated)
            }
            .onError { error, code ->
                responseWrapper = when (code) {
                    HttpURLConnection.HTTP_CONFLICT -> {
                        ResponseWrapper(
                            error = Error(
                                isError = true,
                                message = StringConstants.accountAlreadyExists
                            )
                        )
                    }

                    else -> {
                        ResponseWrapper(error = Error(isError = true, message = error))
                    }
                }
            }

        return responseWrapper
    }

    private suspend fun removeDuckItToken() {
        accountRepository.removeDuckItToken()
        this.authorizationToken = ""
        isUserLoggedIn = false
    }

    private suspend fun addDuckItToken(
        token: TokenResponse,
        accountState: AccountState
    ): ResponseWrapper<AccountState> {
        accountRepository.addDuckItToken(token.token)
        this.authorizationToken = token.token
        isUserLoggedIn = true
        return ResponseWrapper(data = accountState)
    }
}
