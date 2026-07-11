package io.github.xxfast.decompose.router.screens.items.item

import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.state
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

class ItemViewModel(context: RouterContext): CoroutineScope {
  private val initialState: ItemState = context.state(ItemState(0)) { states.value }
  private val _state: MutableStateFlow<ItemState> = MutableStateFlow(initialState)
  val states: StateFlow<ItemState> = _state

  override val coroutineContext: CoroutineContext = Dispatchers.Main

  init {
    launch {
      while (isActive) {
        delay(1.seconds)
        val previous: ItemState = _state.value
        val updated: ItemState = previous.copy(tick = previous.tick + 1)
        _state.emit(updated)
      }
    }
  }
}
