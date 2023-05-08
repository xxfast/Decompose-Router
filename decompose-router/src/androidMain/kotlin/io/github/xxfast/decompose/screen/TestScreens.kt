package io.github.xxfast.decompose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import io.github.xxfast.decompose.router.rememberViewModel
import io.github.xxfast.decompose.screen.Screen.Detail

const val TOOLBAR_TAG = "toolbar"
const val BACK_BUTTON_TAG = "back"
const val TITLEBAR_TAG = "titleBar"
const val DETAILS_TAG = "details"
const val LAZY_COLUMN_TAG = "lazyColumn"

@Composable
fun HomeScreen() {
  val router: Router<Screen> = rememberRouter(Screen::class, listOf(Screen.List))
  RoutedContent(router = router) { screen ->
    when (screen) {
      Screen.List -> ListScreen(
        onSelect = { detail -> router.push(Detail(detail)) },
      )

      is Detail -> DetailsScreen(
        detail = screen.detail,
        onBack = { router.pop() }
      )
    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
  onSelect: (detail: String) -> Unit,
) {
  val instance: ListInstance =
    rememberViewModel(ListInstance::class) { savedState -> ListInstance(savedState) }
  val state: ListState by instance.state.collectAsState()
  val items: List<String>? = state.items

  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.testTag(TOOLBAR_TAG),
        title = {
          Text(
            text = "Home",
            modifier = Modifier.testTag(TITLEBAR_TAG)
          )
        }
      )
    }) { paddingValues ->

    if (items == Loading) Box(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxWidth()
    ) {
      CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    } else LazyColumn(
      modifier = Modifier.fillMaxSize().testTag(LAZY_COLUMN_TAG)
    ) {
      items(items) { item ->
        ListItem(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(item) },
          headlineText = { Text(text = item) }
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
  detail: String,
  onBack: () -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.testTag(TOOLBAR_TAG),
        title = {
          Text(
            text = detail,
            modifier = Modifier.testTag(TITLEBAR_TAG)
          )
        },
        navigationIcon = {
          IconButton(
            modifier = Modifier
              .testTag(BACK_BUTTON_TAG),
            onClick = onBack
          ) {
            Icon(Icons.Default.ArrowBack, null)
          }
        })
    }) { paddingValues ->
    Box(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxWidth()
    ) {
      Text(
        text = "Details: $detail",
        modifier = Modifier.testTag(DETAILS_TAG)
      )
    }
  }
}


