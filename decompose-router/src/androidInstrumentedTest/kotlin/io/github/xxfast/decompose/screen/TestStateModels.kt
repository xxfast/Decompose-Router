package io.github.xxfast.decompose.screen

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class Screen: Parcelable {
  object List: Screen()
  data class Detail(val detail: String): Screen()
}

val Loading: Nothing? = null
@Parcelize data class ListState(val items: List<String>? = Loading): Parcelable
@Parcelize data class DetailState(val detail: String): Parcelable
