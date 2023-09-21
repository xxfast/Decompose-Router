package io.github.xxfast.decompose.screen.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.xxfast.decompose.router.rememberOnRoute
import io.github.xxfast.decompose.screen.FAVORITE_TAG
import io.github.xxfast.decompose.screen.LIST_TAG
import io.github.xxfast.decompose.screen.TITLE_BAR_TAG
import io.github.xxfast.decompose.screen.TOOLBAR_TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
  onSelect: (count: Int) -> Unit,
  onSelectColor: () -> Unit,
) {
  val instance: ListInstance = rememberOnRoute(ListInstance::class) { savedState ->
    ListInstance(savedState)
  }

  val state: ListState by instance.state.collectAsState()
  val items: List<Int>? = state.items

  Scaffold(
    topBar = {
      LargeTopAppBar(
        modifier = Modifier.testTag(TOOLBAR_TAG),
        title = {
          Text(
            text = "Welcome",
            modifier = Modifier.testTag(TITLE_BAR_TAG)
          )
        },
        actions = {
          IconButton(
            onClick = onSelectColor,
            modifier = Modifier
              .testTag(FAVORITE_TAG)
          ){
            Icon(
              Icons.Rounded.Favorite,
              contentDescription = null,
            )
          }
        }
      )
    }) { paddingValues ->

    if (items == Loading) Box(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxWidth()
    ) {
      CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    } else LazyVerticalGrid(
      columns = Adaptive(100.dp),
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .testTag(LIST_TAG),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(16.dp),
    ) {
      items(items) { item ->
        Card(
          modifier = Modifier
            .height(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onSelect(item) },
        ) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = item.toString(), style = MaterialTheme.typography.titleLarge)
          }
        }
      }
    }
  }
}
