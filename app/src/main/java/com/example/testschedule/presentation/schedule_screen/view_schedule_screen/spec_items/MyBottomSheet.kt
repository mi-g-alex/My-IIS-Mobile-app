package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.testschedule.R
import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel

@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    closeBottomSheet: () -> Unit,
    onEditButtonClicked: () -> Unit,
    selectScheduleClicked: (id: String, title: String) -> Unit,
    saved: MutableState<List<ListOfSavedEntity>?>,
    savedGroups: MutableState<List<ListOfGroupsModel>>,
    savedEmployees: MutableState<List<ListOfEmployeesModel>>
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.schedule_bottom_sheet_title),
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
                            stringResource(id = R.string.schedule_bottom_sheet_edit_btn_desc)
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
            saved.value?.forEach {
                if (it.isGroup) {
                    if (savedGroups.value.any { item -> item.name == it.id }) {
                        savedGroups.value.find { item -> item.name == it.id }?.let { it1 ->
                            item {
                                GroupItemCard(
                                    selectScheduleClicked,
                                    item = it1
                                )
                            }
                        }
                    } else {
                        item {
                            GroupItemCard(
                                selectScheduleClicked,
                                item = ListOfGroupsModel(
                                    0,
                                    stringResource(id = R.string.schedule_removed_from_server_schedules_list),
                                    it.title,
                                    "",
                                    ""
                                )
                            )
                        }
                    }
                } else {
                    if (savedEmployees.value.any { item -> item.urlId == it.id }) {
                        savedEmployees.value.find { item -> item.urlId == it.id }?.let { it1 ->
                            item {
                                EmployeeItemCard(
                                    selectScheduleClicked = selectScheduleClicked,
                                    item = it1
                                )
                            }
                        }
                    } else {
                        item {
                            EmployeeItemCard(
                                selectScheduleClicked = selectScheduleClicked,
                                item = ListOfEmployeesModel(
                                    listOf(stringResource(id = R.string.schedule_removed_from_server_schedules_list)),
                                    it.title,
                                    it.title,
                                    "",
                                    null,
                                    null,
                                    null,
                                    it.id
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
