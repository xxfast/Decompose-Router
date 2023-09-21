package io.github.xxfast.decompose.screen.nested

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import io.github.xxfast.decompose.screen.BACK_BUTTON_TAG
import io.github.xxfast.decompose.screen.LIST_TAG
import io.github.xxfast.decompose.screen.TITLE_BAR_TAG
import io.github.xxfast.decompose.screen.TOOLBAR_TAG
import io.github.xxfast.decompose.screen.nested.NestedScreens.Home
import io.github.xxfast.decompose.screen.nested.NestedScreens.Primary
import io.github.xxfast.decompose.screen.nested.NestedScreens.Secondary
import io.github.xxfast.decompose.screen.nested.NestedScreens.Tertiary

@Composable
fun NestedScreen(
  onBack: () -> Unit,
  onSelect: (Int) -> Unit
) {
  val router: Router<NestedScreens> = rememberRouter(NestedScreens::class) { listOf(Home) }

  val items: List<Int> = buildList { repeat(50) { add(it) } }

  RoutedContent(
    router = router,
    animation = stackAnimation(slide(orientation = Vertical))
  ) { screen ->

    val (onPrimary, primaryContainer  ) = MaterialTheme.colorScheme.onPrimaryContainer to MaterialTheme.colorScheme.primaryContainer
    val (onSecondary, secondaryContainer  ) = MaterialTheme.colorScheme.onSecondaryContainer to MaterialTheme.colorScheme.secondaryContainer
    val (onTertiary, tertiaryContainer  ) = MaterialTheme.colorScheme.onTertiaryContainer to MaterialTheme.colorScheme.tertiaryContainer

    when (screen) {
      Home -> NestedHomeView(
        onPrimary = { router.push(Primary)},
        onSecondary = { router.push(Secondary)},
        onTertiary = { router.push(Tertiary) },
        onBack = onBack
      )

      Primary -> NestedView(
        title = "Primary",
        items = items,
        contentColor = onPrimary,
        containerColor = primaryContainer,
        onBack = { router.pop() },
        onSelect = onSelect
      )

      Secondary -> NestedView(
        title = "Secondary",
        items = items,
        contentColor = onSecondary,
        containerColor = secondaryContainer,
        onBack = { router.pop() },
        onSelect = onSelect
      )
      Tertiary -> NestedView(
        title = "Tertiary",
        items = items,
        contentColor = onTertiary,
        containerColor = tertiaryContainer,
        onBack = { router.pop() },
        onSelect = onSelect
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedHomeView(
  onPrimary: () -> Unit,
  onSecondary: () -> Unit,
  onTertiary: () -> Unit,
  onBack: () -> Unit,
) {
  Scaffold(
    topBar = {
      LargeTopAppBar(
        modifier = Modifier.testTag(TOOLBAR_TAG),
        title = {
          Text(
            text = "Colors",
            modifier = Modifier.testTag(TITLE_BAR_TAG)
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag(BACK_BUTTON_TAG)) {
            Icon(Icons.Rounded.ArrowBack, null) }
        }
      )
    }
  ) { paddingValues ->
    Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
      Button(
        onClick = onPrimary,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth().fillMaxHeight(.33f).padding(16.dp)
      ) {
        Text("Primary", style = MaterialTheme.typography.titleLarge)
      }
      Button(
        onClick = onSecondary,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.fillMaxWidth().fillMaxHeight(.5f).padding(16.dp)
      ) {
        Text("Secondary", style = MaterialTheme.typography.titleLarge)
      }
      Button(
        onClick = onTertiary,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp)
      ) {
        Text("Tertiary", style = MaterialTheme.typography.titleLarge)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedView(
  title: String,
  items: List<Int>,
  contentColor: Color,
  containerColor: Color,
  onBack: () -> Unit,
  onSelect: (Int) -> Unit,
) {
  MaterialTheme(
    colorScheme = MaterialTheme.colorScheme.copy(
      onSurface = contentColor,
      surface = containerColor,
    )
  ) {
    Scaffold(
      topBar = {
        LargeTopAppBar(
          modifier = Modifier.testTag(TOOLBAR_TAG),
          title = {
            Text(
              text = title,
              modifier = Modifier.testTag(TITLE_BAR_TAG)
            )
          },
          navigationIcon = {
            IconButton(onClick = onBack, modifier = Modifier.testTag(BACK_BUTTON_TAG)) {
              Icon(Icons.Rounded.KeyboardArrowUp, null) }
          }
        )
      }
    ) { paddingValues ->
      LazyVerticalGrid(
        columns = Adaptive(100.dp),
        modifier = androidx.compose.ui.Modifier
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
}
