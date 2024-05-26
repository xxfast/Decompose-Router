package io.github.xxfast.decompose.router

import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import kotlinx.browser.document
import org.w3c.dom.Document

fun defaultRouterContext(): RouterContext {
  val backDispatcher = BackDispatcher()
  val lifecycle = LifecycleRegistry()
  lifecycle.attachToDocument()
  return RouterContext(lifecycle = lifecycle, backHandler = backDispatcher)
}

// Attaches the LifecycleRegistry to the document
private fun LifecycleRegistry.attachToDocument() {
  fun onVisibilityChanged() = if (visibilityState(document) == "visible") resume() else stop()
  onVisibilityChanged()
  document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}

// Workaround for Document#visibilityState not available in Wasm
// From https://github.com/arkivanov/Minesweeper/blob/8270ffb0c75bf032b6d4da673c0bb2b01c9496ec/composeApp/src/wasmJsMain/kotlin/Main.kt#L47
@JsFun("(document) => document.visibilityState")
private external fun visibilityState(document: Document): String
