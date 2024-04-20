package com.example.testschedule.presentation.account.headman_screen.components.create_omissions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
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
    Column {
        for (i in omissions) {
            LessonCard(
                lesson = i,
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
    var isOpen by remember {
        mutableStateOf(false)
    }

    val selectedStudents = hours[lesson.id]?.size ?: 0
    val selectedHours = hours[lesson.id]?.map { it.value }?.sum() ?: 0

    ElevatedCard(onClick = { isOpen = !isOpen }, modifier = Modifier.padding(8.dp)) {
        ListItem(
            headlineContent = {
                Text(
                    stringResource(
                        id = R.string.account_headman_create_by_student_lesson_name,
                        lesson.nameAbbrev,
                        lesson.lessonTypeAbbrev
                    )
                )
            },
            supportingContent = {
                Text(
                    stringResource(
                        id = R.string.account_headman_create_lesson_time,
                        lesson.lessonPeriod.startTime,
                        lesson.lessonPeriod.endTime,
                        lesson.lessonPeriod.lessonPeriodHours
                    )
                )
            },
            overlineContent = {
                Text(
                    if (selectedStudents == 0)
                        stringArrayResource(id = R.array.subgroups)[lesson.subGroup]
                    else
                        stringResource(
                            id = R.string.account_headman_create_by_lesson_info,
                            selectedStudents,
                            selectedHours
                        )
                )
            }
        )

        if (isOpen) {
            for (i in lesson.students) {
                val isChecked = checkCheck(lesson.id, i.id)

                val selectClicked = {
                    selectOmission(
                        lesson.id,
                        i.id,
                        if (isChecked == ToggleableState.Off) lesson.lessonPeriod.lessonPeriodHours else null
                    )
                }

                ListItem(
                    headlineContent = { Text(i.fio) },
                    modifier = Modifier.clickable { selectClicked() },
                    leadingContent = {
                        TriStateCheckbox(
                            state = isChecked,
                            onClick = { selectClicked() },
                            enabled = i.omission == null
                        )
                    },
                    trailingContent = {
                        AddRemoveHours(
                            hours = if (i.omission != null) i.omission.missedHours else hours[lesson.id]?.get(
                                i.id
                            ) ?: 0,
                            enabled = i.omission == null,
                            maxHours = lesson.lessonPeriod.lessonPeriodHours,
                            isRespect = i.omission?.respectfulOmission ?: false
                        ) { h ->
                            selectOmission(lesson.id, i.id, h)
                        }
                    }
                )
            }
        }
    }
}