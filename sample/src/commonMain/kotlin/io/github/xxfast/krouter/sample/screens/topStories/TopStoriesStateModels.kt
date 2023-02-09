package io.github.xxfast.krouter.sample.screens.topStories

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.github.xxfast.krouter.sample.models.StoryId

val Loading: Nothing? = null

@Parcelize
data class TopStoriesState(
  val page: Int = 1,
  val stories: List<TopStorySummaryState>? = Loading
): Parcelable

@Parcelize
data class TopStorySummaryState(
  val id: StoryId,
  val imageUrl: String,
  val title: String,
  val description: String,
  val source: String
): Parcelable

sealed interface TopStoriesEvent {
  object Refresh: TopStoriesEvent
  data class Page(val page: Int): TopStoriesEvent
  data class Search(val query: String): TopStoriesEvent
}
