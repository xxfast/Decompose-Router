package io.github.xxfast.decompose.router.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.ChildItems
import com.arkivanov.decompose.router.items.Items
import com.arkivanov.decompose.router.items.ItemsNavigation
import com.arkivanov.decompose.router.items.LazyChildItems
import com.arkivanov.decompose.router.items.childItems
import com.arkivanov.essenty.lifecycle.Lifecycle
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.asState
import io.github.xxfast.decompose.router.getOrCreate
import io.github.xxfast.decompose.router.key
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializerOrNull

@OptIn(ExperimentalDecomposeApi::class)
class Router<C : Any> @PublishedApi internal constructor(
  private val navigation: ItemsNavigation<C>,
  @PublishedApi internal val lazyItems: LazyChildItems<C, RouterContext>,
  private val lifecycle: Lifecycle
) : ItemsNavigation<C> by navigation {

  val items: State<ChildItems<C, RouterContext>>
    get() = lazyItems.asState(lifecycle)

  fun getContext(configuration: C): RouterContext = lazyItems[configuration]

}

@OptIn(InternalSerializationApi::class, ExperimentalDecomposeApi::class)
@Composable
inline fun <reified C : @Serializable Any> rememberRouter(
  key: Any = C::class.key,
  noinline initialItems: () -> List<C>,
): Router<C> {
  val routerContext = LocalRouterContext.current
  val routerKey = "$key.router"

  return remember(routerKey) {
    routerContext.getOrCreate(key = routerKey) {
      val navigation: ItemsNavigation<C> = ItemsNavigation()
      val childItems: LazyChildItems<C, RouterContext> = routerContext
        .childItems(
          source = navigation,
          serializer = C::class.serializerOrNull(),
          initialItems = { Items(items = initialItems()) },
          key = routerKey,
          childFactory = { _, childComponentContext -> RouterContext(childComponentContext) }
        )

      Router(navigation, childItems, routerContext.lifecycle)
    }
  }
}