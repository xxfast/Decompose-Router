package io.github.xxfast.decompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ComponentContext
import io.github.xxfast.decompose.router.retainedComponentContext
import io.github.xxfast.decompose.screen.HomeScreen

class TestActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val rootComponentContext: ComponentContext = retainedComponentContext()

    setContent {
      Surface {
        CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
          MaterialTheme {
            HomeScreen()
          }
        }
      }
    }
  }
}
