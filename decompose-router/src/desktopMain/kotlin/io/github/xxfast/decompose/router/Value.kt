package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.essenty.lifecycle.Lifecycle

@Composable
internal actual fun <T : Any> Value<T>.subscribeAsState(lifecycle: Lifecycle): State<T> {
  val state = remember { mutableStateOf(value) }
  observe(lifecycle = lifecycle) { state.value = it }
  return state
}
