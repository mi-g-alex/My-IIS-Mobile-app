package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.LessonCard
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.MoreDetailCard
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.StickySchedule

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewExamsScreen(
    navBack: () -> Unit,
    selectScheduleClicked: (id: String, title: String) -> Unit,
    viewModel: ViewExamsViewModel = hiltViewModel()
) {
    val openDialog = remember { mutableStateOf(false) }
    var dialogLesson by remember { mutableStateOf<ScheduleModel.WeeksSchedule.Lesson?>(null) }

    LaunchedEffect(viewModel.schedule.value) {
        if (viewModel.schedule.value != null) {
            viewModel.getExams()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.schedule_exams_fab))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navBack()
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, stringResource(id = R.string.back))
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        if (viewModel.schedule.value != null)
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                viewModel.exams.value.forEachIndexed() { i, _ ->
                    val exams = viewModel.exams.value
                    stickyHeader {
                        if (i == 0 || (exams[i - 1].date != exams[i].date)) {
                            StickySchedule(exams[i])
                        }
                    }
                    item {
                        if (!exams[i].exam.announcement) {
                            LessonCard(
                                lesson = exams[i].exam,
                                isGroup = viewModel.schedule.value!!.isGroupSchedule
                            ) {
                                openDialog.value = true
                                dialogLesson = exams[i].exam
                            }
                        } else {
                            val les = exams[i].exam.copy(
                                auditories = exams[i].exam.auditories,
                                startLessonTime = exams[i].exam.startLessonTime,
                                endLessonTime = exams[i].exam.endLessonTime,
                                startLessonDate = exams[i].exam.startLessonDate,
                                endLessonDate = exams[i].exam.endLessonDate,
                                dateLesson = exams[i].exam.dateLesson,
                                lessonTypeAbbrev = exams[i].exam.lessonTypeAbbrev,
                                note = exams[i].exam.note,
                                numSubgroup = exams[i].exam.numSubgroup,
                                studentGroups = exams[i].exam.studentGroups,
                                subject = stringResource(id = R.string.schedule_announcement),
                                subjectFullName = stringResource(id = R.string.schedule_announcement),
                                weekNumber = exams[i].exam.weekNumber,
                                employees = exams[i].exam.employees,
                                announcement = exams[i].exam.announcement,
                                split = exams[i].exam.split,
                            )
                            LessonCard(
                                lesson = les,
                                isGroup = viewModel.schedule.value!!.isGroupSchedule,
                                click = {
                                    openDialog.value = true
                                    dialogLesson = les
                                }
                            )
                        }
                    }
                }
            }
    }

    if (openDialog.value && dialogLesson != null) {
        MoreDetailCard(
            lesson = dialogLesson!!,
            {
                openDialog.value = false
            },
            selectScheduleClicked
        )
    }
}
