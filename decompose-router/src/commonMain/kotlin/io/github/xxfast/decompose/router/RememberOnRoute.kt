package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlin.reflect.KClass

/***
 * Scopes instance of [T] to the current route
 *
 * @param type class of [T] instance
 * @param key key to remember the instance with. Defaults to [type]'s key
 * @param block lambda to create an instance of [T] with a given [RouterContext]
 */
@Composable
fun <T: Any> rememberOnRoute(
  type: KClass<T>,
  key: Any = type.key,
  block: @DisallowComposableCalls (context: RouterContext) -> T
): T {
  class RouteInstance(val instance: T): InstanceKeeper.Instance
  val routerContext: RouterContext = LocalRouterContext.current
  val instanceKeeper: InstanceKeeper = routerContext.instanceKeeper
  val routeInstance: RouteInstance = remember(key) {
    instanceKeeper.getOrCreate(key) { RouteInstance(block(routerContext)) }
  }
  return routeInstance.instance
}
