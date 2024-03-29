package io.github.xxfast.decompose.router.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.screens.HomeScreen
import io.github.xxfast.decompose.router.defaultRouterContext

fun main() {
  application {
    val windowState: WindowState = rememberWindowState()
    val rootRouterContext: RouterContext = defaultRouterContext(windowState = windowState)

    Window(
      title = "App",
      state = windowState,
      onCloseRequest = { exitApplication() }
    ) {
      CompositionLocalProvider(
        LocalRouterContext provides rootRouterContext,
      ) {
        MaterialTheme {
          HomeScreen()
        }
      }
    }
  }
}
