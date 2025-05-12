package io.github.xxfast.decompose.router.wear.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.HierarchicalFocusCoordinator
import androidx.wear.compose.foundation.rememberSwipeToDismissBoxState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.SwipeToDismissBox
import androidx.wear.compose.material.SwipeToDismissKeys.Background
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.pop
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.stack.Router
import io.github.xxfast.decompose.router.RouterContext
import kotlinx.serialization.Serializable

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun <C : @Serializable Any> RoutedContent(
  router: Router<C>,
  modifier: Modifier = Modifier,
  content: @Composable (C) -> Unit,
) {
  val stack: ChildStack<C, RouterContext> by router.stack
  val active: Child.Created<C, RouterContext> = stack.active
  val background: Child.Created<C, RouterContext>? = stack.backStack.lastOrNull()
  val holder: SaveableStateHolder = rememberSaveableStateHolder()
  holder.RetainStates(stack.getConfigurations())

  // TODO fix in wear compose
  // without this the wrong state is remembered in SwipeToDismissBox
  val fudgeFactor: Int = stack.items.size
  val fudgeAmount: Float = 1f - ((fudgeFactor % 2) * 0.01f)
  val backgroundScrimColor: Color = MaterialTheme.colors.background.copy(alpha = fudgeAmount)

  SwipeToDismissBox(
    onDismissed = { router.pop() },
    state = rememberSwipeToDismissBoxState(),
    modifier = modifier,
    backgroundScrimColor = backgroundScrimColor,
    backgroundKey = background?.configuration ?: Background,
    hasBackground = background != null,
    contentKey = active.configuration,
  ) { isBackground ->
    val child = if (isBackground) requireNotNull(background) else active
    holder.SaveableStateProvider(child.configuration.key()) {
      CompositionLocalProvider(LocalRouterContext provides child.instance) {
        HierarchicalFocusCoordinator(requiresFocus = { !isBackground }) {
          content(child.configuration)
        }
      }
    }
  }
}

private fun ChildStack<*, *>.getConfigurations(): Set<String> =
  items.mapTo(HashSet()) { it.configuration.key() }

private fun Any.key(): String = "${this::class.simpleName}_${hashCode().toString(radix = 36)}"

@Composable
private fun SaveableStateHolder.RetainStates(currentKeys: Set<Any>) {
  val keys = remember(this) { Keys(currentKeys) }

  DisposableEffect(this, currentKeys) {
    keys.set.forEach {
      if (it !in currentKeys) {
        removeState(it)
      }
    }

    keys.set = currentKeys

    onDispose {}
  }
}

private class Keys(
  var set: Set<Any>
)
