package io.github.xxfast.decompose.router.slot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import io.github.xxfast.decompose.router.asState
import io.github.xxfast.decompose.router.key
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.getOrCreate
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KClass

class Router<C : Any> internal constructor(
  val navigation: SlotNavigation<C>,
  val slot: State<ChildSlot<C, RouterContext>>,
): SlotNavigation<C> by navigation

@OptIn(InternalSerializationApi::class)
@Composable
fun <C : @Serializable Any> rememberRouter(
  type: KClass<C>,
  key: Any = type.key,
  handleBackButton: Boolean = true,
  serializer: KSerializer<C>? = type.serializerOrNull(),
  initialConfiguration: () -> C?,
): Router<C> {
  val routerContext: RouterContext = LocalRouterContext.current
  val routerKey = "$key.router"

  return remember(routerKey) {
    routerContext.getOrCreate(key = routerKey) {
      val navigation: SlotNavigation<C> = SlotNavigation()
      val slot: State<ChildSlot<C, RouterContext>> = routerContext
        .childSlot(
          source = navigation,
          serializer = serializer,
          initialConfiguration = initialConfiguration,
          key = routerKey,
          handleBackButton = handleBackButton,
          childFactory = { _, childComponentContext -> RouterContext(childComponentContext) },
        )
        .asState(routerContext.lifecycle)

      Router(navigation, slot)
    }
  }
}
