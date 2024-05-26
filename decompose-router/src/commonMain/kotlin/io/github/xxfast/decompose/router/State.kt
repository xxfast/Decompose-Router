package io.github.xxfast.decompose.router

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.essenty.lifecycle.Lifecycle

internal fun <T : Any> Value<T>.asState(lifecycle: Lifecycle): State<T> {
  val state = mutableStateOf(value)
  subscribe(lifecycle = lifecycle) { state.value = it }
  return state
}
