package io.github.xxfast.decompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.core.view.WindowCompat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.defaultComponentContext
import io.github.xxfast.decompose.screen.BACK_BUTTON_TAG
import io.github.xxfast.decompose.screen.DETAILS_TAG
import io.github.xxfast.decompose.screen.HomeScreen
import io.github.xxfast.decompose.screen.LAZY_COLUMN_TAG
import io.github.xxfast.decompose.screen.TITLEBAR_TAG
import org.junit.Rule
import org.junit.Test

private val backButton = hasTestTag(BACK_BUTTON_TAG)
private val titleBar = hasTestTag(TITLEBAR_TAG)
private val details = hasTestTag(DETAILS_TAG)
private val circularProgressIndicator = hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
private val lazyColumn = hasTestTag(LAZY_COLUMN_TAG)

typealias TestActivityRule = AndroidComposeTestRule<ActivityScenarioRule<TestActivity>, TestActivity>

class TestActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val rootComponentContext: DefaultComponentContext = defaultComponentContext()

    setContent {
      CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
        MaterialTheme {
          HomeScreen()
        }
      }
    }
  }
}

class TestDecomposeRouterWithActivity {
  @get:Rule
  val composeRule: TestActivityRule = createAndroidComposeRule()

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
