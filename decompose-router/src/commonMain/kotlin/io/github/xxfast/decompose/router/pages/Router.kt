package io.github.xxfast.decompose.router.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.asState
import io.github.xxfast.decompose.router.getOrCreate
import io.github.xxfast.decompose.router.key
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KClass

class Router<C: Any> internal constructor(
  private val navigation: PagesNavigation<C>,
  val pages: State<ChildPages<C, RouterContext>>,
) : PagesNavigation<C> by navigation

/***
 * Creates a router that retains pages of [C] configuration
 * @param key
 * @param initialPages initial list of pages
 * @param handleBackButton should the router handle back button
 */
@Suppress("DEPRECATION") // For migration purposes
@Composable
inline fun <reified C: @Serializable Any> rememberRouter(
  key: Any = C::class,
  handleBackButton: Boolean = true,
  noinline initialPages: () -> Pages<C>,
): Router<C> = rememberRouter(
  type = C::class,
  key = key,
  handleBackButton = handleBackButton,
  initialPages = initialPages
)

@Deprecated(message = "Use rememberRouter with reified type parameter")
@OptIn(InternalSerializationApi::class)
@Composable
fun <C: @Serializable Any> rememberRouter(
  type: KClass<C>,
  key: Any = type.key,
  handleBackButton: Boolean = true,
  serializer: KSerializer<C>? = type.serializerOrNull(),
  initialPages: () -> Pages<C>,
): Router<C> {
  val routerContext: RouterContext = LocalRouterContext.current
  val routerKey = "$key.router"

  return remember(routerKey) {
    routerContext.getOrCreate(key = routerKey) {
      val navigation: PagesNavigation<C> = PagesNavigation()
      val stack: State<ChildPages<C, RouterContext>> = routerContext
        .childPages(
          source = navigation,
          serializer = serializer,
          initialPages = initialPages,
          key = routerKey,
          handleBackButton = handleBackButton,
          childFactory = { _, childComponentContext -> RouterContext(childComponentContext) },
        )
        .asState(routerContext.lifecycle)

      Router(navigation, stack)
    }
  }
}

@OptIn(ExperimentalDecomposeApi::class)
fun <C: Any> pagesOf(vararg pages: C, selectedIndex: Int = 0): Pages<C> = Pages(pages.toList(), selectedIndex)
