# Overview

<img src="decompose_router.svg" alt="Alt text" width="200"/>

[![Kotlin Experimental](https://kotl.in/badges/experimental.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Build](https://github.com/xxfast/Decompose-Router/actions/workflows/build.yml/badge.svg)](https://github.com/xxfast/Decompose-Router/actions/workflows/build.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.xxfast/decompose-router?color=blue)](https://search.maven.org/search?q=g:io.github.xxfast)

![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-wearos](http://img.shields.io/badge/platform-wearos-8ECDA0.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-4D76CD.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-EAEAEA.svg?style=flat)
![badge-browser](https://img.shields.io/badge/platform-js-F8DB5D.svg?style=flat)

A Compose-multiplatform navigation library that leverage [Decompose](https://github.com/arkivanov/Decompose) to create an API inspired by [Conductor](https://github.com/bluelinelabs/Conductor)

## Features
- 🚏 A `Router` that manages a stack, pages or slot for your screen configurations
- 📦 `rememberOnRoute()` lets you retain your state holders across configuration changes and gets cleared when the user leaves the screen
- 🚉 Multiplatform! Supports Android, WearOS, Desktop, iOS and Web

## At a glance

```kotlin
// Declare your screen configurations for type-safety
@Serializable
sealed class Screen : Parcelable {
  data object List : Screen()
  data class Details(val detail: String) : Screen()
}

@Composable
fun ListDetailScreen() {
  // Create a router with a stack of screen configurations 🚏
  val router: Router<Screen> = rememberRouter { listOf(List) }

  // Hoist your screens for each configuration 🏗️
  RoutedContent(router = router) { screen ->
    when (screen) {
      List -> ListScreen(
        // Navigate by pushing new configurations on the router 🧭
        onSelectItem = { detail -> router.push(detail) }
      )

      is Details -> DetailsScreen(screen.detail)
    }
  }
}

@Composable
fun DetailsScreen(detail: String) {
  // 📦 Scope an instance (a view model, a state-holder or whatever) to a route with [rememberOnRoute] 
  // This makes your instances survive configuration changes (on android) 🔁
  // And holds-on the instance as long as it is in the backstack 🔗
  // Pass in key if you want to reissue a new instance when key changes 🔑 (optional) 
  val viewModel: DetailViewModel = rememberOnRoute(key = detail) { // this: RouterContext 
    DetailViewModel(this, detail)
      // Optional, if you want your coroutine scope to be cancelled when the screen is removed from the backstack
      .apply { doOnDestroy { cancel() } }
  }

  val state: DetailState by viewModel.states.collectAsState()
  Text(text = state.detail)
}

class DetailViewModel(context: RouterContext, detail: String): CoroutineScope {
  // Optional, if you want to scope your coroutines to the lifecycle of this screen
  override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()
  
  // If you want your state to survive process death ☠️ derive your initial state from [RouterContext.state] 
  private val initialState: DetailState = context.state(DetailState(detail)) { states.value }
  private val stateFlow = MutableStateFlow(initialState)
  
  val states: StateFlow<DetailState> = stateFlow
}
```

<seealso style="cards">
  <category ref="external">
    <a href="https://proandroiddev.com/diy-compose-multiplatform-navigation-with-decompose-94ac8126e6b5">Medium article that started this off</a>
    <a href="https://xxfast.github.io/Decompose-Router/api/">API Doc</a>
  </category>
</seealso>
