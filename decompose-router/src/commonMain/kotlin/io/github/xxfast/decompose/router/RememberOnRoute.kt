package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.statekeeper.StateKeeper
import io.github.xxfast.decompose.SavedStateHandle
import io.github.xxfast.decompose.key
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

/***
 * Creates an instance of [T] that is scoped to the current route
 *
 * @param type class of [T] instance
 * @param key key to remember the instance with. Defaults to [type]'s key
 * @param block lambda to create an instance of [T] with a given [SavedStateHandle]
 */
@Composable
fun <T : InstanceKeeper.Instance, C: @Serializable Any> rememberOnRoute(
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
