package io.github.xxfast.decompose.router.js

import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import io.github.xxfast.decompose.router.RouterContext
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
  fun onVisibilityChanged() = if (document.visibilityState == "visible") resume() else stop()
  onVisibilityChanged()
  document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}

private val Document.visibilityState: String
  get() = asDynamic().visibilityState.unsafeCast<String>()

