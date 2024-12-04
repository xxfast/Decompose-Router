# Slot Navigation

Slot navigation is for managing one (or none) screen at a time, where user can activate or dismiss a screen

Typical use cases are
- Dialogs
- Bottom Sheets

Define your navigation model, (as already covered in [model-driven navigation section](using-decompose-router.md#model-driven-navigation))

```kotlin
@Serializable object SlotScreens
```

## Creating a router with slot navigation model

````kotlin
@Composable
fun SlotScreen() {
  val router: Router<SlotScreens> = rememberRouter { null }
}
````

## Using the router to open up a dialog

Use `RoutedContent` to use the router to open a dialog. 

```kotlin
@Composable
fun SlotScreen() {
  val router: Router<SlotScreens> = rememberRouter { null }

  RoutedContent(router) { _ ->
    AlertDialog(
      onDismissRequest = { dialogRouter.dismiss() },
    ) {
      // Dialog content here
    }
  }
}
```

## Navigating with slot navigation router

Decompose-router exposes the same Decompose page navigator extension [functions](https://arkivanov.github.io/Decompose/navigation/stack/navigation/#stacknavigator-extension-functions)

```kotlin
// A example of how to show a dialog
Button(
  onClick = { dialogRouter.activate(SlotScreens) }
) {
  Text("Show Dialog")
}
```

<seealso style="cards">
  <category ref="external">
    <a href="https://arkivanov.github.io/Decompose/navigation/slot/overview/">Decompose API Documentation for slots</a>
  </category>
</seealso>
