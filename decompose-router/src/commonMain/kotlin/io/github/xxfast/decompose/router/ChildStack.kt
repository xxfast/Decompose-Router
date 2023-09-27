package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.essenty.parcelable.Parcelable
import kotlin.reflect.KClass

@Composable
internal fun <C : Parcelable> RouterContext.rememberChildStack(
  type: KClass<C>,
  key: Any,
  navigation: StackNavigationSource<C>,
  handleBackButton: Boolean = true,
  initialStack: () -> List<C>,
): State<ChildStack<C, RouterContext>> {
  val stackKey = "$key.stack"
  return remember(stackKey) {
    getOrCreate(key = stackKey) {
      childStack(
        source = navigation,
        initialStack = initialStack,
        configurationClass = type,
        key = stackKey,
        handleBackButton = handleBackButton,
        childFactory = { _, childComponentContext -> RouterContext(childComponentContext) },
      )
    }
  }
  .subscribeAsState(lifecycle)
}
