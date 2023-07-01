package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.testschedule.R
import kotlinx.coroutines.launch

@Composable
fun ViewScheduleScreen() {
    val cnt = LocalContext.current

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true }
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                "253501",
                { showBottomSheet = true },
                { showToast(cnt, "ActionButton", Toast.LENGTH_LONG) })
        },
    ) { pv ->
        Text(
            text = "WellCome",
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
        )
        if (showBottomSheet) {
            BottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { showBottomSheet = false },
                closeBottomSheet = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                },
                onEditButtonClicked = { showToast(cnt, "Edit", Toast.LENGTH_SHORT) },
                selectScheduleClicked = { id -> showToast(cnt, "Selected $id", Toast.LENGTH_SHORT) }
            )
        }
    }
}

@Composable
fun MyTopAppBar(
    titleText: String,
    navIconClicked: () -> Unit,
    actionIconClicked: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                titleText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = navIconClicked) {
                Icon(
                    Icons.Default.Menu,
                    stringResource(id = R.string.open_list_of_saved_schedule_desc)
                )
            }
        },
        actions = {
            Row { // TODO
                IconButton(onClick = actionIconClicked) {
                    Icon(
                        Icons.Default.Favorite,
                        stringResource(id = R.string.open_list_of_saved_schedule_desc)
                    )
                }
            }
        }
    )
}

@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    closeBottomSheet: () -> Unit,
    onEditButtonClicked: () -> Unit,
    selectScheduleClicked: (id: String) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.bottom_sheet_title),
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onEditButtonClicked(); closeBottomSheet(); },
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            stringResource(id = R.string.bottom_sheet_edit_btn_desc)
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(20) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = { selectScheduleClicked(it.toString()) },
                ) {
                    Text("Group $it")
                }
            }
        }
    }
}

private fun showToast(cnt: Context, text: String, length: Int) {
    Toast.makeText(cnt, text, length).show()
}