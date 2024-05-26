package io.github.xxfast.decompose.router.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.pages.Pages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.pages.defaultHorizontalPager
import com.arkivanov.decompose.router.pages.select
import io.github.xxfast.decompose.router.LocalRouterContext
import kotlinx.serialization.Serializable

@OptIn(ExperimentalFoundationApi::class, ExperimentalDecomposeApi::class)
@Composable
fun <C : @Serializable Any> RoutedContent(
  router: Router<C>,
  modifier: Modifier = Modifier,
  animation: PagesScrollAnimation = PagesScrollAnimation.Default,
  pager: @Composable (
    Modifier,
    PagerState,
    key: (index: Int) -> Any,
    pageContent: @Composable PagerScope.(index: Int) -> Unit,
  ) -> Unit = defaultHorizontalPager(),
  content: @Composable (C) -> Unit,
) {
  Pages(
    pages = router.pages.value,
    onPageSelected = { index -> router.select(index) },
    modifier = modifier,
    pager = pager,
    scrollAnimation = animation,
  ) { index, page ->
    CompositionLocalProvider(LocalRouterContext provides page) {
      content(router.pages.value.items[index].configuration)
    }
  }
}
