package com.cjmobileapps.duckitandroid.data

import com.cjmobileapps.duckitandroid.data.model.EmailPasswordRequest
import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import retrofit2.Response

object MockData {

    val mockEmailPasswordRequest = EmailPasswordRequest(
        email = "johnsmith@duckit.com",
        password = "password"
    )

    const val mockToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huc21pdGhAZHVja2l0LmNvbSJ9.F0v_iQya0HIxUroq6Z-CsIqePbLvHCUoyvVHSfpO1vsXSpDdFa9OfXE6vtrVgcnOykVIB8A0yJoGZuB83mmfOQ"

    private val mockTokenResponse = TokenResponse(
        token = mockToken
    )

    val mockTokenResponseSuccess: Response<TokenResponse> = Response.success(mockTokenResponse)
}
