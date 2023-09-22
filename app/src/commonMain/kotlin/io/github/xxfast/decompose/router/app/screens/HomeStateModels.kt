package io.github.xxfast.decompose.router.app.screens

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class HomeScreens: Parcelable {
  data object List: HomeScreens()
  data object Nested: HomeScreens()
  data class Details(val number: Int): HomeScreens()
}

