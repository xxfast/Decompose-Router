package io.github.xxfast.decompose.screen.nested

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class NestedScreens: Parcelable {
  data object Home: NestedScreens()
  data object Primary: NestedScreens()
  data object Secondary: NestedScreens()
  data object Tertiary: NestedScreens()
}
