package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import kotlinx.serialization.Serializable

/***
 * Composable to hoist content that are navigated by the router
 *
 * @param router Router to be used
 * @param modifier
 * @param animation [StackAnimation] used for transitions
 * @param content
 */
@Composable
fun <C : @Serializable Any> RoutedContent(
  router: Router<C>,
  modifier: Modifier = Modifier,
  animation: StackAnimation<C, RouterContext>? = null,
  content: @Composable (C) -> Unit,
) {
  Children(
    stack = router.stack.value,
    modifier = modifier,
    animation = animation,
  ) { child ->
    CompositionLocalProvider(LocalRouterContext provides child.instance) {
      content(child.configuration)
    }
  }
}
