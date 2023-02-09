package io.github.xxfast.krouter.sample.screens.topStories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.xxfast.krouter.sample.api.NewsWebService
import io.github.xxfast.krouter.sample.models.TopStoryResponse
import kotlinx.coroutines.flow.Flow

@Composable
fun TopStoriesDomain(
  initialState: TopStoriesState,
  events: Flow<TopStoriesEvent>,
  webService: NewsWebService
): TopStoriesState {
  var stories: List<TopStorySummaryState>? by remember { mutableStateOf(initialState.stories) }
  var page: Int by remember { mutableStateOf(initialState.page) }
  var refresh: Int by remember { mutableStateOf(0) }

  LaunchedEffect(refresh) {
    stories = Loading
    page = 1
  }

  LaunchedEffect(refresh, page){
    val topStory: TopStoryResponse = webService.topStories(page).getOrNull()
      ?: return@LaunchedEffect // TODO: Handle errors

    stories = stories.orEmpty() + topStory.data
      .map { story ->
        TopStorySummaryState(
          id = story.uuid,
          imageUrl = story.image_url,
          title = story.title,
          description = story.description,
          source = story.source,
        )
      }
  }

  LaunchedEffect(Unit) {
    events.collect { event ->
      when (event) {
        TopStoriesEvent.Refresh -> refresh++
        is TopStoriesEvent.Page -> page = event.page
        is TopStoriesEvent.Search -> TODO()
      }
    }
  }

  return TopStoriesState(page, stories)
}