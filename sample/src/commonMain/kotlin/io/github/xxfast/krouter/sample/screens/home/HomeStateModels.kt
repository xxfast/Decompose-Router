package io.github.xxfast.krouter.sample.screens.home

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.github.xxfast.krouter.sample.models.StoryId

@Parcelize
sealed class StoryHomeScreen: Parcelable {
  object List: StoryHomeScreen()
  data class Details(val id: StoryId, val title: String): StoryHomeScreen()
}
