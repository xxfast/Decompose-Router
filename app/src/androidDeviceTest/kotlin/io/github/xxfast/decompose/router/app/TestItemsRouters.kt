package io.github.xxfast.decompose.router.app

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.waitUntilDoesNotExist
import androidx.compose.ui.test.waitUntilExactlyOneExists
import org.junit.Rule
import org.junit.Test

class TestItemsRouters {
  @get:Rule
  val composeRule: TestActivityRule = createAndroidComposeRule()

  // Each item renders as "#<index> - <tick>", where <tick> increments every second.
  // Matching on the "#<index> - " prefix keeps assertions stable against the ticking
  // counter and avoids "#1" also matching "#10"/"#11".
  private fun itemAt(index: Int) = hasText("#$index - ", substring = true)

  @Test
  fun testInitialState(): Unit = with(composeRule) {
    // Go to the items screen
    onNode(bottomNavItemsItem).assertExists()
    onNode(bottomNavItemsItem).performClick()
    onNode(titleBar).assertExists().assertTextEquals("Items")
    onNode(lazyColumn).assertExists()

    // The initial 10 items are present
    onNode(itemAt(1)).assertExists()
    onNode(lazyColumn).performScrollToNode(itemAt(10))
    onNode(itemAt(10)).assertExists()
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testAddAndRemoveItems(): Unit = with(composeRule) {
    onNode(bottomNavItemsItem).performClick()

    // Add a new item via the FAB and verify it shows up
    onNode(fabAdd).performClick()
    onNode(lazyColumn).performScrollToNode(itemAt(11))
    waitUntilExactlyOneExists(itemAt(11))

    // Remove an item and verify it's gone. Scroll to the top first so the removed
    // item's button isn't under the floating action button, and wait past the item
    // removal animation (each row runs a ticking coroutine, so the clock never idles).
    onNode(lazyColumn).performScrollToNode(itemAt(1))
    onNode(removeItem(1)).performClick()
    waitUntilDoesNotExist(itemAt(1), timeoutMillis = 5_000)

    // Removing an item leaves the rest of the list intact
    onNode(itemAt(2)).assertExists()
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testRestoredAcrossConfigurationChanges(): Unit = with(composeRule) {
    onNode(bottomNavItemsItem).performClick()
    onNode(lazyColumn).performScrollToNode(itemAt(3))
    onNode(itemAt(3)).assertExists()

    // Rotate to landscape and verify the items list is retained. The activity is
    // recreated asynchronously, so wait for the rebuilt list before asserting.
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    waitUntilExactlyOneExists(lazyColumn, timeoutMillis = 5_000)
    onNode(lazyColumn).performScrollToNode(itemAt(3))
    waitUntilExactlyOneExists(itemAt(3), timeoutMillis = 5_000)

    // Rotate back and verify it's still there
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    waitUntilExactlyOneExists(lazyColumn, timeoutMillis = 5_000)
    onNode(lazyColumn).performScrollToNode(itemAt(3))
    waitUntilExactlyOneExists(itemAt(3), timeoutMillis = 5_000)
  }
}
