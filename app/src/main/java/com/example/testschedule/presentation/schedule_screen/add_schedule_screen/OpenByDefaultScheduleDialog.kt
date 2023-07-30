package com.example.testschedule.presentation.schedule_screen.add_schedule_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.testschedule.R
import com.example.testschedule.data.local.entity.ListOfSavedEntity

@Composable
fun OpenByDefaultScheduleDialog(
    closeDialog: () -> Unit,
    clickOk: (item: ListOfSavedEntity?) -> Unit,
    saved: List<ListOfSavedEntity>,
    selected: String
) {
    var selectedId by remember { mutableStateOf(selected) }
        var selectedItem by remember { mutableStateOf<ListOfSavedEntity?>(null) }

    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    clickOk(selectedItem)
                },
                enabled = selectedItem?.id != selected
            ) {
                Text(stringResource(id = R.string.select_default_schedule_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                closeDialog()
            }) {
                Text(stringResource(id = R.string.select_default_schedule_cancel))
            }
        },
        title = {
            Text(stringResource(id = R.string.select_default_schedule_text))
        },
        text = {
            LazyColumn {
                items(saved.size) {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = saved[it].id == selectedId,
                            onClick = {
                                selectedId = saved[it].id
                                selectedItem = saved[it]
                            }
                        )
                        Column(
                            Modifier.weight(1f)
                        ) {
                            Text(
                                text = saved[it].title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    )
}