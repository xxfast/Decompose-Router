package io.github.xxfast.decompose.router.screens.stack.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.xxfast.decompose.router.screens.BACK_BUTTON_TAG
import io.github.xxfast.decompose.router.screens.TITLE_BAR_TAG
import io.github.xxfast.decompose.router.screens.TOOLBAR_TAG
import io.github.xxfast.decompose.router.screens.stack.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
  item: Item,
  onBack: () -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.testTag(TOOLBAR_TAG),
        title = {
          Text(
            text = item.index.toString(),
            modifier = Modifier.testTag(TITLE_BAR_TAG)
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
    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
      Text(
        text = item.toString(),
        modifier = Modifier
          .padding(16.dp)
          .align(Alignment.Center)
      )
    }
  }
}
