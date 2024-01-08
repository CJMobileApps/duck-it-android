package com.cjmobileapps.duckitandroid

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cjmobileapps.duckitandroid.data.model.TokenResponse
import com.cjmobileapps.duckitandroid.network.NetworkConstants

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        println("HERE_ " + appContext.packageName)
        assertEquals("com.cjmobileapps.duckitandroid.debug", appContext.packageName)
        assertEquals(
            NetworkConstants.AUTHORIZATION_HEADER,
            "Authorization"
        );
        assertEquals(
            NetworkConstants.BEARER,
            "Bearer "
        );

        assertEquals(
            TokenResponse(token = "token").token,
            "token"
        )

    }
}