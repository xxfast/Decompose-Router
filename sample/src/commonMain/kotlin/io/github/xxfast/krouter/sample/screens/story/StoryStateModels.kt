package io.github.xxfast.krouter.sample.screens.story

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.github.xxfast.krouter.sample.models.ArticleUri
import io.github.xxfast.krouter.sample.models.TopStorySection

val Loading: Nothing? = null

@Parcelize
data class StoryState(
  val title: String,
  val details: StoryDetailsState? = Loading
): Parcelable

@Parcelize
data class StoryDetailsState(
  val uri: ArticleUri,
  val title: String,
  val description: String,
  val externalUrl: String,
  val imageUrl: String?,
  val section: TopStorySection,
  val subsection: String,
): Parcelable

sealed interface StoryEvent {
  object Refresh: StoryEvent
}

