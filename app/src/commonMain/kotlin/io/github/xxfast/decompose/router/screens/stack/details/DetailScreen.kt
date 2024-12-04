package io.github.xxfast.decompose.router.screens.stack.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.lifecycle.doOnDestroy
import io.github.xxfast.decompose.router.rememberOnRoute
import io.github.xxfast.decompose.router.screens.BACK_BUTTON_TAG
import io.github.xxfast.decompose.router.screens.DETAILS_TAG
import io.github.xxfast.decompose.router.screens.TITLE_BAR_TAG
import io.github.xxfast.decompose.router.screens.TOOLBAR_TAG
import io.github.xxfast.decompose.router.screens.stack.Item
import kotlinx.coroutines.cancel

@Composable
fun DetailScreen(
  item: Item,
  onBack: () -> Unit,
  onNext: () -> Unit,
) {
  val viewModel: DetailsViewModel = rememberOnRoute {
    DetailsViewModel(this, item)
      .apply { doOnDestroy { cancel() } }
  }

  val state: DetailState by viewModel.states.collectAsState()

  DetailView(
    state = state,
    onBack = onBack,
    onNext = onNext,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView(
  state: DetailState,
  onBack: () -> Unit,
  onNext: () -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.testTag(TOOLBAR_TAG),
        title = {
          Text(
            text = "#${state.item.index}",
            modifier = Modifier.testTag(TITLE_BAR_TAG)
          )
        },
        navigationIcon = {
          IconButton(
            modifier = Modifier
              .testTag(BACK_BUTTON_TAG),
            onClick = onBack
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
          }
        },
        actions = {
          IconButton(
            modifier = Modifier,
            onClick = onNext
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
          }
        })
    }) { paddingValues ->
    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
      Text(
        text = "${state.item} been in the stack for ${state.age}",
        modifier = Modifier
          .padding(16.dp)
          .align(Alignment.Center)
          .testTag(DETAILS_TAG)
      )
    }
  }
}
