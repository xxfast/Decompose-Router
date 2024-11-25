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

class TestNestedRouters {
  @get:Rule
  val composeRule: TestActivityRule = createAndroidComposeRule()

  @Test
  fun testInitialState(): Unit = with(composeRule) {
    // Check the initial state
    onNode(bottomNavStackItem).assertExists()
    onNode(bottomNavPagesItem).assertExists()
    onNode(bottomNavSlotItem).assertExists()
  }

  @Test
  fun testNestedNavigation(): Unit = with(composeRule) {
    // Add 5 more items on to stack
    repeat(5) {
      onNode(fabAdd).performClick()
    }

    // Go to 5th detail screen
    var testItem = "5"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    onNode(hasText(testItem)).performClick()
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(details).assertExists().assertTextContains("Item@", substring = true)

    // Go to pages and swipe to the 5th page
    onNode(bottomNavPagesItem).performClick()
    onNode(pager).performScrollToIndex(5)
    onNode(hasText("Page #5")).assertExists()

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
    onNode(bottomNavStackItem).performClick()
    onNode(details).assertExists().assertTextContains("Item@", substring = true)
    onNode(bottomNavPagesItem).performClick()
    onNode(hasText("Page #5")).assertExists()
    onNode(bottomNavSlotItem).performClick()
    onNode(buttonBottomSheet).performClick()
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    onNode(bottomSheet).assertExists()
  }
}
