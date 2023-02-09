package io.github.xxfast.krouter.sample.models

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class TopStoryResponse(
  val meta: TopStoryMetadata,
  val data: List<NewsStory>,
)

@Serializable
data class TopStoryMetadata(
  val page: Int,
)

@Parcelize
@Serializable
@JvmInline
value class StoryId(val value: String):  Parcelable

@Serializable
data class NewsStory(
  val uuid: StoryId,
  val title: String,
  val description: String,
  val keywords: String,
  val snippet: String,
  val url: String,
  val image_url: String,
  val published_at: Instant,
  val source: String,
  val categories: List<String>,
)
