package io.github.xxfast.krouter.sample.screens.story

import app.cash.molecule.RecompositionClock.Immediate
import app.cash.molecule.moleculeFlow
import io.github.xxfast.krouter.SavedStateHandle
import io.github.xxfast.krouter.ViewModel
import io.github.xxfast.krouter.sample.api.HttpClient
import io.github.xxfast.krouter.sample.api.NewsWebService
import io.github.xxfast.krouter.sample.models.StoryId
import io.github.xxfast.krouter.sample.screens.story.StoryEvent.Refresh
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StoryViewModel(private val savedState: SavedStateHandle, id: StoryId, title: String): ViewModel() {
  private val eventsFlow: MutableSharedFlow<StoryEvent> = MutableSharedFlow(5)
  private val initialState: StoryState = savedState.get() ?: StoryState(title, Loading)
  private val webService = NewsWebService(HttpClient)

  val states: StateFlow<StoryState> by lazy {
    moleculeFlow(Immediate) { StoryDomain(id, title, initialState, eventsFlow, webService) }
      .onEach { state -> savedState.set(state) }
      .stateIn(this, SharingStarted.Lazily, initialState)
  }

  fun onRefresh() { launch { eventsFlow.emit(Refresh) } }
}
