package io.github.xxfast.decompose.router.screens.items

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.setItems
import io.github.xxfast.decompose.router.items.ItemsRouterLifecycleController
import io.github.xxfast.decompose.router.items.Router
import io.github.xxfast.decompose.router.items.items
import io.github.xxfast.decompose.router.items.rememberRouter
import io.github.xxfast.decompose.router.rememberOnRoute
import io.github.xxfast.decompose.router.screens.FAB_ADD
import io.github.xxfast.decompose.router.screens.LIST_TAG
import io.github.xxfast.decompose.router.screens.TITLE_BAR
import io.github.xxfast.decompose.router.screens.TOOLBAR
import io.github.xxfast.decompose.router.screens.items.item.ItemViewModel
import io.github.xxfast.decompose.router.screens.items.item.ItemState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalDecomposeApi::class)
@Composable
fun ItemsScreen() {
  var lastIndex: Int by remember { mutableStateOf(10) }
  val router: Router<Int> = rememberRouter { (1..lastIndex).toList() }
  val listState: LazyListState = rememberLazyListState()

  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.testTag(TOOLBAR),
        title = {
          Text(
            text = "Items",
            modifier = Modifier.testTag(TITLE_BAR)
          )
        },
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = {
          router.setItems { it + (++lastIndex) }
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
      items(
        router = router,
        key = { it }
      ) { item ->
        val itemComponent: ItemViewModel = rememberOnRoute(key = "item_$item") {
          ItemViewModel(this)
        }

        val state: ItemState by itemComponent.states.collectAsState()

        Card(
          modifier = Modifier
            .height(128.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .animateItem(),
        ) {
          Box(
            modifier = Modifier.fillMaxSize()
          ) {
            IconButton(
              onClick = {
                router.setItems { it - item }
              },
              modifier = Modifier.align(Alignment.TopEnd)
            ) {
              Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
              )
            }

            Text(
              text = "#$item — ${state.tick}",
              style = MaterialTheme.typography.displayMedium,
              modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
            )
          }
        }
      }
    }
  }

  ItemsRouterLifecycleController(
    router = router,
    lazyListState = listState,
    itemIndexConverter = { it },
    forwardPreloadCount = 3,
    backwardPreloadCount = 3
  )
}