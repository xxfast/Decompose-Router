package io.github.xxfast.decompose.router.app.screens.stack.list

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import io.github.xxfast.decompose.router.rememberOnRoute

class ListViewModel: ViewModel()

@Composable
fun ListScreen() {
  val viewModel: ListViewModel = rememberOnRoute(type = ListViewModel::class) { ListViewModel() }
}
