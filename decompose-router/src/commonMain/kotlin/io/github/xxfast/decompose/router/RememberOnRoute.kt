package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass


/***
 * Scopes instance of [T] to the current route
 * @param key key to remember the instance with. Defaults to [T]'s [io.github.xxfast.decompose.router.key]
 * @param block lambda to create an instance of [T] with a given [RouterContext]
 */
@Suppress("DEPRECATION") // For migration purposes
@Composable
inline  fun <reified T: @Serializable Any> rememberOnRoute(
  key: Any = T::class,
  noinline block: @DisallowComposableCalls RouterContext.() -> T
): T = rememberOnRoute(T::class, key, block)

/***
 * Scopes instance of [T] to the current route
 *
 * @param type class of [T] instance
 * @param key key to remember the instance with. Defaults to [type]'s [io.github.xxfast.decompose.router.key]
 * @param block lambda to create an instance of [T] with a given [RouterContext]
 */
@Deprecated("Use the inline variant above")
@Composable
fun <T: Any> rememberOnRoute(
  type: KClass<T>,
  key: Any = type.key,
  block: @DisallowComposableCalls RouterContext.() -> T
): T {
  class RouteInstance(val instance: T): InstanceKeeper.Instance
  val routerContext: RouterContext = LocalRouterContext.current
  val instanceKeeper: InstanceKeeper = routerContext.instanceKeeper
  val routeInstance: RouteInstance = remember(key) {
    instanceKeeper.getOrCreate(key) { RouteInstance(block(routerContext)) }
  }
  return routeInstance.instance
}
