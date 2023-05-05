
# Decompose Router

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://user-images.githubusercontent.com/13775137/236108051-73a54cd2-839a-4113-a8c0-25eeaad6b673.svg">
  <source media="(prefers-color-scheme: light)" srcset="https://user-images.githubusercontent.com/13775137/236108679-0ed87db8-fc1e-4f23-bcf7-3c10eeedc82a.svg">
  <img src="https://user-images.githubusercontent.com/13775137/236108679-0ed87db8-fc1e-4f23-bcf7-3c10eeedc82a.svg" height="128" align="right"> 
</picture>

[![Kotlin Experimental](https://kotl.in/badges/experimental.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Build](https://github.com/xxfast/Decompose-Router/actions/workflows/build.yml/badge.svg)](https://github.com/xxfast/Decompose-Router/actions/workflows/build.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.21-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.xxfast/decompose-router?color=blue)](https://search.maven.org/search?q=g:io.github.xxfast)

![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-wearos](http://img.shields.io/badge/platform-wearos-8ECDA0.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-4D76CD.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-EAEAEA.svg?style=flat)
![badge-browser](https://img.shields.io/badge/platform-js-F8DB5D.svg?style=flat)

A Compose-multiplatform navigation library that leverage [Decompose](https://github.com/arkivanov/Decompose) to create an API inspired by [Conductor](https://github.com/bluelinelabs/Conductor)

A detailed breakdown available in this [Medium article](https://proandroiddev.com/diy-compose-multiplatform-navigation-with-decompose-94ac8126e6b5)

## Features
- 🚏 A `Router` that manages a FILO stack for your screen configurations
- 📦 `rememberViewModel()` lets you retain your view model across configuration changes and gets cleared when the user leaves the screen
- ☠️ A `SavedStateHandle` to restore state gracefully after the process death. (for Android)
- 🚉 Multiplatform! Supports Android, WearOS, Desktop, iOS and Web

## Adding to your project

Decompose-Router is published on Maven Central
```kotlin
repositories {
  mavenCentral()
  // or for snapshot builds
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}
```

Include the dependency in `commonMain`. Latest version [![Maven Central](https://img.shields.io/maven-central/v/io.github.xxfast/decompose-router?color=blue)](https://search.maven.org/search?q=g:io.github.xxfast)

> **Note**
> Check for compatible versions of Compose Multiplatform, Decompose and Essenty in the [Version Catalog](gradle/libs.versions.toml)

<details>
  <summary>1. With version catalog</summary>

  **libs.version.toml**
  ```toml
  [versions]
  # Check in gradle/libs.versions.toml

  [libraries]
  # For Compose Multiplatform
  decompose-router = { module = "io.github.xxfast:decompose-router", version.ref = "decompose-router" }
  
  # For Compose Wear
  decompose-router-wear = { module = "io.github.xxfast:decompose-router-wear", version.ref = "decompose-router" }

  # You will probably need to also bring in decompose and essenty
  decompose = { module = "com.arkivanov.decompose:decompose", version.ref = "decompose" }
  decompose-compose-multiplatform = { module = "com.arkivanov.decompose:extensions-compose-jetbrains", version.ref = "decompose" }
  essenty-parcelable = { module = "com.arkivanov.essenty:parcelable", version.ref = "essenty" }
  ```

  **build.gradle.kts**
  ```kotlin
  sourceSets {
    // For Compose Multiplatform
    val commonMain by getting { 
      dependencies { 
        implementation(libs.decompose.router)
        
        // You will probably need to also bring in decompose and essenty
        implementation(libs.decompose)
        implementation(libs.decompose.compose.multiplatform)
        implementation(libs.essenty.parcelable)
      } 
    }
    
    // For Compose Wear
    val androidMain by getting {
      dependencies { 
        implementation(libs.decompose.router.wear)
      } 
    }
  }
  ```
</details>

<details>
  <summary>2. Without version catalog</summary>

  **build.gradle.kts**
  ```kotlin
  sourceSets {
    // For Compose Multiplatform
    val commonMain by getting {
      dependencies {
        implementation("io.github.xxfast:decompose-router:${versions.decompose-router}")
  
        // You will probably need to also bring in decompose and essenty
        implementation("com.arkivanov.decompose:decompose:${versions.decompose}")
        implementation("com.arkivanov.decompose:extensions-compose-jetbrains:${versions.decompose}")
        implementation("com.arkivanov.essenty:parcelable:${versions.essenty}")
      }
    }
  
    // For Compose Wear
    val androidMain by getting {
      dependencies {
        implementation("io.github.xxfast:decompose-router-wear:${versions.decompose-router}")
      }
    }
  }
  ```
</details>

## At a glance

```kotlin
// Declare your screen configurations for type-safety
sealed class Screen: Parcelable {
  @Parcelize object List : Screen()
  @Parcelize data class Details(val detail: String) : Screen()
}

@Composable
fun ListDetailScreen() {
  // Create a router with a stack of screen configurations 🚏
  val router: Router<Screen> = rememberRouter(listOf(List))

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

// If you want your view-model to survive config changes 🔁, make it implement the [Instance] interface
class ListViewModel : InstanceKeeper.Instance

// If you want your state to survive process death ☠️, derive your initial state from [SavedStateHandle] 
class DetailViewModel(savedState: SavedStateHandle, detail: String) : InstanceKeeper.Instance {
  private val initialState: DetailState = savedState.get() ?: DetailState(detail)
  private val stateFlow = MutableStateFlow(initialState)
  val states: StateFlow<DetailState> = stateFlow
}

@Composable
fun DetailsScreen(detail: String) {
  // Scope your view models to screen so that they get cleared when user leaves the screen 
  val viewModel: DetailViewModel = rememberViewModel { savedState -> DetailViewModel(savedState, detail) }
  
  val state: DetailState by viewModel.states.collectAsState()
  Text(text = state.detail)
}
```

## Platform configurations 🚉

<details>
  <summary>Android / WearOS</summary>

**build.gradle.kts**
  ```kotlin
  class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      val rootComponentContext: DefaultComponentContext = defaultComponentContext()
      setContent {
        CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
          MaterialTheme {
            ListDetailScreen()
          }
        }
      }
    }
  }
  ```
</details>

<details>
  <summary>Desktop</summary>

**build.gradle.kts**
  ```kotlin
  fun main() {
    val lifecycle = LifecycleRegistry()
    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)
    
    application {
      Window {
        CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
          MaterialTheme {
            ListDetailScreen()
          }
        }
      }
    }
  }
  ```
</details>

<details>
  <summary>iOS</summary>

**build.gradle.kts**
  ```kotlin
  fun main(): UIViewController = ComposeUIViewController {
    val lifecycle = LifecycleRegistry()
    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)
    CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
      MaterialTheme {
        ListDetailScreen()
      }
    }
  }

  ```
</details>

<details>
  <summary>Web</summary>

**build.gradle.kts**
  ```kotlin
  fun main() {
    onWasmReady {
      val lifecycle = LifecycleRegistry()
      val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)
  
      BrowserViewportWindow(..) {
        CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
          MaterialTheme {
            ListDetailScreen()
          }
        }
      }
    }
  }
  ```
</details>

## Licence

    Copyright 2023 Isuru Rajapakse

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
