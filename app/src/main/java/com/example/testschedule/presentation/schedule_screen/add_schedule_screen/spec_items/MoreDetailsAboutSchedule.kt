package com.example.testschedule.presentation.schedule_screen.add_schedule_screen.spec_items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testschedule.R
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel

@Composable
fun MoreDetailsAboutSchedule(
    closeDialog: () -> Unit,
    isGroup: Boolean,
    dataOfGroup: ListOfGroupsModel?,
    dataOfEmployee: ListOfEmployeesModel?
) {
    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        confirmButton = {
        },
        title = {
            Text(
                if (isGroup) stringResource(
                    id = R.string.schedule_add_info_dialog_group_title,
                    dataOfGroup?.name ?: ""
                ) else stringResource(
                    id = R.string.schedule_add_info_dialog_employee_title
                )
            )
        },
        text = {
            if (isGroup)
                dataOfGroup?.let { MoreAboutGroupDialog(it) }
            else
                dataOfEmployee?.let { MoreAboutScheduleDialog(it) }
        }
    )
}

@Composable
fun MoreAboutGroupDialog(dataOfGroup: ListOfGroupsModel) {
    LazyColumn {
        item {
            Text(
                stringResource(
                    id = R.string.schedule_add_info_dialog_group_faculty,
                    dataOfGroup.facultyAbbrev
                )
            )
        }
        item {
            Text(
                stringResource(
                    id = R.string.schedule_add_info_dialog_group_full_name,
                    dataOfGroup.specialityName
                )
            )
        }
        item {
            Text(
                stringResource(
                    id = R.string.schedule_add_info_dialog_group_short_name,
                    dataOfGroup.specialityAbbrev
                )
            )
        }
        item {
            Text(
                stringResource(
                    id = R.string.schedule_add_info_dialog_group_course,
                    dataOfGroup.course
                )
            )
        }
    }
}

@Composable
fun MoreAboutScheduleDialog(dataOfEmployee: ListOfEmployeesModel) {
    LazyColumn(Modifier.fillMaxWidth()) {
        item {
            Box(Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(dataOfEmployee.photoLink)
                        .crossfade(true).build(),
                    contentDescription = dataOfEmployee.fio,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            Text(
            stringResource(
                id = R.string.schedule_add_info_dialog_employee_fio,
                dataOfEmployee.lastName, dataOfEmployee.firstName, (dataOfEmployee.middleName ?: "")
            ))
        }

        if (dataOfEmployee.degree?.isNotBlank() == true)
        item {
            Text(stringResource(
                id = R.string.schedule_add_info_dialog_employee_degree,
                dataOfEmployee.degree
            ))
        }

        if (dataOfEmployee.rank?.isNotBlank() == true)
        item {
            Text(stringResource(
                id = R.string.schedule_add_info_dialog_employee_rank,
                dataOfEmployee.rank
            ))
        }

        if (dataOfEmployee.academicDepartment.isNotEmpty())
        item {
            var dep = ""
            dataOfEmployee.academicDepartment.sorted().forEach {
                dep += "$it, "
            }
            dep = dep.removeSuffix(", ")
            Text(stringResource(
                id = R.string.schedule_add_info_dialog_employee_departments,
                dep
            ))
        }
    }
}