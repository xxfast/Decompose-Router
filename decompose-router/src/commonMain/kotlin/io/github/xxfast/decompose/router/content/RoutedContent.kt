package io.github.xxfast.decompose.router.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.essenty.parcelable.Parcelable
import io.github.xxfast.decompose.LocalComponentContext
import io.github.xxfast.decompose.router.LocalRouter
import io.github.xxfast.decompose.router.Router

@Composable
fun <C : Parcelable> RoutedContent(
  router: Router<C>,
  modifier: Modifier = Modifier,
  animation: StackAnimation<C, ComponentContext>? = null,
  content: @Composable (C) -> Unit,
) {
  CompositionLocalProvider(LocalRouter provides router) {
    Children(
      stack = router.stack.value,
      modifier = modifier,
      animation = animation,
    ) { child ->
      CompositionLocalProvider(LocalComponentContext provides child.instance) {
        content(child.configuration)
      }
    }
  }
}
