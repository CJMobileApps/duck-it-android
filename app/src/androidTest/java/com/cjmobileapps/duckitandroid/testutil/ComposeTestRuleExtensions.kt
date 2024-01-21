package com.cjmobileapps.duckitandroid.testutil

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * Uses a [ComposeTestRule] created via [createEmptyComposeRule] that allows setup before the activity
 * is launched via [onBefore]. Assertions on the view can be made in [onAfterLaunched].
 */
inline fun <reified A : Activity> ComposeTestRule.launch(
    onBefore: () -> Unit = {},
    intentFactory: (Context) -> Intent = {
        Intent(
            ApplicationProvider.getApplicationContext(),
            A::class.java
        )
    },
    onAfterLaunched: ComposeTestRule.() -> Unit
) {
    onBefore()

    val context = ApplicationProvider.getApplicationContext<Context>()
    ActivityScenario.launch<A>(intentFactory(context))

    onAfterLaunched()
}

fun ComposeTestRule.waitUntilTimeout(
    timeoutMillis: Long
) {
    AsyncTimer.start(timeoutMillis)
    this.waitUntil(
        condition = { AsyncTimer.expired },
        timeoutMillis = timeoutMillis + 1000
    )
}

private object AsyncTimer {
    var expired = false
    fun start(delay: Long = 1000) {
        expired = false
        Timer().schedule(delay) {
            expired = true
        }
    }
}
