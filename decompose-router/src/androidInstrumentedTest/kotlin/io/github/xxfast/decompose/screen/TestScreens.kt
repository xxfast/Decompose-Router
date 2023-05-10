package io.github.xxfast.decompose.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberOnRoute
import io.github.xxfast.decompose.router.rememberRouter
import io.github.xxfast.decompose.screen.Screen.Round

const val TOOLBAR_TAG = "toolbar"
const val BACK_BUTTON_TAG = "back"
const val TITLEBAR_TAG = "titleBar"
const val DETAILS_TAG = "details"
const val LAZY_COLUMN_TAG = "lazyColumn"

@Composable
fun HomeScreen() {
  val router: Router<Screen> = rememberRouter(Screen::class, listOf(Screen.Game))
  RoutedContent(
    router = router,
    animation = stackAnimation(slide())
  ) { screen ->
    when (screen) {
      Screen.Game -> ListScreen(
        onSelect = { count -> router.push(Round(count)) },
      )

      is Round -> DetailScreen(
        count = screen.number,
        onBack = { router.pop() }
      )
    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
  onSelect: (count: Int) -> Unit,
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
      CircularProgressIndicator(modifier = Modifier.align(Center))
    } else LazyVerticalGrid(
      columns = GridCells.Adaptive(100.dp),
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .testTag(LAZY_COLUMN_TAG),
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
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
            Text(text = item.toString(), style = MaterialTheme.typography.titleLarge)
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
  count: Int,
  onBack: () -> Unit,
) {
  var counted: Set<Int> by rememberSaveable { mutableStateOf(emptySet()) }

  if (counted.size == count) onBack()

  Scaffold(
    topBar = {
      LargeTopAppBar(
        modifier = Modifier.testTag(TOOLBAR_TAG),
        title = {
          Text(
            text = count.toString(),
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
    LazyHorizontalGrid(
      rows = GridCells.Adaptive(100.dp),
      modifier = Modifier
        .padding(paddingValues)
        .testTag(DETAILS_TAG),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(16.dp),
    ) {
      items(count) { i ->
        val cornerSize: Dp by animateDpAsState(if (i in counted) 64.dp else 8.dp)

        Card(
          modifier = Modifier
            .width(100.dp)
            .clickable { counted = if (i in counted) counted - i else counted + i }
            .clip(RoundedCornerShape(cornerSize)),
          colors = CardDefaults.cardColors(
            containerColor =
            if (i in counted) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.primaryContainer
          ),
          shape = RoundedCornerShape(cornerSize)
        ) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
            if (i in counted) Icon(imageVector = Icons.Default.Check, null)
            else Text(text = i.toString(), style = MaterialTheme.typography.titleLarge)
          }
        }
      }
    }
  }
}


