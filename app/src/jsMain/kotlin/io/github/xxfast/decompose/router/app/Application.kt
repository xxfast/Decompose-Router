package io.github.xxfast.decompose.router.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.app.screens.HomeScreen
import io.github.xxfast.decompose.router.app.utils.BrowserViewportWindow
import io.github.xxfast.decompose.router.js.defaultRouterContext
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
  val rootRouterContext: RouterContext = defaultRouterContext()

  onWasmReady {
    BrowserViewportWindow("App") {
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
