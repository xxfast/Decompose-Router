package io.github.xxfast.decompose.router.app.screens.nested

import kotlinx.serialization.Serializable

@Serializable
sealed class NestedScreens {
  data object Home: NestedScreens()
  data object Primary: NestedScreens()
  data object Secondary: NestedScreens()
  data object Tertiary: NestedScreens()
}
