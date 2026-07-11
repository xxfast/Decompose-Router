package io.github.xxfast.decompose.router.items

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lazyitems.ChildItemsLifecycleController

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun <C : Any> ItemsRouterLifecycleController(
  router: Router<C>,
  lazyListState: LazyListState,
  itemIndexConverter: (Int) -> Int,
  forwardPreloadCount: Int = 0,
  backwardPreloadCount: Int = 0,
) {
  ChildItemsLifecycleController(
    items = router.lazyItems,
    lazyListState = lazyListState,
    itemIndexConverter = itemIndexConverter,
    forwardPreloadCount = forwardPreloadCount,
    backwardPreloadCount = backwardPreloadCount
  )
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun <C : Any> ItemsRouterLifecycleController(
  router: Router<C>,
  lazyGridState: LazyGridState,
  itemIndexConverter: (Int) -> Int,
  forwardPreloadCount: Int = 0,
  backwardPreloadCount: Int = 0,
) {
  ChildItemsLifecycleController(
    items = router.lazyItems,
    lazyGridState = lazyGridState,
    itemIndexConverter = itemIndexConverter,
    forwardPreloadCount = forwardPreloadCount,
    backwardPreloadCount = backwardPreloadCount
  )
}