package io.github.xxfast.decompose.router

import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop

fun defaultRouterContext(): RouterContext {
  val backDispatcher = BackDispatcher()
  val lifecycle = LifecycleRegistry()
  return RouterContext(lifecycle = lifecycle, backHandler = backDispatcher)
}

private val RouterContext.lifecycleRegistry: LifecycleRegistry get() = this.lifecycle as LifecycleRegistry
fun RouterContext.destroy() = lifecycleRegistry.destroy()
fun RouterContext.resume() = lifecycleRegistry.resume()
fun RouterContext.stop() = lifecycleRegistry.stop()
fun RouterContext.pause() = lifecycleRegistry.pause()
