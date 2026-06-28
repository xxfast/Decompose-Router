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
import org.junit.Rule
import org.junit.Test

class TestSlotRouters {
  @get:Rule
  val composeRule: TestActivityRule = createAndroidComposeRule()

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testSlotNavigation(): Unit = with(composeRule) {
    // Go to slots, open the bottom sheet and verify if it is visible
    onNode(bottomNavSlotItem).performClick()
    onNode(buttonBottomSheet).performClick()
    onNode(bottomSheet).assertExists()

    // Verify all the screens of nested screens are restored
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    onNode(bottomSheet).assertExists()
    activityRule.scenario.onActivity { activity ->
      activity.onBackPressedDispatcher.onBackPressed()
    }
    onNode(bottomSheet).assertDoesNotExist()
    onNode(buttonBottomSheet).performClick()
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    onNode(bottomSheet).assertExists()
    activityRule.scenario.onActivity { activity ->
      activity.onBackPressedDispatcher.onBackPressed()
    }

    // Open the dialog and verify if it is visible
    onNode(buttonDialog).performClick()
    onNode(dialog).assertExists()
    activityRule.scenario.onActivity { activity ->
      activity.onBackPressedDispatcher.onBackPressed()
    }
    onNode(dialog).assertDoesNotExist()
    onNode(buttonDialog).performClick()
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    onNode(dialog).assertExists()
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
  }
}
