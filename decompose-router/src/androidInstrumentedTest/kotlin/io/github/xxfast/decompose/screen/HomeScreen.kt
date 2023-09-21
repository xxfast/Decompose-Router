package io.github.xxfast.decompose.screen

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import io.github.xxfast.decompose.screen.details.DetailScreen
import io.github.xxfast.decompose.screen.list.ListScreen
import io.github.xxfast.decompose.screen.nested.NestedScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun HomeScreen() {
  val router: Router<HomeScreens> =
    rememberRouter(HomeScreens::class, stack = listOf(HomeScreens.List))

  RoutedContent(
    router = router,
    animation = predictiveBackAnimation(
      animation = stackAnimation(slide()),
      onBack = { router.pop() },
      backHandler = LocalRouterContext.current.backHandler
    )
  ) { screen ->
    when (screen) {
      HomeScreens.List -> ListScreen(
        onSelect = { count -> router.push(HomeScreens.Details(count)) },
        onSelectColor = { router.push(HomeScreens.Nested) }
      )

      HomeScreens.Nested -> NestedScreen(
        onBack = { router.pop() },
        onSelect = { count -> router.push(HomeScreens.Details(count)) },
      )

      is HomeScreens.Details -> DetailScreen(
        count = screen.number,
        onBack = { router.pop() }
      )
    }
  }
}
