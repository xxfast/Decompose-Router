package io.github.xxfast.decompose.router.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.essenty.parcelable.Parcelable
import io.github.xxfast.decompose.router.LocalRouter
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.RouterContext

/***
 * Composable to hoist content that are navigated by the router
 *
 * @param router Router to be used
 * @param modifier
 * @param animation [StackAnimation] used for transitions
 * @param content
 */
@Composable
fun <C : Parcelable> RoutedContent(
  router: Router<C>,
  modifier: Modifier = Modifier,
  animation: StackAnimation<C, RouterContext>? = null,
  content: @Composable (C) -> Unit,
) {
  CompositionLocalProvider(LocalRouter provides router) {
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
}
