package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.statekeeper.StateKeeper
import io.github.xxfast.decompose.LocalComponentContext
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

/***
 * Creates a instance of [T] that is scoped to the current route
 *
 * @param instanceClass class of [T] instance
 * @param block lambda to create an instance of [T] with a given [SavedStateHandle]
 */
@Suppress("UNCHECKED_CAST")
@Composable
fun <T : Instance> rememberOnRoute(
  instanceClass: KClass<T>,
  block: @DisallowComposableCalls (savedState: SavedStateHandle) -> T
): T {
  val component: ComponentContext = LocalComponentContext.current
  val stateKeeper: StateKeeper = component.stateKeeper
  val instanceKeeper: InstanceKeeper = component.instanceKeeper

  val packageName: String =
    requireNotNull(instanceClass.simpleName) { "Unable to retain anonymous instance of $instanceClass"}
  val instanceKey = "$packageName.instance"
  val stateKey = "$packageName.savedState"

  val (instance, savedState) = remember(instanceClass) {
    val savedState: SavedStateHandle = instanceKeeper
      .getOrCreate(stateKey) { SavedStateHandle(stateKeeper.consume(stateKey, SavedState::class)) }
    var instance: T? = instanceKeeper.get(instanceKey) as T?
    if (instance == null) {
      instance = block(savedState)
      instanceKeeper.put(instanceKey, instance)
    }
    instance to savedState
  }

  LaunchedEffect(Unit) {
    if (!stateKeeper.isRegistered(stateKey))
      stateKeeper.register(stateKey) { savedState.value }
  }

  return instance
}
