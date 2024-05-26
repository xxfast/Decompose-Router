package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@Composable
fun defaultRouterContext(
  backDispatcher: BackDispatcher = BackDispatcher(),
  lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(),
  windowState: WindowState = rememberWindowState(),
): RouterContext {
  LifecycleController(lifecycleRegistry, windowState)
  return RouterContext(lifecycle = lifecycleRegistry, backHandler = backDispatcher)
}
