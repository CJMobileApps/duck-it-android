package com.cjmobileapps.duckitandroid.data.model

import com.cjmobileapps.duckitandroid.data.MockData
import com.cjmobileapps.duckitandroid.testutil.BaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ResponseWrapperTest : BaseTest() {

    @Test
    fun `call ResponseWrapper onSuccess`() {
        MockData.mockAccountStateAccountSignedInResponseWrapper
            .onSuccess { accountState ->
                Assertions.assertEquals(
                    AccountState.AccountSignedIn,
                    accountState
                )
            }
    }

    @Test
    fun `call ResponseWrapper onError`() {
        MockData.mockAccountStateGenericErrorResponseWrapper
            .onError { error ->
                Assertions.assertEquals(
                    "There was a problem",
                    error
                )
            }
    }
}
