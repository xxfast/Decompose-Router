package io.github.xxfast.decompose.router.app

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.waitUntilExactlyOneExists
import org.junit.Rule
import org.junit.Test

class TestPagesRouters {
  @get:Rule
  val composeRule: TestActivityRule = createAndroidComposeRule()

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testPagesNavigation(): Unit = with(composeRule) {
    // Go to pages and swipe to the 5th page
    onNode(bottomNavPagesItem).performClick()
    onNode(pager).performScrollToIndex(5)
    onNode(hasText("Page #5")).assertExists()

    // Verify pages screens are restored
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    waitUntilExactlyOneExists(pager, timeoutMillis = 5_000)
    waitUntilExactlyOneExists(hasText("Page #5"), timeoutMillis = 5_000)
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    waitUntilExactlyOneExists(pager, timeoutMillis = 5_000)
    waitUntilExactlyOneExists(hasText("Page #5"), timeoutMillis = 5_000)
  }
}
