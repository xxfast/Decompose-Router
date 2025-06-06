package io.github.xxfast.decompose.router.screens.stack.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.xxfast.decompose.router.rememberOnRoute
import io.github.xxfast.decompose.router.screens.FAB_ADD
import io.github.xxfast.decompose.router.screens.LIST_TAG
import io.github.xxfast.decompose.router.screens.TITLE_BAR
import io.github.xxfast.decompose.router.screens.TOOLBAR
import io.github.xxfast.decompose.router.screens.stack.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
  onSelect: (screen: Item) -> Unit,
) {
  val listComponent: ListViewModel = rememberOnRoute {
    ListViewModel(this)
  }

  val state: ListState by listComponent.states.collectAsState()

  ListView(
    state = state,
    onSelect = onSelect,
    onAdd = { listComponent.add() }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListView(
  state: ListState,
  onSelect: (screen: Item) -> Unit,
  onAdd: () -> Unit,
) {

  val listState: LazyListState = rememberLazyListState()
  val coroutineScope: CoroutineScope = rememberCoroutineScope()

  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.testTag(TOOLBAR),
        title = {
          Text(
            text = "Stack (${state.screens.size})",
            modifier = Modifier.testTag(TITLE_BAR)
          )
        },
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          onAdd()
          coroutineScope.launch { listState.animateScrollToItem(state.screens.lastIndex) }
        },
        content = { Icon(Icons.Rounded.Add, null) },
        modifier = Modifier.testTag(FAB_ADD)
      )
    },
    contentWindowInsets = WindowInsets(0, 0, 0, 0)
  ) { paddingValues ->
    LazyColumn(
      state = listState,
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .testTag(LIST_TAG),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(16.dp),
    ) {
      items(state.screens) { screen ->
        Card(
          modifier = Modifier
            .height(128.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onSelect(screen) },
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
          ) {
            Text(
              text = screen.index.toString(),
              style = MaterialTheme.typography.displayMedium,
              modifier = Modifier
                .padding(8.dp)
            )
          }
        }
      }
    }
  }
}
