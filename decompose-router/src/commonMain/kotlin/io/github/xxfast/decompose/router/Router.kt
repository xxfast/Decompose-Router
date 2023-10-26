package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import kotlinx.serialization.*
import kotlin.reflect.KClass

/***
 * Router with a given navigator and a stack
 * Detailed breakdown of this available [here](https://proandroiddev.com/diy-compose-multiplatform-navigation-with-decompose-94ac8126e6b5)
 *
 * @param navigation decompose navigator to use
 * @param stack state of decompose child stack to use
 */
class Router<C: Any>(
  private val navigation: StackNavigation<C>,
  val stack: State<ChildStack<C, RouterContext>>,
) : StackNavigation<C> by navigation

/***
 * Compositional local for component context
 */
val LocalRouter: ProvidableCompositionLocal<Router<*>?> =
  staticCompositionLocalOf { null }

// TODO: Add this back to API once this [issue](https://github.com/JetBrains/compose-multiplatform/issues/2900) is fixed
//@Composable
//inline fun <reified C: @Serializable Any> rememberRouter(
//  key: Any = C::class,
//  handleBackButton: Boolean = true,
//  noinline initialStack: () -> List<C>,
//): Router<C> = rememberRouter(C::class, key, handleBackButton, initialStack)

/***
 * Creates a router that retains a stack of [C] configuration
 *
 * @param type configuration class type
 * @param key
 * @param initialStack initial stack of configurations
 * @param handleBackButton should the router handle back button
 */
@OptIn(InternalSerializationApi::class)
@Composable
fun <C: @Serializable Any> rememberRouter(
  type: KClass<C>,
  key: Any = type.key,
  handleBackButton: Boolean = true,
  initialStack: () -> List<C>,
): Router<C> {
  val routerContext: RouterContext = LocalRouterContext.current
  val routerKey = "$key.router"

  return remember(routerKey) {
    routerContext.getOrCreate(key = routerKey) {
      val navigation: StackNavigation<C> = StackNavigation()
      val stack: State<ChildStack<C, RouterContext>> = routerContext
        .childStack(
          source = navigation,
          serializer = type.serializerOrNull(),
          initialStack = initialStack,
          key = routerKey,
          handleBackButton = handleBackButton,
          childFactory = { _, childComponentContext -> RouterContext(childComponentContext) },
        )
        .asState(routerContext.lifecycle)

      Router(navigation, stack)
    }
  }
}

fun <T : Any> Value<T>.asState(lifecycle: Lifecycle): State<T> {
  val state = mutableStateOf(value)
  observe(lifecycle = lifecycle) { state.value = it }
  return state
}

/***
 * Creates an instance of [T] that is scoped to the current route
 *
 * @param type class of [T] instance
 * @param key key to remember the instance with. Defaults to [type]'s key
 * @param block lambda to create an instance of [T] with a given [SavedStateHandle]
 */
@Composable
fun <T : Instance, C: @Serializable Any> rememberOnRoute(
  type: KClass<T>,
  strategy: KSerializer<C>,
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
      .getOrCreate(stateKey) { SavedStateHandle(stateKeeper.consume(stateKey, strategy)) }
    var instance: T? = instanceKeeper.get(instanceKey) as T?
    if (instance == null) {
      instance = block(savedState)
      instanceKeeper.put(instanceKey, instance)
    }
    instance to savedState
  }

  LaunchedEffect(Unit) {
    if (!stateKeeper.isRegistered(stateKey))
      stateKeeper.register(stateKey, strategy) { savedState.value as C? }
  }

  return instance
}

/***
 * Retrieve a instance of [V] that is scoped to the current route
 *
 * @param type of [V] instance
 * @param childClass class of stack child[T]
 * @param keyBlock lambda to configure the key [which was used to save the instance [T]] by default [type].key
 */

inline fun <C : @Serializable Any, reified T : Any, reified V : Instance> Router<C>.getInstanceFromBackStack(
  type: KClass<V>,
  childClass: KClass<T>,
  block: ((childConfiguration: T) -> Any?) = { null }
): V {
  val child: Child.Created<C, RouterContext>? = this.stack.value.backStack
    .firstOrNull { it.configuration is T }

  require(child != null) {
    "Couldn't find the ${T::class} in the backstack"
  }

  val configuration: C = child.configuration
  require(configuration is T) {
    "Couldn't find the ${T::class} in the backstack"
  }

  val childConfiguration: T = configuration
  val key: Any? = block(childConfiguration)
  val keyInstance: String = if (key == null) "${type.key}.instance"
  else "${key}.instance"

  val viewModelInstance: Instance? = child.instance.instanceKeeper.get(keyInstance)

  require(viewModelInstance is V) {
    "Couldn't find the viewModel for  ${T::class} in the backstack"
  }

  return viewModelInstance
}