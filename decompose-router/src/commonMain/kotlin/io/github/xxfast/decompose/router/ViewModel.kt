package io.github.xxfast.decompose.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.statekeeper.StateKeeper
import io.github.xxfast.decompose.LocalComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST") // ViewModels must be Instances
@Composable
fun <T : Instance> rememberViewModel(
  viewModelClass: KClass<T>,
  block: @DisallowComposableCalls (savedState: SavedStateHandle) -> T
): T {
  val component: ComponentContext = LocalComponentContext.current
  val stateKeeper: StateKeeper = component.stateKeeper
  val instanceKeeper: InstanceKeeper = component.instanceKeeper

  val packageName: String =
    requireNotNull(viewModelClass.simpleName) { "Unable to retain anonymous instance of $viewModelClass"}
  val viewModelKey = "$packageName.viewModel"
  val stateKey = "$packageName.savedState"

  val (viewModel, savedState) = remember(viewModelClass) {
    val savedState: SavedStateHandle = instanceKeeper
      .getOrCreate(stateKey) { SavedStateHandle(stateKeeper.consume(stateKey, SavedState::class)) }
    var viewModel: T? = instanceKeeper.get(viewModelKey) as T?
    if (viewModel == null) {
      viewModel = block(savedState)
      instanceKeeper.put(viewModelKey, viewModel)
    }
    viewModel to savedState
  }

  LaunchedEffect(Unit) {
    if (!stateKeeper.isRegistered(stateKey))
      stateKeeper.register(stateKey) { savedState.value }
  }

  return viewModel
}
