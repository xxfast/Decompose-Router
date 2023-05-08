package io.github.xxfast.decompose.screen

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class Screen: Parcelable {
  object Game: Screen()
  data class Round(val number: Int): Screen()
}

val Loading: Nothing? = null
@Parcelize data class ListState(val items: List<Int>? = Loading): Parcelable
@Parcelize data class DetailState(val count: Int): Parcelable
