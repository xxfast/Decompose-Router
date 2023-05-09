package io.github.xxfast.decompose.screen

import com.arkivanov.essenty.instancekeeper.InstanceKeeper.Instance
import io.github.xxfast.decompose.router.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext

class ListInstance(savedStateHandle: SavedStateHandle) : Instance, CoroutineScope {
  private val initialState: ListState = savedStateHandle.get() ?: ListState()

  // TODO: This has to be lazy for some weird reason
  val state: StateFlow<ListState> by lazy {
    flow {
      emit(ListState(Loading))
      delay(300L)
      emit(ListState((1.. 100).toList()))
    }
      .stateIn(this, Lazily, initialState)
  }

  override val coroutineContext: CoroutineContext = Dispatchers.Main

  override fun onDestroy() {
    coroutineContext.cancel()
  }
}
