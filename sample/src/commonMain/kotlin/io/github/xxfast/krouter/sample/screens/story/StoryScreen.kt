package io.github.xxfast.krouter.sample.screens.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.xxfast.krouter.rememberViewModel
import io.github.xxfast.krouter.sample.models.StoryId
import io.github.xxfast.krouter.sample.screens.topStories.Loading
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import io.ktor.http.Url

@Composable
fun StoryScreen(
  id: StoryId,
  title: String,
  onBack: () -> Unit,
) {
  val viewModel: StoryViewModel =
    rememberViewModel { savedState -> StoryViewModel(savedState, id, title) }

  val state: StoryState by viewModel.states.collectAsState()

  StoryView(
    state = state,
    onRefresh = viewModel::onRefresh,
    onBack = onBack,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryView(
  state: StoryState,
  onRefresh: () -> Unit,
  onBack: () -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = state.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        navigationIcon = {
          IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, contentDescription = null) }
        },
        actions = {
          IconButton(onClick = onRefresh) { Icon(Icons.Rounded.Refresh, contentDescription = null) }
        }
      )
    },
  ) { scaffoldPadding ->
    Box(
      modifier = Modifier
        .padding(scaffoldPadding)
        .fillMaxSize()
    ) {
      if (state.details == Loading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
      else Column {
        KamelImage(
          resource = lazyPainterResource(Url(state.details.imageUrl)),
          contentDescription = null,
          onLoading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
          contentScale = ContentScale.FillWidth,
          modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))
            .fillMaxWidth()
            .height(200.dp)
        )

        Column(
          verticalArrangement = Arrangement.spacedBy(16.dp),
          modifier = Modifier.padding(16.dp)
        ) {

          Text(
            text = state.details.title,
            style = MaterialTheme.typography.headlineSmall,
          )

          AssistChip(
            onClick = {},
            label = {
              Text(
                text = state.details.source,
                style = MaterialTheme.typography.labelMedium
              )
            }
          )

          Text(
            text = state.details.description,
            style = MaterialTheme.typography.bodyMedium,
          )

          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "Read the full story",
              style = MaterialTheme.typography.labelSmall,
            )

            val uriHandler = LocalUriHandler.current

            TextButton(
              onClick = { uriHandler.openUri(state.details.externalUrl) },
              shape = MaterialTheme.shapes.small,
            ) {
              Icon(
                imageVector = Icons.Rounded.ExitToApp,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
              )

              Text(
                text = state.details.externalUrl,
                style = MaterialTheme.typography.bodySmall,
              )
            }
          }

          Spacer(modifier = Modifier.weight(1f))

          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "Categories",
              style = MaterialTheme.typography.labelSmall,
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              items(state.details.categories) { category ->
                AssistChip(
                  onClick = {},
                  label = {
                    Text(
                      text = category,
                      style = MaterialTheme.typography.bodySmall
                    )
                  },
                  shape = MaterialTheme.shapes.extraLarge
                )
              }
            }
          }

          if (state.details.keywords.isNotEmpty()) Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "Keywords",
              style = MaterialTheme.typography.labelSmall,
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              items(state.details.keywords) { keyword ->
                AssistChip(
                  onClick = {},
                  label = {
                    Text(
                      text = keyword,
                      style = MaterialTheme.typography.bodySmall
                    )
                  },
                  shape = MaterialTheme.shapes.large
                )
              }
            }
          }
        }
      }
    }
  }
}
