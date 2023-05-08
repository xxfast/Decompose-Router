package io.github.xxfast.decompose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.xxfast.decompose.screen.BACK_BUTTON_TAG
import io.github.xxfast.decompose.screen.DETAILS_TAG
import io.github.xxfast.decompose.screen.HomeScreen
import io.github.xxfast.decompose.screen.LAZY_COLUMN_TAG
import io.github.xxfast.decompose.screen.TITLEBAR_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val backButton = hasTestTag(BACK_BUTTON_TAG)
private val titleBar = hasTestTag(TITLEBAR_TAG)
private val details = hasTestTag(DETAILS_TAG)
private val circularProgressIndicator = hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
private val lazyColumn = hasTestTag(LAZY_COLUMN_TAG)

class TestDecomposeRouterWithoutActivity {
  @get:Rule
  val composeRule: ComposeContentTestRule = createComposeRule()

  @Before
  fun setup() {
    val rootComponentContext = DefaultComponentContext(LifecycleRegistry())
    composeRule.setContent {
      CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
        MaterialTheme {
          HomeScreen()
        }
      }
    }
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testBasicNavigation(): Unit = with(composeRule) {
    // Check the initial state
    onNode(circularProgressIndicator).assertExists()
    onNode(titleBar).assertExists().assertTextEquals("Home")

    // Wait till the screen is populated
    waitUntilAtLeastOneExists(lazyColumn)

    // Go to the 50th item
    var testItem = "50"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    // Click on the 10th item
    onNode(hasText(testItem)).performClick()

    // Verify if detail is shown is correct
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(details).assertExists().assertTextEquals("Details: $testItem")

    // Navigate back
    onNode(backButton).assertExists().performClick()
    onNode(titleBar).assertExists().assertTextEquals("Home")

    // Verify if state and scroll position is restored
    onNode(circularProgressIndicator).assertDoesNotExist()
    onNode(hasText(testItem)).assertExists()

    // Go to the 100th item and navigate back
    testItem = "100"
    onNode(lazyColumn).performScrollToNode(hasText(testItem))
    onNode(hasText(testItem)).performClick()
    onNode(titleBar).assertExists().assertTextEquals(testItem)
    onNode(details).assertExists().assertTextEquals("Details: $testItem")
    onNode(circularProgressIndicator).assertDoesNotExist()
    onNode(hasText(testItem)).assertExists()
  }
}
