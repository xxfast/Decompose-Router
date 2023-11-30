package io.github.xxfast.decompose.router.app.screens.list

import kotlinx.serialization.Serializable

@Serializable data class ListState(val items: List<Int>? = Loading)

val Loading: Nothing? = null
