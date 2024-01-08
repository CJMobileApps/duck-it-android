package com.cjmobileapps.duckitandroid

import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import com.cjmobileapps.duckitandroid.network.NetworkConstants
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    //todo 7,703 of 7,984	3%	606 of 611	0%	668	687	1,006	1,070	355	374	177	187
    @Test
    fun addition_isCorrect() {

//        object NetworkConstants {
//            const val AUTHORIZATION_HEADER = "Authorization"
//            const val BEARER = "Bearer "
//        }
        Assertions.assertEquals(
            NetworkConstants.AUTHORIZATION_HEADER,
            "Authorization"
        )
        Assert.assertEquals(
            TokenResponse(token = "token").token,
            "token"
        )
    }

    //gradle createDevDebugCoverageReport AndroidTest
    //gradle jacocoTestReportDevDebug JUnit
}
