package io.github.xxfast.decompose.router.screens.stack.details

import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.screens.stack.Item
import io.github.xxfast.decompose.router.state
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

class DetailsViewModel(context: RouterContext, item: Item) : CoroutineScope {
  private val initialState: DetailState = context.state(DetailState(item)) { states.value }
  override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()

  val states: StateFlow<DetailState> = flow {
    var value = DetailState(item = item, age = 0.seconds)
    emit(value)
    while (isActive) {
      delay(1.seconds)
      value = value.copy(age = value.age + 1.seconds)
      emit(value)
    }
  }
  .stateIn(this, started = SharingStarted.Lazily, initialValue = initialState)
}
