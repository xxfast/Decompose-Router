package io.github.xxfast.decompose.router.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
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
import androidx.compose.ui.platform.testTag
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.router.pages.select
import io.github.xxfast.decompose.router.pages.RoutedContent
import io.github.xxfast.decompose.router.pages.Router
import io.github.xxfast.decompose.router.pages.pagesOf
import io.github.xxfast.decompose.router.pages.rememberRouter
import io.github.xxfast.decompose.router.screens.HomeScreens.Pages
import io.github.xxfast.decompose.router.screens.HomeScreens.Slot
import io.github.xxfast.decompose.router.screens.HomeScreens.Stack
import io.github.xxfast.decompose.router.screens.HomeScreens.Items
import io.github.xxfast.decompose.router.screens.items.ItemsScreen
import io.github.xxfast.decompose.router.screens.pages.PagesScreen
import io.github.xxfast.decompose.router.screens.slot.SlotScreen
import io.github.xxfast.decompose.router.screens.stack.StackScreen

@Composable
fun HomeScreen() {
  val pager: Router<HomeScreens> = rememberRouter { pagesOf(Stack, Pages, Slot, Items) }

  Scaffold(
    bottomBar = {
      NavigationBar(
        modifier = Modifier.testTag(BOTTOM_NAV_BAR)
      ) {
        HomeScreens.entries.forEach { screen ->
          NavigationBarItem(
            selected = screen.ordinal == pager.pages.value.selectedIndex,
            icon = {
              Icon(
                imageVector = when (screen) {
                  Stack -> Icons.Rounded.Reorder
                  Pages -> Icons.Rounded.ImportContacts
                  Slot -> Icons.Rounded.CropSquare
                  Items -> Icons.AutoMirrored.Rounded.List
                },
                contentDescription = null,
              )
            },
            label = { Text(screen.name) },
            onClick = { pager.select(screen.ordinal) },
            modifier = Modifier.testTag(
              when(screen) {
                Stack -> BOTTOM_NAV_STACK
                Pages -> BOTTOM_NAV_PAGES
                Slot -> BOTTOM_NAV_SLOT
                Items -> BOTTOM_NAV_ITEMS
              }
            )
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
        Pages -> PagesScreen()
        Slot -> SlotScreen()
        Items -> ItemsScreen()
      }
    }
  }
}
