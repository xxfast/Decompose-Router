package io.github.xxfast.decompose.router

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper

class RouterContext internal constructor(
  private val delegate: ComponentContext,
) : ComponentContext by delegate {

  constructor(
    lifecycle: Lifecycle,
    stateKeeper: StateKeeper? = null,
    instanceKeeper: InstanceKeeper? = null,
    backHandler: BackHandler? = null,
  ) : this(DefaultComponentContext(lifecycle, stateKeeper, instanceKeeper, backHandler))

  internal val storage: MutableMap<Any, Any> = HashMap()
}

internal inline fun <reified T : Any> RouterContext.getOrCreate(key: Any, factory: () -> T) : T {
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
