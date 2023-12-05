# Stack Navigation

Define your navigation model, (as already covered
in [model-driven navigation section](using-decompose-router.md#model-driven-navigation))

```kotlin
@Serializable
sealed class Screens {
  @Serializable
  data object List : Screens()
  @Serializable
  data class Details(val number: Int) : Screens()
}
```

## Creating a router with stack navigation model

````kotlin
@Composable
fun HomeScreen() {
  val router: Router<Screens> = rememberRouter(Screens::class) { 
    listOf(Screens.List) // Root screen to be set here
  }
}
````

> Due to this [issue](https://github.com/JetBrains/compose-multiplatform/issues/2900), you will still need to provide this
> type `Screen:class` manually for now.
> Once resolved, you will be able to use the `inline` `refied` (and nicer) signature
> ```kotlin
> val router: Router<Screens> = rememberRouter { listOf(Screens.List) }
> ```
{style="warning"}

## Consuming the state from the router

Use `RoutedContent` to consume the state from the router.

```kotlin
@Composable
fun HomeScreen() {
  val router: Router<Screens> = rememberRouter(Screens::class) { listOf(Screens.List) }

  RoutedContent(router = router) { screen: Screens ->
    when (screen) {
      HomeScreens.List -> ListScreen()
      is Details -> DetailScreen(screen.number)
    }
  }
}
```

## Navigating with stack navigation router

Decompose-router exposes the same Decompose stack navigator extension [functions](https://arkivanov.github.io/Decompose/navigation/stack/navigation/#stacknavigator-extension-functions)


```kotlin
val router: Router<HomeScreens> = rememberRouter(HomeScreens::class) { listOf(HomeScreens.List) }

// To go to details screen
Button(onClick = { number -> router.push(Details(number)) })

// To go back to list screen
Button(onClick = { router.pop() })
```

## Animating screen transitions 

Decompose-router simply exposes Decompose [API]( https://arkivanov.github.io/Decompose/extensions/compose/#animations)
```kotlin
RoutedContent(
  router = router,
  animation = predictiveBackAnimation(
    animation = stackAnimation(slide()),
  ),
)
```

### For predictive back animations 
Decompose-router simply exposes Decompose [API]( https://arkivanov.github.io/Decompose/extensions/compose/#predictive-back-gesture)

```kotlin
RoutedContent(
  router = router,
  animation = predictiveBackAnimation(
    animation = stackAnimation(slide()),
    onBack = { router.pop() },
    backHandler = LocalRouterContext.current.backHandler
  )
)
```

<seealso style="cards">
  <category ref="external">
    <a href="https://arkivanov.github.io/Decompose">Decompose API Documentation</a>
  </category>
</seealso>