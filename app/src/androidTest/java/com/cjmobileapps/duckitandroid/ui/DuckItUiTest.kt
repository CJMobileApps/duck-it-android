package com.cjmobileapps.duckitandroid.ui

import android.content.Intent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import com.cjmobileapps.duckitandroid.testutil.launch
import com.cjmobileapps.duckitandroid.testutil.waitUntilTimeout
import org.junit.Rule
import org.junit.Test

class DuckItUiTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun loginThenLogoutHappyFlow() = composeRule.launch<DuckItActivity>(
        intentFactory = {
            Intent(it, DuckItActivity::class.java)
        },
        onAfterLaunched = {
            loginIfLoggedOut()

            // list screen
            waitUntilTimeout(1500L)
            onNodeWithTag("DuckItListUi")
                .performScrollToNode(hasText("Super Duck"))
            onNodeWithText("Super Duck").performScrollTo()
            onNodeWithContentDescription("Super Duck Upvote").performScrollTo()
            onNodeWithContentDescription("Super Duck Upvote").performClick()
            onNodeWithContentDescription("Super Duck Downvote").performClick()

            onNodeWithContentDescription("More Vert").performClick()
            onNodeWithText("Sign Out").performClick()
        }
    )

    private fun ComposeTestRule.loginIfLoggedOut() {

        // list screen
        onNodeWithContentDescription("More Vert").performClick()

        // login screen
        waitUntilTimeout(1500L)
        if (onNodeWithText("Log In").isDisplayed()) {
            onNodeWithText("Log In").performClick()
            onNodeWithText("Email").performTextInput("johnsmith@duckit.com")
            onNodeWithText("Password").performTextInput("password")
            onNodeWithText("Log In").performClick()
        } else {
            Espresso.pressBack()
        }
    }
}
