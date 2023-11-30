package io.github.xxfast.decompose.router.app.screens

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeScreens {
  @Serializable data object List: HomeScreens()
  @Serializable data object Nested: HomeScreens()
  @Serializable data class Details(val number: Int): HomeScreens()
}

