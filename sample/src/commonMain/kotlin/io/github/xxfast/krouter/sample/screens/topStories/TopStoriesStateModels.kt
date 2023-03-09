package io.github.xxfast.krouter.sample.screens.topStories

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.github.xxfast.krouter.sample.models.ArticleUri
import io.github.xxfast.krouter.sample.models.TopStorySection
import io.github.xxfast.krouter.sample.models.TopStorySections.home

val Loading: Nothing? = null

@Parcelize
data class TopStoriesState(
  val section: TopStorySection = home,
  val articles: List<TopStorySummaryState>? = Loading,
): Parcelable

val Default: TopStoriesState = TopStoriesState()

@Parcelize
data class TopStorySummaryState(
  val uri: ArticleUri,
  val imageUrl: String?,
  val title: String,
  val description: String,
  val section: TopStorySection
): Parcelable

sealed interface TopStoriesEvent {
  object Refresh: TopStoriesEvent
  data class Search(val query: String): TopStoriesEvent
}
