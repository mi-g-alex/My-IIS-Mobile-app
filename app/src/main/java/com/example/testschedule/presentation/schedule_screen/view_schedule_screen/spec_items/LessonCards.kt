@file:OptIn(ExperimentalLayoutApi::class)

package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.testschedule.R
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun StickySchedule(lesson: LessonDay) = Row(
    Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)
        .padding(8.dp),
    Arrangement.SpaceBetween,
    Alignment.CenterVertically
) {
    val date = lesson.day.toString() +
            " " +
            stringArrayResource(id = R.array.months)[lesson.month] +
            ", " +
            stringArrayResource(id = R.array.days_of_week)[lesson.dayOfWeek - 1]
    val curWeek = stringResource(id = R.string.schedule_week, lesson.week)
    Text(
        text = date,
        style = MaterialTheme.typography.headlineMedium
    )
    Text(
        text = curWeek,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun StickySchedule(exam: ExamDay) = Row(
    Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)
        .padding(8.dp),
    Arrangement.SpaceBetween,
    Alignment.CenterVertically
) {
    val date = exam.day.toString() +
            " " +
            stringArrayResource(id = R.array.months)[exam.month]
    Text(
        text = date,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun LessonCard(
    lesson: ScheduleModel.WeeksSchedule.Lesson, isGroup: Boolean,
    click: () -> Unit
) {
    val color = when (lesson.lessonTypeAbbrev) {
        "ЛК" -> colorResource(id = R.color.lecture)
        "УЛк" -> colorResource(id = R.color.lecture)
        "ЛР" -> colorResource(id = R.color.labs)
        "УЛр" -> colorResource(id = R.color.labs)
        "ПЗ" -> colorResource(id = R.color.practice)
        "УПз" -> colorResource(id = R.color.practice)
        "Консультация" -> colorResource(id = R.color.consultation)
        "Экзамен" -> colorResource(id = R.color.exams)
        "Зачёт" -> colorResource(id = R.color.exams)
        "Зачет" -> colorResource(id = R.color.exams)
        else -> colorResource(id = R.color.other)
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                getTimeInString(lesson.startLessonTime),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                getTimeInString(lesson.endLessonTime),
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
        Card(
            onClick = { click() },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Column(Modifier.fillMaxSize()) {
                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        Text(
                            text = (if (lesson.announcement) {
                                stringResource(R.string.schedule_announcement)
                            } else {
                                lesson.subject
                            }) + if (lesson.lessonTypeAbbrev.isNotEmpty()) {
                                " (${lesson.lessonTypeAbbrev})"
                            } else {
                                ""
                            },
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (lesson.auditories.isNotEmpty())
                            Text(
                                text = lesson.auditories[0],
                                style = MaterialTheme.typography.titleMedium
                            )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        var fio = ""
                        if (isGroup) {
                            if (lesson.employees?.isNotEmpty() == true) {
                                lesson.employees.sortedBy { it.lastName }.forEach {
                                    it.getFio()
                                    fio += it.getFio() + ", "
                                }
                                fio = fio.removeSuffix(", ")
                            }
                        } else {
                            if (lesson.studentGroups.isNotEmpty()) {
                                lesson.studentGroups.sortedBy { it.name }.forEach {
                                    fio += "${it.name}, "
                                }
                                fio = fio.removeSuffix(", ")
                            }
                        }
                        Text(
                            text = fio,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (lesson.numSubgroup != 0) {
                            Text(
                                text = stringResource(
                                    id = R.string.schedule_subgroup_text,
                                    lesson.numSubgroup
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    if (lesson.note?.isNotBlank() == true)
                        Text(
                            text = lesson.note,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(color)
            )
        }
    }
}

@Composable
fun GroupItemCard(
    selectScheduleClicked: (id: String, title: String) -> Unit,
    item: ListOfGroupsModel
) {
    Card(
        onClick = {
            selectScheduleClicked(item.name, item.name)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)

                ) {
                    Text(
                        item.course.toString(),
                        Modifier
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Column(Modifier.padding(start = 16.dp)) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        item.facultyAbbrev + " " + item.specialityAbbrev,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun EmployeeItemCard(
    selectScheduleClicked: (id: String, title: String) -> Unit,
    item: ListOfEmployeesModel
) {
    Card(
        onClick = {
            selectScheduleClicked(item.urlId, item.fio)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)

                ) {
                    Icon(
                        Icons.Outlined.Person,
                        null,
                        modifier = Modifier.align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                    if (item.photoLink != null) {
                        SubcomposeAsyncImage(
                            model = item.photoLink,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }
                }
                Column(Modifier.padding(start = 16.dp)) {
                    Text(
                        item.lastName + " " + item.firstName + item.middleName.let { " $it" },
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    var kaf = ""
                    item.academicDepartment.forEach {
                        kaf += "$it, "
                    }
                    kaf = kaf.removeSuffix(", ")
                    if (kaf.isNotEmpty()) {
                        Text(
                            kaf,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoreDetailCard(
    lesson: ScheduleModel.WeeksSchedule.Lesson,
    onDismissRequest: () -> Unit,
    selectScheduleClicked: (id: String, title: String) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (lesson.announcement) {
                    stringResource(R.string.schedule_announcement)
                } else {
                    lesson.subjectFullName.ifBlank { lesson.subject }
                },
                style = MaterialTheme.typography.headlineSmall
            )
            if (lesson.lessonTypeAbbrev.isNotBlank()) {
                Text(
                    text = lesson.lessonTypeAbbrev,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            androidx.compose.material3.HorizontalDivider(Modifier.padding(vertical = 8.dp))

            LessonDetailRow(
                label = stringResource(R.string.schedule_ui_lesson_time),
                value = "${getTimeInString(lesson.startLessonTime)} - ${getTimeInString(lesson.endLessonTime)}"
            )
            if (lesson.auditories.isNotEmpty()) {
                LessonDetailRow(
                    label = stringResource(R.string.schedule_ui_auditoriums),
                    value = lesson.auditories.joinToString(", ")
                )
            }

            val firstDate = lesson.startLessonDate ?: lesson.dateLesson
            val lastDate = lesson.endLessonDate ?: lesson.dateLesson
            if (firstDate != null) {
                val formatter = SimpleDateFormat(
                    stringResource(R.string.schedule_dialog_date_pattern),
                    Locale.getDefault()
                )
                val value = if (firstDate == lastDate || lastDate == null) {
                    formatter.format(Date(firstDate))
                } else {
                    "${formatter.format(Date(firstDate))} - ${formatter.format(Date(lastDate))}"
                }
                LessonDetailRow(stringResource(R.string.schedule_ui_dates), value)
            }
            if (lesson.weekNumber.isNotEmpty()) {
                LessonDetailRow(
                    stringResource(R.string.schedule_ui_weeks),
                    lesson.weekNumber.joinToString(", ")
                )
            }
            if (lesson.numSubgroup != 0) {
                LessonDetailRow(
                    stringResource(R.string.schedule_ui_subgroup),
                    lesson.numSubgroup.toString()
                )
            }
            if (!lesson.note.isNullOrBlank()) {
                LessonDetailRow(stringResource(R.string.schedule_ui_note), lesson.note)
            }

            if (!lesson.employees.isNullOrEmpty()) {
                Text(
                    stringResource(R.string.schedule_add_employees_title),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    lesson.employees.sortedBy { it.lastName }.forEach { employee ->
                        val fullName = listOf(
                            employee.lastName,
                            employee.firstName,
                            employee.middleName.orEmpty()
                        ).filter { it.isNotBlank() }.joinToString(" ")
                        AssistChip(
                            onClick = {
                                selectScheduleClicked(employee.urlId, fullName)
                                onDismissRequest()
                            },
                            label = { Text(fullName) }
                        )
                    }
                }
            }
            if (lesson.studentGroups.isNotEmpty()) {
                Text(
                    stringResource(R.string.schedule_add_groups_title),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    lesson.studentGroups.sortedBy { it.name }.forEach { group ->
                        AssistChip(
                            onClick = {
                                selectScheduleClicked(group.name, group.name)
                                onDismissRequest()
                            },
                            label = { Text(group.name) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.35f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.65f)
        )
    }
}
