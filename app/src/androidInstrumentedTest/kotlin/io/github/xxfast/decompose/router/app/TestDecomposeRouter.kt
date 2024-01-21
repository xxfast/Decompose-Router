package io.github.xxfast.decompose.router.app

import android.content.pm.ActivityInfo
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import io.github.xxfast.decompose.router.screens.BACK_BUTTON_TAG
import io.github.xxfast.decompose.router.screens.DETAILS_TAG
import io.github.xxfast.decompose.router.screens.FAVORITE_TAG
import io.github.xxfast.decompose.router.screens.LIST_TAG
import io.github.xxfast.decompose.router.screens.TITLE_BAR_TAG
import org.junit.Rule
import org.junit.Test

private val backButton = hasTestTag(BACK_BUTTON_TAG)
private val titleBar = hasTestTag(TITLE_BAR_TAG)
private val details = hasTestTag(DETAILS_TAG)
private val circularProgressIndicator = hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
private val lazyColumn = hasTestTag(LIST_TAG)
private val favouriteButton = hasTestTag(FAVORITE_TAG)

typealias TestActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

class TestDecomposeRouterWithActivity {
  @get:Rule
  val composeRule: TestActivityRule = createAndroidComposeRule()

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testBasicNavigation(): Unit = with(composeRule) {
    // Check the initial state
    onNode(circularProgressIndicator).assertExists()
    onNode(titleBar).assertExists().assertTextEquals("Welcome")

    // Wait till the screen is populated
    waitUntilAtLeastOneExists(lazyColumn)

    // Go to the 4th item
    var testItem = "4"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    // Click on the 10th item
    onNode(hasText(testItem)).performClick()

    // Verify if detail is shown is correct
    onNode(titleBar).assertExists().assertTextEquals(testItem)

    // Do the little game
    onNode(details).onChildren().fetchSemanticsNodes().forEachIndexed { index, _ ->
      onNode(details).onChildAt(index).performClick()
    }

    // Verify if auto-navigation works
    onNode(titleBar).assertExists().assertTextEquals("Welcome")

    // Verify if state and scroll position is restored
    onNode(circularProgressIndicator).assertDoesNotExist()
    onNode(hasText(testItem)).assertExists()

    // Go to the 100th item
    testItem = "100"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    onNode(hasText(testItem)).performClick()
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(circularProgressIndicator).assertDoesNotExist()
    onNode(hasText(testItem)).assertExists()

    // Test go back
    onNode(backButton).assertExists().performClick()
    onNode(titleBar).assertExists().assertTextEquals("Welcome")
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testRetainInstanceAcrossConfigurationChanges(): Unit = with(composeRule) {
    // Wait till the screen is populated
    waitUntilAtLeastOneExists(lazyColumn)

    // Trigger configuration change
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    // Test if the loaded data is not lost
    onNode(lazyColumn).assertExists()
    onNode(circularProgressIndicator).assertDoesNotExist()

    // Go to the 50th item
    val testItem = "50"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    // Click on the 10th item
    onNode(hasText(testItem)).performClick()

    // Verify if detail is shown is correct
    onNode(titleBar).assertExists().assertTextEquals(testItem)

    // Trigger configuration change again
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    // Navigate back
    onNode(backButton).assertExists().performClick()
    onNode(titleBar).assertExists().assertTextEquals("Welcome")

    // Verify if state and scroll position is restored
    onNode(circularProgressIndicator).assertDoesNotExist()
    onNode(hasText(testItem)).assertExists()
  }

  @Test
  fun testNestedNavigation(): Unit = with(composeRule) {
    onNode(favouriteButton).performClick()
    onNode(titleBar).assertExists().assertTextEquals("Colors")
    onNode(hasText("Primary")).performClick()
    onNode(titleBar).assertExists().assertTextEquals("Primary")
    onNode(backButton).performClick()
    onNode(hasText("Secondary")).performClick()
    onNode(titleBar).assertExists().assertTextEquals("Secondary")
    onNode(hasText("10")).performClick()
    onNode(titleBar).assertExists().assertTextEquals("10")
    onNode(hasText("5")).performClick()
    onNode(backButton).performClick()
    onNode(titleBar).assertExists().assertTextEquals("Secondary")
  }
}
