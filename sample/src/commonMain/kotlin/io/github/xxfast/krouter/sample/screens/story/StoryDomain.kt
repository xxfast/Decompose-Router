package io.github.xxfast.krouter.sample.screens.story

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.xxfast.krouter.sample.api.NewsWebService
import io.github.xxfast.krouter.sample.models.StoryId
import kotlinx.coroutines.flow.Flow

@Composable
fun StoryDomain(
  id: StoryId,
  title: String,
  initialState: StoryState,
  events: Flow<StoryEvent>,
  webService: NewsWebService
): StoryState {
  var details: StoryDetailsState? by remember { mutableStateOf(initialState.details) }
  var refresh: Int by remember { mutableStateOf(0) }

  LaunchedEffect(refresh) {
    details = Loading
    details = webService.story(id.value).getOrNull()
      ?.let { story ->
        StoryDetailsState(
          id = story.uuid,
          title = story.title,
          description =story.description,
          keywords = story.keywords.takeIf { it.isNotEmpty() }?.split(",").orEmpty(),
          snippet = story.snippet,
          externalUrl = story.url,
          imageUrl = story.image_url,
          source = story.source,
          categories = story.categories,
        )
      }
  }

  LaunchedEffect(Unit) {
    events.collect { event ->
      when (event) {
        StoryEvent.Refresh -> refresh++
      }
    }
  }

  return StoryState(
    title = title,
    details = details
  )
}
