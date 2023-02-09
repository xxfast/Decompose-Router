package io.github.xxfast.krouter.sample.screens.home

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.krouter.RoutedContent
import io.github.xxfast.krouter.Router
import io.github.xxfast.krouter.rememberRouter
import io.github.xxfast.krouter.sample.screens.home.StoryHomeScreen.Details
import io.github.xxfast.krouter.sample.screens.home.StoryHomeScreen.List
import io.github.xxfast.krouter.sample.screens.story.StoryScreen
import io.github.xxfast.krouter.sample.screens.topStories.TopStoriesScreen

@Composable
fun HomeScreen() {
  val router: Router<StoryHomeScreen> = rememberRouter(listOf(List))

  RoutedContent(
    router = router,
    animation = stackAnimation(slide())
  ) { screen ->
    when(screen){
      List -> TopStoriesScreen(onSelect = { id, title -> router.push(Details(id, title))})
      is Details -> StoryScreen(id = screen.id, title = screen.title, onBack = { router.pop() })
    }
  }
}