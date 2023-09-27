package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle

@Composable
internal expect fun <T : Any> Value<T>.subscribeAsState(lifecycle: Lifecycle): State<T>
