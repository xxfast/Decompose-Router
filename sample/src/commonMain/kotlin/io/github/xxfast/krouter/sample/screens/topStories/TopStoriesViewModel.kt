package io.github.xxfast.krouter.sample.screens.topStories

import app.cash.molecule.RecompositionClock.Immediate
import app.cash.molecule.moleculeFlow
import io.github.xxfast.krouter.SavedStateHandle
import io.github.xxfast.krouter.ViewModel
import io.github.xxfast.krouter.sample.api.HttpClient
import io.github.xxfast.krouter.sample.api.NyTimesWebService
import io.github.xxfast.krouter.sample.screens.topStories.TopStoriesEvent.Refresh
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TopStoriesViewModel(savedState: SavedStateHandle) : ViewModel() {
  private val eventsFlow: MutableSharedFlow<TopStoriesEvent> = MutableSharedFlow(5)
  private val initialState: TopStoriesState = savedState.get() ?: TopStoriesState()
  private val webService = NyTimesWebService(HttpClient)

  val states by lazy {
    moleculeFlow(Immediate) { TopStoriesDomain(initialState, eventsFlow, webService) }
      .onEach { state -> savedState.set(state) }
      .stateIn(this, SharingStarted.Lazily, initialState)
  }

  fun onRefresh() { launch { eventsFlow.emit(Refresh) } }
}
