package io.github.xxfast.decompose.router.screens.stack.list

import io.github.xxfast.decompose.router.screens.stack.Item
import kotlinx.serialization.Serializable

@Serializable
data class ListState(val screens: List<Item> = List(5) { Item(it) })
