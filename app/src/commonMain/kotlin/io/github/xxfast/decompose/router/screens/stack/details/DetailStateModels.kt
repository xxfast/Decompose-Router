package io.github.xxfast.decompose.router.screens.stack.details

import io.github.xxfast.decompose.router.screens.stack.Item
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class DetailState(
  val item: Item,
  val age: Duration = Duration.ZERO
)
