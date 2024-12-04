package io.github.xxfast.decompose.router.screens.slot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import io.github.xxfast.decompose.router.screens.BOTTOM_SHEET
import io.github.xxfast.decompose.router.screens.BUTTON_BOTTOM_SHEET
import io.github.xxfast.decompose.router.screens.BUTTON_DIALOG
import io.github.xxfast.decompose.router.screens.DIALOG
import io.github.xxfast.decompose.router.screens.TITLE_BAR
import io.github.xxfast.decompose.router.slot.RoutedContent
import io.github.xxfast.decompose.router.slot.Router
import io.github.xxfast.decompose.router.slot.rememberRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotScreen() {
  val dialogRouter: Router<DialogScreens> = rememberRouter { null }
  val bottomSheetRouter: Router<BottomSheetScreens> = rememberRouter { null }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Slot",
            modifier = Modifier.testTag(TITLE_BAR)
          )
        }
      )
    },
  ) { scaffoldPadding ->
    Column(
      modifier = Modifier
        .padding(scaffoldPadding)
        .fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Button(
        onClick = { dialogRouter.activate(DialogScreens) },
        modifier = Modifier.testTag(BUTTON_DIALOG)
      ) {
        Text("Show Dialog")
      }

      Button(
        onClick = { bottomSheetRouter.activate(BottomSheetScreens()) },
        modifier = Modifier.testTag(BUTTON_BOTTOM_SHEET)
      ) {
        Text("Show Bottom Sheet")
      }
    }
  }

  RoutedContent(dialogRouter) { screens ->
    AlertDialog(
      onDismissRequest = { dialogRouter.dismiss() },
      confirmButton = {
        TextButton(onClick = { dialogRouter.dismiss() }) { Text("Ok") }
      },
      title = { Text("Dialog") },
      text = {
        Text(
          text = screens.toString(),
          modifier = Modifier.padding(8.dp)
        )
      },
      modifier = Modifier.testTag(DIALOG)
    )
  }

  RoutedContent(bottomSheetRouter) { screen ->
    val sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
      sheetState = sheetState,
      onDismissRequest = { bottomSheetRouter.dismiss() },
      modifier = Modifier
        .height(480.dp)
        .testTag(BOTTOM_SHEET)
    ) {
      Scaffold(
        topBar = { TopAppBar(title = { Text("Bottom Sheet") }) },
        bottomBar = {
          BottomAppBar {
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
              onClick = { bottomSheetRouter.dismiss() },
              modifier = Modifier.padding(16.dp)
            ){
              Text("Ok")
            }
          }
        }
      ) { scaffoldPadding ->
        Box(
          modifier = Modifier.fillMaxSize().padding(scaffoldPadding)
        ) {
          Text(
            text = screen.toString(),
            modifier = Modifier
              .padding(16.dp)
              .align(Alignment.Center)
          )
        }
      }
    }
  }
}
