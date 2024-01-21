package io.github.xxfast.decompose.router.screens.stack

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.screens.stack.StackScreens.Details
import io.github.xxfast.decompose.router.screens.stack.StackScreens.List
import io.github.xxfast.decompose.router.screens.stack.details.DetailScreen
import io.github.xxfast.decompose.router.screens.stack.list.ListScreen
import io.github.xxfast.decompose.router.stack.RoutedContent
import io.github.xxfast.decompose.router.stack.Router
import io.github.xxfast.decompose.router.stack.rememberRouter

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun StackScreen() {
  val router: Router<StackScreens> = rememberRouter(StackScreens::class) { listOf(List) }

  RoutedContent(
    router = router,
    animation = predictiveBackAnimation(
      animation = stackAnimation(slide()),
      onBack = { router.pop() },
      backHandler = LocalRouterContext.current.backHandler
    )
  ) { screen ->
    when (screen) {
      List -> ListScreen(
        onSelect = { item -> router.push(Details(item)) },
      )

      is Details -> DetailScreen(
        item = screen.item,
        onBack = { router.pop() }
      )
    }
  }
}
