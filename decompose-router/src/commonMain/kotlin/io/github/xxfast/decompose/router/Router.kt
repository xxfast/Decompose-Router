package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.essenty.parcelable.Parcelable
import io.github.xxfast.decompose.rememberChildStack
import kotlin.reflect.KClass

/***
 * Router with a given navigator and a stack
 * Detailed breakdown of this available [here](https://proandroiddev.com/diy-compose-multiplatform-navigation-with-decompose-94ac8126e6b5)
 *
 * @param navigator decompose navigator to use
 * @param stack state of decompose child stack to use
 */
class Router<C : Parcelable>(
  private val navigator: StackNavigation<C>,
  val stack: State<ChildStack<C, ComponentContext>>,
) : StackNavigation<C> by navigator

/***
 * Compositional local for component context
 */
val LocalRouter: ProvidableCompositionLocal<Router<*>?> =
  staticCompositionLocalOf { null }

/***
 * Creates a router that retains a stack of [C] configuration
 *
 * @param type configuration class type
 * @param stack initial stack of configurations
 * @param handleBackButton should the router handle back button
 */
@Composable
fun <C : Parcelable> rememberRouter(
  type: KClass<C>,
  stack: List<C>,
  handleBackButton: Boolean = true
): Router<C> {
  val navigator: StackNavigation<C> = remember { StackNavigation() }

  val packageName: String =
    requireNotNull(type.simpleName) { "Unable to retain anonymous instance of $type"}

  val childStackState: State<ChildStack<C, ComponentContext>> = rememberChildStack(
    source = navigator,
    initialStack = { stack },
    key = packageName,
    handleBackButton = handleBackButton,
    type = type,
  )

  return remember { Router(navigator = navigator, stack = childStackState) }
}
