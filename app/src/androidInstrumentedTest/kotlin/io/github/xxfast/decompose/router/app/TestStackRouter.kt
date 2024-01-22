package io.github.xxfast.decompose.router.app

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import io.github.xxfast.decompose.router.screens.BACK_BUTTON_TAG
import io.github.xxfast.decompose.router.screens.BOTTOM_NAV_BAR
import io.github.xxfast.decompose.router.screens.BOTTOM_NAV_PAGES
import io.github.xxfast.decompose.router.screens.BOTTOM_NAV_SLOT
import io.github.xxfast.decompose.router.screens.BOTTOM_NAV_STACK
import io.github.xxfast.decompose.router.screens.FAB_ADD
import io.github.xxfast.decompose.router.screens.LIST_TAG
import io.github.xxfast.decompose.router.screens.TITLE_BAR_TAG
import org.junit.Rule
import org.junit.Test



typealias TestActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

class TestStackRouter {
  @get:Rule
  val composeRule: TestActivityRule = createAndroidComposeRule()

  @Test
  fun testInitialState(): Unit = with(composeRule) {
    // Check the initial state
    onNode(bottomNavStackItem).assertExists()
    onNode(bottomNavStackItem).performClick()
    onNode(titleBar).assertExists().assertTextEquals("Stack (5)")
    onNode(lazyColumn).assertExists()
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testBasicNavigation(): Unit = with(composeRule) {
    // Navigate to the 4th item and verify
    var testItem = "4"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    onNode(hasText(testItem)).performClick()
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(details).assertExists().assertTextContains("Item@", substring = true)

    // Navigate back
    onNode(backButton).performClick()
    onNode(lazyColumn).assertExists()
    onNode(hasText(testItem)).assertExists()

    // Add 5 more items
    repeat(5) {
      onNode(fabAdd).performClick()
      waitUntilAtLeastOneExists(hasText(it.toString()))
    }

    // Go back to the 5th item
    testItem = "5"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    onNode(hasText(testItem)).performClick()
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(details).assertExists().assertTextContains("Item@", substring = true)

    // Navigate back and verify state and scroll position is restored
    onNode(backButton).assertExists().performClick()
    onNode(lazyColumn).assertExists()
    onNode(titleBar).assertExists().assertTextContains("Stack", substring = true)
    onNode(hasText(testItem)).assertExists()

    // Repeat the same test but this time navigate back with gestures
    testItem = "9"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    onNode(hasText(testItem)).performClick()
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(details).assertExists().assertTextContains("Item@", substring = true)
    activityRule.scenario.onActivity { activity ->
      activity.onBackPressedDispatcher.onBackPressed()
    }
    onNode(lazyColumn).assertExists()
    onNode(titleBar).assertExists().assertTextContains("Stack", substring = true)
    onNode(hasText(testItem)).assertExists()
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testRetainInstanceAcrossConfigurationChanges(): Unit = with(composeRule) {
    // Add 5 more items
    repeat(5) {
      onNode(fabAdd).performClick()
      waitUntilAtLeastOneExists(hasText(it.toString()))
    }

    // Go to and click 5th item
    var testItem = "5"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    onNode(hasText(testItem)).performClick()

    // Trigger configuration change and verify if the state and scroll position is restored back on the list screen
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(hasText(testItem)).assertExists()

    // Trigger configuration change again and verify scroll position is restored
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    onNode(hasText(testItem)).assertExists()

    // Repeat the same test but this time navigate back with gestures
    activityRule.scenario.onActivity { activity ->
      activity.onBackPressedDispatcher.onBackPressed()
    }
    testItem = "9"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    onNode(hasText(testItem)).performClick()
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(details).assertExists().assertTextContains("Item@", substring = true)
    activityRule.scenario.onActivity { activity ->
      activity.onBackPressedDispatcher.onBackPressed()
    }
    onNode(lazyColumn).assertExists()
    onNode(titleBar).assertExists().assertTextContains("Stack", substring = true)
    onNode(hasText(testItem)).assertExists()
  }
}
