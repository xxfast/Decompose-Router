package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.stack.StackNavigation

@Composable
internal fun <C: Any> RouterContext.rememberNavigation(
  key: Any,
) : StackNavigation<C> {
  val navigationKey = "$key.navigation"
  return remember(navigationKey) { getOrCreate(key = navigationKey) { StackNavigation() } }
}
