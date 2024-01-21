package io.github.xxfast.decompose.router.screens.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FirstPage
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.selectFirst
import io.github.xxfast.decompose.router.pages.RoutedContent
import io.github.xxfast.decompose.router.pages.rememberRouter

@OptIn(
  ExperimentalDecomposeApi::class,
  ExperimentalFoundationApi::class,
  ExperimentalMaterial3Api::class
)
@Composable
fun PagesScreen() {
  val router = rememberRouter(PagesScreens::class) {
    Pages(
      items = List(10) { PagesScreens(it) },
      selectedIndex = 0,
    )
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Pages (${router.pages.value.items.size})") },
        actions = {
          IconButton(onClick = { router.selectFirst() }) {
            Icon(Icons.Rounded.FirstPage, null)
          }
        }
      )
    },
    contentWindowInsets = WindowInsets(0, 0, 0, 0)
  ) { scaffoldPadding ->
    RoutedContent(router = router, modifier = Modifier.padding(scaffoldPadding)) { page ->
      Card(modifier = Modifier.padding(16.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
          Text(
            text = page.index.toString(),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.align(Alignment.Center)
          )
        }
      }
    }
  }
}
