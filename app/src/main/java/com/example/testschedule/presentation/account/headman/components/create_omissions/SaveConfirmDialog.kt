package com.example.testschedule.presentation.account.headman.components.create_omissions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel

@Composable
fun SaveConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    lessonList: List<HeadmanGetOmissionsModel.LessonModel>,
    selectedOmissions: Map<Int, MutableMap<Int, Int>>,

    ) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) {
                Text(stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(stringResource(id = R.string.account_headman_create_dialog_title))
        },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                for (lessonId in selectedOmissions) {
                    val lesson = lessonList.first {it.id == lessonId.key}
                    val selectedStudents = selectedOmissions[lesson.id]?.size ?: 0
                    val selectedHours = selectedOmissions[lesson.id]?.map { it.value }?.sum() ?: 0
                    item {
                        ListItem(
                            headlineContent = {
                                Text(
                                    stringResource(
                                        id = R.string.account_headman_create_dialog_lesson_name,
                                        lesson.nameAbbrev,
                                        lesson.lessonTypeAbbrev
                                    )
                                )
                            },
                            overlineContent = {
                                Text(
                                    stringResource(
                                        id = R.string.account_headman_create_dialog_lesson_info,
                                        selectedStudents,
                                        selectedHours
                                    )
                                )
                            },
                            supportingContent = {
                                Text(
                                    stringResource(
                                        id = R.string.account_headman_create_dialog_lesson_time,
                                        lesson.lessonPeriod.startTime,
                                        lesson.lessonPeriod.endTime,
                                        lesson.lessonPeriod.lessonPeriodHours,
                                    )
                                )
                            }
                        )
                    }
                    for (student in selectedOmissions[lesson.id] ?: emptyMap()) {
                        item {
                            Text(
                                stringResource(
                                    id = R.string.account_headman_create_dialog_person,
                                    lesson.students.first { it.id == student.key}.fio,
                                    student.value
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}