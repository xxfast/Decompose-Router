package io.github.xxfast.decompose.router.app.utils

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.defaultRouterContext

fun defaultRouterContext(): RouterContext = defaultRouterContext()
val Lifecycle.registry get() = this as LifecycleRegistry
fun Lifecycle.destroy() = registry.destroy()
fun Lifecycle.resume() = registry.resume()
fun Lifecycle.stop() = registry.stop()
