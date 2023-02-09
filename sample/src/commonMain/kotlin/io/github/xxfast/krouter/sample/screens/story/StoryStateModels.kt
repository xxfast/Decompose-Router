package io.github.xxfast.krouter.sample.screens.story

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.github.xxfast.krouter.sample.models.StoryId

val Loading: Nothing? = null

@Parcelize
data class StoryState(
  val title: String,
  val details: StoryDetailsState? = Loading
): Parcelable

@Parcelize
data class StoryDetailsState(
  val id: StoryId,
  val title: String,
  val description: String,
  val keywords: List<String>,
  val snippet: String,
  val externalUrl: String,
  val imageUrl: String,
  val source: String,
  val categories: List<String>,
): Parcelable

sealed interface StoryEvent {
  object Refresh: StoryEvent
}

