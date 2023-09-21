package io.github.xxfast.decompose.router

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import com.arkivanov.decompose.defaultComponentContext

fun ComponentActivity.defaultRouterContext(): RouterContext =
  RouterContext(defaultComponentContext())

fun Fragment.defaultRouterContext(
  onBackPressedDispatcher: OnBackPressedDispatcher?,
): RouterContext =
  RouterContext(defaultComponentContext(onBackPressedDispatcher))
