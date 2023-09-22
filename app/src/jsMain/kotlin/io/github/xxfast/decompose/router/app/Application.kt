package io.github.xxfast.decompose.router.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.app.screens.HomeScreen
import io.github.xxfast.decompose.router.app.utils.BrowserViewportWindow
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
  val lifecycle = LifecycleRegistry()
  val rootRouterContext = RouterContext(lifecycle = lifecycle)

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
