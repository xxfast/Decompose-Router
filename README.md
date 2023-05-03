# Decompose-Router
[![Kotlin Experimental](https://kotl.in/badges/experimental.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Build](https://github.com/xxfast/Decompose-Router/actions/workflows/android.yml/badge.svg)](https://github.com/xxfast/Decompose-Router/actions/workflows/android.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.21-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)

![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-wearos](http://img.shields.io/badge/platform-wearos-8ECDA0.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-4D76CD.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-ios-EAEAEA.svg?style=flat)
![badge-browser](https://img.shields.io/badge/platform-js-F8DB5D.svg?style=flat)

A Compose-multiplatform navigation library that leverage [Decompose](https://github.com/arkivanov/Decompose) to create an API inspired by [Conductor](https://github.com/bluelinelabs/Conductor)

A detailed breakdown available in this [Medium article](https://proandroiddev.com/diy-compose-multiplatform-navigation-with-decompose-94ac8126e6b5)

## Features
- 🚏 `Router`; A FILO stack for your screen configurations
- 📦 `ViewModel`; A instance that survives configuration changes and gets cleared when the user leaves the screen
- ☠️ `SavedState`; Restore your view models state after the process death.
- 🚉 Multiplatform!

## At a glance

```kotlin
sealed class Screen: Parcelable {
  @Parcelize object List : Screen()
  @Parcelize data class Details(val detail: String) : Screen()
}

@Composable
fun ListDetailScreen() {
  val router: Router<Screen> = rememberRouter(listOf(List))

  RoutedContent(
    router = router,
    animation = stackAnimation(slide()),
  ) { screen ->
    when (screen) {
      List -> ListScreen(onSelect { detail -> router.push(detail) } )
      is Details -> DetailsScreen(screen.detail)
    }
  }
}

@Composable
fun ListScreen(onSelect: (detail: String) -> Unit) {
  val viewModel: ListViewModel = 
    rememberViewModel { savedState -> ListViewModel(savedState) }
  
  val state: ListState by viewModel.states.collectAsState()

  LazyColumn {
    items(state.items) { item ->
      TextButton(onClick = { onSelect(item) } ) {
        Text(text = item)
      }
    }
  }
}

@Composable
fun DetailScreen(detail: String) {
  val viewModel: ListViewModel = 
    rememberViewModel(key = detail) { DetailsViewModel(detail) }

  val state: DetailsState by viewModel.states.collectAsState()

  Toolbar(title = detail)
  Text(state.descriptions)
}
```
