package io.github.xxfast.krouter.sample.screens.topStories

import app.cash.molecule.RecompositionClock.Immediate
import app.cash.molecule.moleculeFlow
import io.github.xxfast.krouter.SavedStateHandle
import io.github.xxfast.krouter.ViewModel
import io.github.xxfast.krouter.sample.api.HttpClient
import io.github.xxfast.krouter.sample.api.NewsWebService
import io.github.xxfast.krouter.sample.screens.topStories.TopStoriesEvent.Page
import io.github.xxfast.krouter.sample.screens.topStories.TopStoriesEvent.Refresh
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TopStoriesViewModel(savedState: SavedStateHandle) : ViewModel() {
  private val eventsFlow: MutableSharedFlow<TopStoriesEvent> = MutableSharedFlow(5)
  private val initialState: TopStoriesState = savedState.get() ?: TopStoriesState(0, Loading)
  private val webService = NewsWebService(HttpClient)

  val states by lazy {
    moleculeFlow(Immediate) { TopStoriesDomain(initialState, eventsFlow, webService) }
      .stateIn(this, SharingStarted.Lazily, initialState)
  }

  fun onRefresh() { launch { eventsFlow.emit(Refresh) } }
  fun onPage(page: Int) { launch { eventsFlow.emit(Page(page)) } }
}
