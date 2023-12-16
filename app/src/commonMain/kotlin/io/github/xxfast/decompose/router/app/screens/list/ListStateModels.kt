package io.github.xxfast.decompose.router.app.screens.list

import kotlinx.serialization.Serializable

@Serializable
data class ListState(
    val items: List<Int>? = Loading,
    var visitedItems: Set<Int> = emptySet()
)

val Loading: Nothing? = null
