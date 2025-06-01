package io.github.xxfast.decompose.router.items

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.ChildItems
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext

@OptIn(ExperimentalDecomposeApi::class)
inline fun <C : Any> LazyListScope.items(
  router: Router<C>,
  noinline key: ((item: C) -> Any)? = null,
  noinline contentType: (item: C) -> Any? = { null },
  crossinline itemContent: @Composable LazyItemScope.(item: C) -> Unit
) {
  val childItems: ChildItems<C, RouterContext> by router.items

  items(
    count = childItems.items.size,
    key = if (key != null) { index: Int -> key(childItems.items[index]) } else null,
    contentType = { index: Int -> contentType(childItems.items[index]) }
  ) {
    val item: C = childItems.items[it]
    val context: RouterContext = router.getContext(item)

    CompositionLocalProvider(LocalRouterContext provides context) {
      itemContent(item)
    }
  }
}

@OptIn(ExperimentalDecomposeApi::class)
inline fun <C : Any> LazyGridScope.items(
  router: Router<C>,
  noinline key: ((item: C) -> Any)? = null,
  noinline span: (LazyGridItemSpanScope.(item: C) -> GridItemSpan)? = null,
  noinline contentType: (item: C) -> Any? = { null },
  crossinline itemContent: @Composable LazyGridItemScope.(item: C) -> Unit
) {
  val childItems: ChildItems<C, RouterContext> by router.items

  items(
    count = childItems.items.size,
    key = if (key != null) { index: Int -> key(childItems.items[index]) } else null,
    span = if (span != null) { index: Int -> span(childItems.items[index]) } else null,
    contentType = { index: Int -> contentType(childItems.items[index]) }
  ) {
    val item: C = childItems.items[it]
    val context: RouterContext = router.getContext(item)

    CompositionLocalProvider(LocalRouterContext provides context) {
      itemContent(item)
    }
  }
}