package io.github.xxfast.decompose.router.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.screens.HomeScreen
import io.github.xxfast.decompose.router.defaultRouterContext
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
  val rootRouterContext: RouterContext = defaultRouterContext()

  onWasmReady {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
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
