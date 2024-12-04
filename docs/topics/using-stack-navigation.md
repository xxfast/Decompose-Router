# Stack Navigation

Stack navigation is for managing a stack of screens, where only screen at the top of the stack is shown to the user.

Define your navigation model, (as already covered in [model-driven navigation section](using-decompose-router.md#model-driven-navigation))

```kotlin
@Serializable
sealed class StackScreens {
  @Serializable
  data object List : StackScreens()
  @Serializable
  data class Details(val number: Int) : StackScreens()
}
```

## Creating a router with stack navigation model

````kotlin
@Composable
fun StackScreen() {
  val router: Router<StackScreens> = rememberRouter { 
    listOf(StackScreens.List) // Root screen to be set here
  }
}
````

## Consuming the state from the router

Use `RoutedContent` to consume the state from the router.

```kotlin
@Composable
fun StackScreen() {
  val router: Router<StackScreens> = rememberRouter { listOf(StackScreens.List) }

  RoutedContent(router = router) { screen: StackScreens ->
    when (screen) {
      StackScreens.List -> ListScreen()
      is StackScreens.Details -> DetailScreen(screen.number)
    }
  }
}
```

## Navigating with stack navigation router

Decompose-router exposes the same Decompose stack navigator extension [functions](https://arkivanov.github.io/Decompose/navigation/stack/navigation/#stacknavigator-extension-functions)


```kotlin
val router: Router<StackScreens> = rememberRouter { listOf(StackScreens.List) }

// To go to details screen
Button(onClick = { number -> router.push(StackScreens.Details(number)) })

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
