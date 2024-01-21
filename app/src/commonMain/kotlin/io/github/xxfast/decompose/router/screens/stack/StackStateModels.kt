package io.github.xxfast.decompose.router.screens.stack

import kotlinx.serialization.Serializable

@Serializable class Item(val index: Int)

@Serializable
sealed class StackScreens {
  @Serializable data object List: StackScreens()
  @Serializable data class Details(val item: Item): StackScreens()
}

