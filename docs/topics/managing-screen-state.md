# Managing Screen State

<title instance="decompose-router">Managing Screen State</title>
**State** (or UiState) model usually capture everything on a given screen that can change over time. This state is usually contained by a **State Holder** (e.g:- a `Component`, `ViewModel`, `Presenter` or whatever) and mutate that state over time by applying business logic 

## Scoping a State Holder to a Router

If you want your screen level state holder to be scoped to a given screen, use `rememberOnRoute`
1. This makes your instances survive configuration changes (on Android)
2. Holds-on the instance as long as it is in the backstack

```kotlin
class List

@Composable
fun ListScreen() {
  val list: List = rememberOnRoute(List::class) { _ -> List() }
}
```

If you want this instance to be recomputed, you can also provide a key to it 
```kotlin
class Details(val id: String)

@Composable
fun DetailsScreen(id: String) {
  val details: Details = rememberOnRoute(Details::class, key = id) { _ -> Details(id) }
}
```
> Due to this [issue](https://github.com/JetBrains/compose-multiplatform/issues/2900), you will still need to provide this
> type `ListScreen:class` manually for now.
> Once resolved, you will be able to use the `inline` `refied` (and nicer) signature
> ```kotlin
> val list: List = rememberOnRoute { _ -> List() }
> ```
{style="warning"}

### Integrating with Decompose Components

Integrating Decompose components works the same way. `RouterContext` can provide you a `ComponentContext` if needed be

```kotlin
import com.arkivanov.decompose.ComponentContext

class ListComponent(context: RouterContext): ComponentContext by context

@Composable
fun MyScreen() {
  val listComponent: ListComponent = rememberOnRoute(ListComponent::class) { context ->
    ListComponent(context)
  }
}
```
{collapsible="true" collapsed-title="class ListComponent(context: RouterContext): com.arkivanov.decompose.ComponentContext by context"}

### Integrating with Android Architecture Component ViewModels

Integrating [AAC `ViewModel`](https://developer.android.com/topic/libraries/architecture/viewmodel)s works the same way. You can scope `ViewModel`s directly and these will be stay on the stack/page/slot as long it is needed 

```kotlin
import androidx.lifecycle.ViewModel

class ListViewModel: ViewModel()

@Composable
fun ListScreen() {
  val viewModel: ListViewModel = rememberOnRoute(type = ListViewModel::class) { ListViewModel() }
}
```
{collapsible="true" collapsed-title="class ListViewModel: androidx.lifecycle.ViewModel()"}

## Restoring Initial State after System-initiated Process Death

> [System-initiated process death](https://developer.android.com/topic/libraries/architecture/saving-states#onsaveinstancestate) may kill your android process when the system is running out of memory.
> 
> Router will restore the screens, backstack and any savable states in them (like scroll position) automatically
> 
> However, you will still need to save and restore state withing your state holders


**Initial state** is the first-ever state your screen is rendered with. This is usually the **default state** for your screen. 
However, if your app is being restored after system initiated process death, we want to _derive_ the initial state 
from **saved state** of the previous process (instead of the default)

Within your **State Holder**, you can derive the initial state by using `RouterContext.state`. 
Make sure to point the supplier lambda back to your state flow so that it knows where to grab the latest state from to save 

```kotlin
class List(context: RouterContext) {
  private val initialState: ListState = context.state(ListState()) { states.value }
  private val _state: MutableStateFlow<ListState> = MutableStateFlow(initialState)
  val states: StateFlow<ListState> = _state
}
```

### Integrating with Molecule

For [Molecule](https://github.com/cashapp/molecule), initial state can be provided to your `moleculeFlow` in conjunction with `stateIn` 

```kotlin
class List(context: RouterContext) {
  private val initialState: ListState = context.state(ListState()) { states.value }
  val states: StateFlow<EquipmentSelectionState> = moleculeFlow(ContextClock) { ListPresenter() }
    .stateIn(this, SharingStarted.Lazily, initialState)
}
```
{collapsible="true" collapsed-title="moleculeFlow(ContextClock) { ListPresenter(initialState) }.stateIn(this, SharingStarted.Lazily, initialState)"}
