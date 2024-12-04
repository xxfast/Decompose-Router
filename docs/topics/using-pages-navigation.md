# Pages Navigation

Pages navigation is for managing a list of pages, where one selected page that is shown to the user.

Define your navigation model, (as already covered in [model-driven navigation section](using-decompose-router.md#model-driven-navigation))

```kotlin
@Serializable
enum class PagesScreens { Page1, Page2, Page3 }
```

## Creating a router with page navigation model

````kotlin
@Composable
fun PagesScreen() {
  val router: Router<PagesScreens> = rememberRouter { pagesOf(Page1, Page2, Page3) }
}
````

## Consuming the state from the router

Use `RoutedContent` to consume the state from the router.

```kotlin
@Composable
fun PagesScreen() {
  val router: Router<PagesScreens> = rememberRouter { pagesOf(Page1, Page2, Page3) }

  RoutedContent(router = router) { screen: PagesScreens ->
    when (screen) {
      Page1 -> Page1Screen()
      Page2 -> Page2Screen()
      Page3 -> Page3Screen()
    }
  }
}
```

## Navigating with page navigation router

Decompose-router exposes the same Decompose page navigator extension [functions](https://arkivanov.github.io/Decompose/navigation/stack/navigation/#stacknavigator-extension-functions)

```kotlin
val router: Router<PagesScreens> = rememberRouter { pagesOf(Page1, Page2, Page3) }

// To go to second page
Button(onClick = { number -> router.select(1) })

// To go back to first screen
Button(onClick = { router.selectFirst() })
```

<seealso style="cards">
  <category ref="external">
    <a href="https://arkivanov.github.io/Decompose/navigation/pages/overview/">Decompose API Documentation for pages</a>
  </category>
</seealso>
