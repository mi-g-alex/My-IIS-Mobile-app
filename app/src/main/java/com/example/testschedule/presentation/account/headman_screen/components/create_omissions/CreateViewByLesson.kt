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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel

@Composable
fun ViewOmissionsByLesson(
    omissions: List<HeadmanGetOmissionsModel.LessonModel>,
    hours: Map<Int, MutableMap<Int, Int>>,
    selectOmission: (lessonId: Int, userId: Int, hours: Int?) -> Unit,
    checkCheck: (lessonId: Int, userId: Int) -> ToggleableState
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        omissions.forEach { lesson ->
            LessonCard(
                lesson = lesson,
                selectOmission = selectOmission,
                checkCheck = checkCheck,
                hours = hours
            )
        }
    }
}

@Composable
private fun LessonCard(
    lesson: HeadmanGetOmissionsModel.LessonModel,
    hours: Map<Int, MutableMap<Int, Int>>,
    selectOmission: (lessonId: Int, userId: Int, hours: Int?) -> Unit,
    checkCheck: (lessonId: Int, userId: Int) -> ToggleableState
) {
    var isOpen by remember { mutableStateOf(false) }
    var showScheduleInfo by remember { mutableStateOf(false) }
    val selectedStudents = hours[lesson.id]?.size ?: 0
    val selectedHours = hours[lesson.id]?.values?.sum() ?: 0
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(
                        id = R.string.account_headman_create_by_student_lesson_name,
                        lesson.nameAbbrev,
                        lesson.lessonTypeAbbrev
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showScheduleInfo = true }
                )
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
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (selectedStudents == 0) {
                        subgroups.getOrElse(lesson.subGroup) { lesson.subGroup.toString() }
                    } else {
                        stringResource(
                            id = R.string.account_headman_create_by_lesson_info,
                            selectedStudents,
                            selectedHours
                        )
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selectedStudents == 0) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
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
                lesson.students.forEachIndexed { index, student ->
                    val checked = checkCheck(lesson.id, student.id)
                    val selectClicked = {
                        selectOmission(
                            lesson.id,
                            student.id,
                            if (checked == ToggleableState.Off) {
                                lesson.lessonPeriod.lessonPeriodHours
                            } else {
                                null
                            }
                        )
                    }
                    ListItem(
                        headlineContent = {
                            Text(text = student.fio, style = MaterialTheme.typography.bodyMedium)
                        },
                        modifier = Modifier.clickable { selectClicked() },
                        leadingContent = {
                            TriStateCheckbox(
                                state = checked,
                                onClick = { selectClicked() },
                                enabled = student.omission == null
                            )
                        },
                        trailingContent = {
                            AddRemoveHours(
                                hours = student.omission?.missedHours
                                    ?: hours[lesson.id]?.get(student.id)
                                    ?: 0,
                                enabled = student.omission == null,
                                maxHours = lesson.lessonPeriod.lessonPeriodHours,
                                isRespect = student.omission?.respectfulOmission ?: false
                            ) { selected ->
                                selectOmission(lesson.id, student.id, selected)
                            }
                        },
                        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
                    )
                    if (index != lesson.students.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                    }
                }
            }
        }
    }

    if (showScheduleInfo) {
        LessonScheduleInfoSheet(
            nameAbbrev = lesson.nameAbbrev,
            lessonTypeAbbrev = lesson.lessonTypeAbbrev,
            subGroup = lesson.subGroup,
            lessonPeriod = lesson.lessonPeriod,
            scheduleInfo = lesson.scheduleInfo,
            onDismiss = { showScheduleInfo = false }
        )
    }
}
