package io.github.xxfast.decompose.router.screens.stack.list

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import io.github.xxfast.decompose.SavedStateHandle
import io.github.xxfast.decompose.router.screens.stack.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListInstance(private val savedStateHandle: SavedStateHandle) : Instance, CoroutineScope {
  private val initialState: ListState = savedStateHandle.get() ?: ListState()

  private val _state: MutableStateFlow<ListState> = MutableStateFlow(initialState)
  val state: StateFlow<ListState> = _state

  override val coroutineContext: CoroutineContext = Dispatchers.Main

  fun add(item: Item = Item(_state.value.screens.size)) = launch {
    val previous: ListState = _state.value
    val updated: ListState = previous.copy(screens = previous.screens.plus(item))
    _state.emit(updated)
    savedStateHandle.set(updated)
  }

  override fun onDestroy() {
    coroutineContext.cancel()
  }
}
