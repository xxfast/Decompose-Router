package io.github.xxfast.decompose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.ComponentContext

/***
 * Compositional local for component context
 *
 * Based on [Arkadii](https://github.com/arkivanov)'s [article](https://proandroiddev.com/a-comprehensive-hundred-line-navigation-for-jetpack-desktop-compose-5b723c4f256e)
 * Original [source](https://github.com/arkivanov/ComposeNavigatorExample/blob/d786d92632fe22e4d7874645ba2071fb813f9ace/navigator/src/commonMain/kotlin/com/arkivanov/composenavigatorexample/navigator/Navigator.kt)
 */
val LocalComponentContext: ProvidableCompositionLocal<ComponentContext> =
  staticCompositionLocalOf { error("Root component context was not provided") }
