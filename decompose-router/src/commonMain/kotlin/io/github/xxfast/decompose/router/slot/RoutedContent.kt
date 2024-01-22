package io.github.xxfast.decompose.router.slot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.slot.ChildSlot
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import kotlinx.serialization.Serializable

/***
 * Composable to hoist content that are navigated by the router
 *
 * @param router Router to be used
 * @param content
 */
@Composable
fun <C : @Serializable Any> RoutedContent(
  router: Router<C>,
  content: @Composable (C) -> Unit,
) {
  val slot: ChildSlot<C, RouterContext> by router.slot
  val child: Child.Created<C, RouterContext>? = slot.child

  if (child != null) CompositionLocalProvider(LocalRouterContext provides child.instance) {
    content(child.configuration)
  }
}
