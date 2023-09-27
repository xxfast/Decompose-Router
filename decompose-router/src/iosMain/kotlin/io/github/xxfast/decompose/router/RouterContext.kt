package io.github.xxfast.decompose.router

import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun defaultRouterContext(): RouterContext {
  val backDispatcher = BackDispatcher()
  val lifecycle = LifecycleRegistry()
  return RouterContext(lifecycle = lifecycle, backHandler = backDispatcher)
}

