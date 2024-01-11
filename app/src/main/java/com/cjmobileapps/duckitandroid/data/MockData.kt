package com.cjmobileapps.duckitandroid.data

import com.cjmobileapps.duckitandroid.data.model.AccountState
import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest
import com.cjmobileapps.duckitandroid.data.model.Error
import com.cjmobileapps.duckitandroid.data.model.ResponseWrapper
import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.HttpURLConnection

object MockData {

    val mockEmailPasswordRequest = EmailPasswordRequest(
        email = "johnsmith@duckit.com",
        password = "password"
    )

    const val mockToken =
        "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huc21pdGhAZHVja2l0LmNvbSJ9.F0v_iQya0HIxUroq6Z-CsIqePbLvHCUoyvVHSfpO1vsXSpDdFa9OfXE6vtrVgcnOykVIB8A0yJoGZuB83mmfOQ"

    private val mockTokenResponse = TokenResponse(
        token = mockToken
    )

    val mockTokenResponseErrorHttpForbidden: Response<TokenResponse> = Response.error(
        HttpURLConnection.HTTP_FORBIDDEN,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockTokenResponseErrorHttpBadRequest: Response<TokenResponse> = Response.error(
        HttpURLConnection.HTTP_BAD_REQUEST,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockTokenResponseErrorHttpNotFound: Response<TokenResponse> = Response.error(
        HttpURLConnection.HTTP_NOT_FOUND,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockTokenResponseErrorHttpConflict: Response<TokenResponse> = Response.error(
        HttpURLConnection.HTTP_CONFLICT,
        "There was a problem".toResponseBody("text/plain;charset=UTF-8".toMediaType())
    )

    val mockTokenResponseSuccess: Response<TokenResponse> = Response.success(mockTokenResponse)


    val mockAccountStateAccountSignedInResponseWrapper = ResponseWrapper(
        data = AccountState.AccountSignedIn
    )

    val mockAccountStateAccountCreatedResponseWrapper = ResponseWrapper(
        data = AccountState.AccountCreated
    )

    val mockAccountStateErrorPasswordIncorrectResponseWrapper = ResponseWrapper<AccountState>(
        error = Error(
            isError = true,
            message = "Password Incorrect"
        )
    )

    val mockAccountStateGenericErrorResponseWrapper = ResponseWrapper<AccountState>(
        error = Error(
            isError = true,
            message = "There was a problem"
        )
    )

    val mockAccountStateAccountAlreadyExistsResponseWrapper = ResponseWrapper<AccountState>(
        error = Error(
            isError = true,
            message = "Account Already Exists"
        )
    )
}
