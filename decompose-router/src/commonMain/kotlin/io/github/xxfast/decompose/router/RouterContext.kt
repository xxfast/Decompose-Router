package io.github.xxfast.decompose.router

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

class RouterContext(
  private val delegate: ComponentContext,
) : ComponentContext by delegate {

  constructor(
    lifecycle: Lifecycle,
    stateKeeper: StateKeeper? = null,
    instanceKeeper: InstanceKeeper? = null,
    backHandler: BackHandler? = null,
  ) : this(DefaultComponentContext(lifecycle, stateKeeper, instanceKeeper, backHandler))

  val storage: MutableMap<Any, Any> = HashMap()
}

inline fun <reified T : Any> RouterContext.getOrCreate(key: Any, factory: () -> T): T {
  var instance: T? = storage[key] as T?
  if (instance == null) {
    instance = factory()
    storage[key] = instance
  }

  return instance
}

/***
 * Compositional local for [RouterContext].
 *
 * Based on [Arkadii](https://github.com/arkivanov)'s [article](https://proandroiddev.com/a-comprehensive-hundred-line-navigation-for-jetpack-desktop-compose-5b723c4f256e)
 * Original [source](https://github.com/arkivanov/ComposeNavigatorExample/blob/d786d92632fe22e4d7874645ba2071fb813f9ace/navigator/src/commonMain/kotlin/com/arkivanov/composenavigatorexample/navigator/Navigator.kt)
 */
val LocalRouterContext: ProvidableCompositionLocal<RouterContext> =
  staticCompositionLocalOf { error("Root RouterContext was not provided") }

@OptIn(InternalSerializationApi::class)
inline fun <reified T : @Serializable Any> RouterContext.state(
  initial: T,
  key: String = T::class.key,
  serializer: KSerializer<T> = T::class.serializer(),
  noinline supplier: () -> T,
): T {
  val state: T = stateKeeper.consume(key, serializer) ?: initial
  if (!stateKeeper.isRegistered(key)) stateKeeper.register(key, serializer, supplier)
  return state
}
