package io.github.xxfast.krouter.sample.screens.topStories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.xxfast.krouter.rememberViewModel
import io.github.xxfast.krouter.sample.models.ArticleUri
import io.github.xxfast.krouter.sample.models.TopStorySection
import io.github.xxfast.krouter.sample.resources.icons.NewYorkTimes
import io.github.xxfast.krouter.sample.resources.icons.NewYorkTimesLogo
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import io.ktor.http.Url
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import io.github.xxfast.krouter.sample.resources.Icons as SampleIcons

@Composable
fun TopStoriesScreen(
  onSelect: (section: TopStorySection, uri: ArticleUri, title: String) -> Unit,
) {
  val viewModel: TopStoriesViewModel =
    rememberViewModel { savedState -> TopStoriesViewModel(savedState) }

  val state: TopStoriesState by viewModel.states.collectAsState()

  TopStoriesView(
    state = state,
    onSelect = onSelect,
    onRefresh = viewModel::onRefresh,
  )
}

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalMaterialApi::class,
  ExperimentalFoundationApi::class
)
@Composable
fun TopStoriesView(
  state: TopStoriesState,
  onRefresh: () -> Unit,
  onSelect: (section: TopStorySection, uri: ArticleUri, title: String) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  val refreshState = rememberPullRefreshState(state.articles == Loading, onRefresh)

  val lazyStaggeredGridState: LazyStaggeredGridState = rememberLazyStaggeredGridState()

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Icon(imageVector = SampleIcons.NewYorkTimes, contentDescription = null) },
        scrollBehavior = scrollBehavior,
        actions = {
          IconButton(onClick = onRefresh) { Icon(Icons.Rounded.Refresh, contentDescription = null) }
        }
      )
    },
    bottomBar = {
      BottomAppBar(
        contentPadding = PaddingValues(16.dp)
      ) {
        Icon(
          imageVector = SampleIcons.NewYorkTimesLogo,
          contentDescription = null,
          modifier = Modifier
            .size(32.dp)
            .padding(4.dp)
        )

        val year: Int = Clock.System.now()
          .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
          .year

        Column {
          Text(text = "Data provided by", style = MaterialTheme.typography.labelSmall)
          Text(text = "The New York Times © $year")
        }
      }
    },
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
  ) { scaffoldPadding ->
    Box(
      modifier = Modifier
        .pullRefresh(refreshState)
        .padding(scaffoldPadding)
        .fillMaxSize()
    ) {
      if (state.articles != Loading) LazyVerticalStaggeredGrid(
        state = lazyStaggeredGridState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        columns = StaggeredGridCells.Adaptive(250.dp),
      ) {
        items(state.articles) { summary -> StorySummaryView(summary, onSelect) }
      }

      PullRefreshIndicator(
        refreshing = state.articles == Loading,
        state = refreshState,
        contentColor = MaterialTheme.colorScheme.onSurface,
        backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
        modifier = Modifier.align(Alignment.TopCenter)
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorySummaryView(
  state: TopStorySummaryState,
  onSelect: (section: TopStorySection, uri: ArticleUri, title: String) -> Unit
) {
  Card(
    onClick = { onSelect(state.section, state.uri, state.title) },
    shape = MaterialTheme.shapes.medium
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.padding(8.dp)
    ) {
      if (state.imageUrl != null) KamelImage(
        resource = lazyPainterResource(Url(state.imageUrl)),
        contentDescription = null,
        onLoading = { CircularProgressIndicator(modifier = Modifier.align(Center)) },
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
          .clip(MaterialTheme.shapes.medium)
          .background(MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))
          .fillMaxWidth()
          .height(180.dp)
      )

      Text(
        text = state.title,
        style = MaterialTheme.typography.headlineSmall,
      )

      AssistChip(
        onClick = {},
        label = {
          Text(
            text = state.section.name,
            style = MaterialTheme.typography.labelMedium
          )
        }
      )

      Text(
        text = state.description,
        style = MaterialTheme.typography.bodyMedium,
      )
    }
  }
}
