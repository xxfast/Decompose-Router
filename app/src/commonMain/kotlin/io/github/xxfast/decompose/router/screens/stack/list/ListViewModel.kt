package io.github.xxfast.decompose.router.screens.stack.list

import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.screens.stack.Item
import io.github.xxfast.decompose.router.state
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListViewModel(context: RouterContext): CoroutineScope {
  private val initialState: ListState = context.state(ListState()) { states.value }
  private val _state: MutableStateFlow<ListState> = MutableStateFlow(initialState)
  val states: StateFlow<ListState> = _state

  override val coroutineContext: CoroutineContext = Dispatchers.Main

  fun add(item: Item = Item(_state.value.screens.size)) = launch {
    val previous: ListState = _state.value
    val updated: ListState = previous.copy(screens = previous.screens.plus(item))
    _state.emit(updated)
  }
}
