package com.example.testschedule.presentation.account.headman.components.create_omissions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
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
    val listOfStudents: MutableMap<String, List<StudentLessonOmissionModel>> = mutableMapOf()
    val userIdMap = mutableMapOf<String, Int>()
    for (i in omissions) {
        for (j in i.students)
            i.toStudentModel(j.omission).also {
                listOfStudents[j.fio] = ((listOfStudents[j.fio]
                    ?: emptyList()) + listOf(it)).sortedBy { itt -> itt.lessonPeriod.startTime }
                userIdMap[j.fio] = j.id
            }
    }

    Column {
        for ((i, j) in listOfStudents) {
            StudentCard(
                list = j,
                fio = i,
                id = userIdMap[i] ?: 0,
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

    ElevatedCard(onClick = { }, modifier = Modifier.padding(8.dp)) {

        val numberOfAvailable = list.count { it.omission == null }
        val st =
            if (numberOfAvailable == hours.count { it.value.containsKey(id) }) ToggleableState.On
            else if (hours.count { it.value.containsKey(id) } == 0) ToggleableState.Off
            else ToggleableState.Indeterminate

        val selectedHours =
            hours.map { if (it.value.containsKey(id)) it.value[id] else 0 }.sumOf { it ?: 0 }

        val onClick: () -> Unit = {
            if (st != ToggleableState.On)
                for (i in list) {
                    if (checkCheck(i.id, id) == ToggleableState.Off) {
                        selectOmission(i.id, id, i.lessonPeriod.lessonPeriodHours)
                    }
                }
            else {
                for (i in list) {
                    selectOmission(i.id, id, null)
                }
            }
        }

        ListItem(
            headlineContent = { Text(fio) },
            supportingContent = {
                Text(
                    stringResource(
                        id = R.string.account_headman_create_by_student_info,
                        selectedHours
                    )
                )
            },
            leadingContent = {
                TriStateCheckbox(
                    state = st,
                    onClick = { onClick() },
                    enabled = numberOfAvailable > 0
                )
            }
        )

        for (lesson in list) {
            val isChecked = checkCheck(lesson.id, id)

            val selectClicked = {
                selectOmission(
                    lesson.id,
                    id,
                    if (isChecked == ToggleableState.Off) lesson.lessonPeriod.lessonPeriodHours else null
                )
            }

            ListItem(
                modifier = Modifier.clickable { selectClicked() },
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
                overlineContent = { Text(stringArrayResource(id = R.array.subgroups)[lesson.subGroup]) },
                leadingContent = {
                    TriStateCheckbox(
                        state = isChecked,
                        onClick = { selectClicked() },
                        enabled = lesson.omission == null
                    )
                },
                trailingContent = {
                    AddRemoveHours(
                        hours = if (lesson.omission != null) lesson.omission.missedHours else hours[lesson.id]?.get(
                            id
                        ) ?: 0,
                        enabled = lesson.omission == null,
                        maxHours = lesson.lessonPeriod.lessonPeriodHours
                    ) { h ->
                        selectOmission(lesson.id, id, h)
                    }
                }
            )
        }

    }
}