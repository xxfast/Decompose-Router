package io.github.xxfast.decompose.router.app.screens

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.app.screens.HomeScreens.Details
import io.github.xxfast.decompose.router.app.screens.HomeScreens.Nested
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import io.github.xxfast.decompose.router.app.screens.details.DetailScreen
import io.github.xxfast.decompose.router.app.screens.list.ListScreen
import io.github.xxfast.decompose.router.app.screens.nested.NestedScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun HomeScreen() {
  val router: Router<HomeScreens> = rememberRouter(HomeScreens::class) { listOf(HomeScreens.List) }

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
        onSelect = { count -> router.push(Details(count)) },
        onSelectColor = { router.push(Nested) }
      )

      Nested -> NestedScreen(
        onBack = { router.pop() },
        onSelect = { count -> router.push(Details(count)) },
      )

      is Details -> DetailScreen(
        count = screen.number,
        onBack = { router.pop() }
      )
    }
  }
}
