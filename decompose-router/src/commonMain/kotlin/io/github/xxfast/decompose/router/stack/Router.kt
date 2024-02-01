package io.github.xxfast.decompose.router.stack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.asState
import io.github.xxfast.decompose.router.getOrCreate
import io.github.xxfast.decompose.router.key
import kotlinx.serialization.*
import kotlin.reflect.KClass

/***
 * Router with a given navigator and a stack
 * Detailed breakdown of this available [here](https://proandroiddev.com/diy-compose-multiplatform-navigation-with-decompose-94ac8126e6b5)
 *
 * @param navigation decompose navigator to use
 * @param stack state of decompose child stack to use
 */
class Router<C: Any> internal constructor(
  private val navigation: StackNavigation<C>,
  val stack: State<ChildStack<C, RouterContext>>,
) : StackNavigation<C> by navigation

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
  serializer: KSerializer<C>? = type.serializerOrNull(),
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
          serializer = serializer,
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
