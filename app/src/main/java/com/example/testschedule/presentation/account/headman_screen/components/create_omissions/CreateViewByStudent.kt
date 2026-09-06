package com.example.testschedule.presentation.account.headman_screen.components.create_omissions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel
import com.example.testschedule.domain.model.account.headman.create_omissions.StudentLessonOmissionModel

@Composable
fun ViewOmissionsByStudent(
    omissions: List<HeadmanGetOmissionsModel.LessonModel>,
    hours: Map<Int, MutableMap<Int, Int>>,
    selectOmission: (lessonId: Int, userId: Int, hours: Int?) -> Unit,
    checkCheck: (lessonId: Int, userId: Int) -> ToggleableState
) {
    val students = mutableMapOf<String, List<StudentLessonOmissionModel>>()
    val userIds = mutableMapOf<String, Int>()
    omissions.forEach { lesson ->
        lesson.students.forEach { student ->
            lesson.toStudentModel(student.omission).also { studentLesson ->
                students[student.fio] = (students[student.fio].orEmpty() + studentLesson)
                    .sortedBy { it.lessonPeriod.startTime }
                userIds[student.fio] = student.id
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        students.toSortedMap().forEach { (fio, lessons) ->
            StudentCard(
                list = lessons,
                fio = fio,
                id = userIds[fio] ?: 0,
                hours = hours,
                selectOmission = selectOmission,
                checkCheck = checkCheck
            )
        }
    }
}

@Composable
private fun StudentCard(
    list: List<StudentLessonOmissionModel>,
    fio: String,
    id: Int,
    hours: Map<Int, MutableMap<Int, Int>>,
    selectOmission: (lessonId: Int, userId: Int, hours: Int?) -> Unit,
    checkCheck: (lessonId: Int, userId: Int) -> ToggleableState
) {
    var isOpen by remember { mutableStateOf(false) }
    val availableLessons = list.count { it.omission == null }
    val selectedLessons = hours.count { it.value.containsKey(id) }
    val state = when {
        availableLessons == selectedLessons -> ToggleableState.On
        selectedLessons == 0 -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }
    val selectedHours = hours.values.sumOf { it[id] ?: 0 }
    val selectAll = {
        if (state != ToggleableState.On) {
            list.forEach { lesson ->
                if (checkCheck(lesson.id, id) == ToggleableState.Off) {
                    selectOmission(lesson.id, id, lesson.lessonPeriod.lessonPeriodHours)
                }
            }
        } else {
            list.forEach { lesson -> selectOmission(lesson.id, id, null) }
        }
    }
    val subgroups = stringArrayResource(id = R.array.subgroups)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isOpen = !isOpen }
                .padding(end = 8.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TriStateCheckbox(
                state = state,
                onClick = selectAll,
                enabled = availableLessons > 0
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = fio, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                if (selectedHours > 0) {
                    Text(
                        text = stringResource(id = R.string.account_headman_create_by_student_info, selectedHours),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            Icon(
                imageVector = if (isOpen) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnimatedVisibility(visible = isOpen) {
            Column {
                HorizontalDivider()
                list.forEachIndexed { index, lesson ->
                    val checked = checkCheck(lesson.id, id)
                    val selectClicked = {
                        selectOmission(
                            lesson.id,
                            id,
                            if (checked == ToggleableState.Off) {
                                lesson.lessonPeriod.lessonPeriodHours
                            } else {
                                null
                            }
                        )
                    }
                    ListItem(
                        modifier = Modifier.clickable { selectClicked() },
                        headlineContent = {
                            Text(
                                text = stringResource(
                                    id = R.string.account_headman_create_by_student_lesson_name,
                                    lesson.nameAbbrev,
                                    lesson.lessonTypeAbbrev
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        supportingContent = {
                            Text(
                                text = if (lesson.lessonPeriod.startTime.isBlank()) {
                                    stringResource(
                                        id = R.string.account_headman_create_lesson_hours,
                                        lesson.lessonPeriod.lessonPeriodHours
                                    )
                                } else {
                                    stringResource(
                                        id = R.string.account_headman_create_lesson_time,
                                        lesson.lessonPeriod.startTime,
                                        lesson.lessonPeriod.endTime,
                                        lesson.lessonPeriod.lessonPeriodHours
                                    )
                                },
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        overlineContent = {
                            Text(subgroups.getOrElse(lesson.subGroup) { lesson.subGroup.toString() })
                        },
                        leadingContent = {
                            TriStateCheckbox(
                                state = checked,
                                onClick = { selectClicked() },
                                enabled = lesson.omission == null
                            )
                        },
                        trailingContent = {
                            AddRemoveHours(
                                hours = lesson.omission?.missedHours ?: hours[lesson.id]?.get(id) ?: 0,
                                enabled = lesson.omission == null,
                                maxHours = lesson.lessonPeriod.lessonPeriodHours,
                                isRespect = lesson.omission?.respectfulOmission ?: false
                            ) { selected -> selectOmission(lesson.id, id, selected) }
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                    if (index != list.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                    }
                }
            }
        }
    }
}
