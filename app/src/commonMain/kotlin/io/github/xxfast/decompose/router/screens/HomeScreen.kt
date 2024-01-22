package io.github.xxfast.decompose.router.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CropSquare
import androidx.compose.material.icons.rounded.ImportContacts
import androidx.compose.material.icons.rounded.Reorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.pages.PagesScrollAnimation
import com.arkivanov.decompose.router.pages.select
import io.github.xxfast.decompose.router.pages.RoutedContent
import io.github.xxfast.decompose.router.pages.Router
import io.github.xxfast.decompose.router.pages.pagesOf
import io.github.xxfast.decompose.router.pages.rememberRouter
import io.github.xxfast.decompose.router.screens.HomeScreens.Page
import io.github.xxfast.decompose.router.screens.HomeScreens.Slot
import io.github.xxfast.decompose.router.screens.HomeScreens.Stack
import io.github.xxfast.decompose.router.screens.pages.PagesScreen
import io.github.xxfast.decompose.router.screens.slot.SlotScreen
import io.github.xxfast.decompose.router.screens.stack.StackScreen

@OptIn(ExperimentalDecomposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
  val pager: Router<HomeScreens> = rememberRouter(HomeScreens::class) { pagesOf(Stack, Page, Slot) }

  Scaffold(
    bottomBar = {
      NavigationBar {
        HomeScreens.entries.forEach { screen ->
          NavigationBarItem(
            selected = screen.ordinal == pager.pages.value.selectedIndex,
            icon = {
              Icon(
                imageVector = when (screen) {
                  Stack -> Icons.Rounded.Reorder
                  Page -> Icons.Rounded.ImportContacts
                  Slot -> Icons.Rounded.CropSquare
                },
                contentDescription = null,
              )
            },
            label = { Text(screen.name) },
            onClick = { pager.select(screen.ordinal) }
          )
        }
      }
    }
  ) { scaffoldPadding ->
    RoutedContent(
      router = pager,
      animation = PagesScrollAnimation.Disabled,
      pager = { modifier, state, key, pageContent ->
        HorizontalPager(
          modifier = modifier,
          state = state,
          key = key,
          pageContent = pageContent,
          userScrollEnabled = false,
        )
      },
      modifier = Modifier
        .padding(scaffoldPadding)
    ) { screen ->
      when (screen) {
        Stack -> StackScreen()
        Page -> PagesScreen()
        Slot -> SlotScreen()
      }
    }
  }
}
