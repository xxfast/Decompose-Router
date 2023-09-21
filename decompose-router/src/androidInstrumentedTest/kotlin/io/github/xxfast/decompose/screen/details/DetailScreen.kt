package io.github.xxfast.decompose.screen.details

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xxfast.decompose.screen.BACK_BUTTON_TAG
import io.github.xxfast.decompose.screen.DETAILS_TAG
import io.github.xxfast.decompose.screen.TITLE_BAR_TAG
import io.github.xxfast.decompose.screen.TOOLBAR_TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
  count: Int,
  onBack: () -> Unit,
) {
  var counted: Set<Int> by rememberSaveable { mutableStateOf(emptySet()) }

  if (counted.size == count) onBack()

  Scaffold(
    topBar = {
      LargeTopAppBar(
        modifier = Modifier.testTag(TOOLBAR_TAG),
        title = {
          Text(
            text = count.toString(),
            modifier = Modifier.testTag(TITLE_BAR_TAG)
          )
        },
        navigationIcon = {
          IconButton(
            modifier = Modifier
              .testTag(BACK_BUTTON_TAG),
            onClick = onBack
          ) {
            Icon(Icons.Default.ArrowBack, null)
          }
        })
    }) { paddingValues ->
    LazyHorizontalGrid(
      rows = Adaptive(100.dp),
      modifier = Modifier
        .padding(paddingValues)
        .testTag(DETAILS_TAG),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(16.dp),
    ) {
      items(count) { i ->
        val cornerSize: Dp by animateDpAsState(if (i in counted) 64.dp else 8.dp)

        Card(
          modifier = Modifier
            .width(100.dp)
            .clickable { counted = if (i in counted) counted - i else counted + i }
            .clip(RoundedCornerShape(cornerSize)),
          colors = CardDefaults.cardColors(
            containerColor =
            if (i in counted) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.primaryContainer
          ),
          shape = RoundedCornerShape(cornerSize)
        ) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (i in counted) Icon(imageVector = Icons.Default.Check, null)
            else Text(text = i.toString(), style = MaterialTheme.typography.titleLarge)
          }
        }
      }
    }
  }
}
