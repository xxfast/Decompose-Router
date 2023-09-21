package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.statekeeper.StateKeeper
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
  val stack: State<ChildStack<C, RouterContext>>,
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
  key: Any = "${type.key}.router",
  stack: List<C>,
  handleBackButton: Boolean = true
): Router<C> {
  val routerContext = LocalRouterContext.current
  val keyStr = key.toString()

  return remember {
    routerContext.getOrCreate(key = keyStr) {
      val navigation = StackNavigation<C>()
      Router(
        navigator = navigation,
        stack = routerContext.childStack(
          source = navigation,
          initialStack = { stack },
          configurationClass = type,
          key = keyStr,
          handleBackButton = handleBackButton,
          childFactory = { _, childComponentContext -> RouterContext(childComponentContext) },
        ).asState(routerContext.lifecycle),
      )
    }
  }
}

private fun <T : Any> Value<T>.asState(lifecycle: Lifecycle): State<T> {
  val state = mutableStateOf(value)
  observe(lifecycle = lifecycle) { state.value = it }
  return state
}

/***
 * Creates a instance of [T] that is scoped to the current route
 *
 * @param type class of [T] instance
 * @param key key to remember the instance with. Defaults to [type]'s key
 * @param block lambda to create an instance of [T] with a given [SavedStateHandle]
 */
@Suppress("UNCHECKED_CAST")
@Composable
fun <T : Instance> rememberOnRoute(
  type: KClass<T>,
  key: Any = type.key,
  block: @DisallowComposableCalls (savedState: SavedStateHandle) -> T
): T {
  val component: RouterContext = LocalRouterContext.current
  val stateKeeper: StateKeeper = component.stateKeeper
  val instanceKeeper: InstanceKeeper = component.instanceKeeper
  val instanceKey = "$key.instance"
  val stateKey = "$key.state"
  val (instance, savedState) = remember(key) {
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
