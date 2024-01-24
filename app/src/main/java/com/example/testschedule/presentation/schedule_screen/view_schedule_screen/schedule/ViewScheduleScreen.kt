package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.schedule

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.common.Constants
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.BottomSheet
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.ExamsFAB
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.LessonCard
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.MoreDetailCard
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.MyTopAppBar
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.StickySchedule
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.getLessons
import kotlinx.coroutines.launch
import java.lang.Long.max
import java.util.Calendar
import java.util.GregorianCalendar

@Composable
fun ViewScheduleScreen(
    goBackSet: (id: String, title: String) -> Unit,
    scheduleId: String? = null,
    titleLink: String? = null,
    goToAddSchedule: () -> Unit,
    navToExams: (exams: ScheduleModel) -> Unit,
    navToLogin: () -> Unit,
    navToProfile: () -> Unit,
    isPrev: Boolean = false,
    goToPreview: (id: String, title: String) -> Unit,
    viewModel: ViewScheduleViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val sharedPref = cnt.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE)
    val openScheduleId = sharedPref.getString(Constants.PREF_OPEN_BY_DEFAULT_ID, null)
    val openScheduleTitle = sharedPref.getString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, null)
    val scope = rememberCoroutineScope()
    val vm = viewModel.state.value
    val state = rememberLazyListState()

    LaunchedEffect(null) {
        viewModel.getSaved()
        viewModel.getProfile()
        viewModel.title.value = titleLink ?: ""
        Log.i("SCHEDULE_IS_FROM_EXAM", viewModel.isFromExams.toString())
        val x = viewModel.isFromExams
        viewModel.isFromExams = false
        Log.w("SCHEDULE_IS_FROM_EXAM", viewModel.isFromExams.toString())
        if (scheduleId != null) {
            if (!x) {
                viewModel.getSchedule(scheduleId)
            }
        } else {
            if (openScheduleTitle != null && openScheduleId != null) {
                viewModel.getSchedule(openScheduleId)
                viewModel.title.value = openScheduleTitle
            }
        }
    }

    LaunchedEffect(viewModel.title.value) {
        scope.launch { state.animateScrollToItem(0, 0) }
    }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true }
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                titleText = viewModel.title.value,
                navIconClicked = { showBottomSheet = true },
                goToAuth = navToLogin,
                isOfflineResult = vm.isLoading || (vm.error?.isNotEmpty() == true),
                userData = viewModel.userData,
                goToProfile = { viewModel.getProfile(); navToProfile() },
                isPrev
            )
        },
        floatingActionButton = {
            if (vm.schedule?.exams?.isNotEmpty() == true)
                if (((vm.schedule.endExamsDate?.plus(24 * 60 * 60 * 1000 - 1))
                        ?: 0) >= Calendar.getInstance().timeInMillis
                )
                    ExamsFAB(
                        goToExams = {
                            viewModel.isFromExams = true
                            goBackSet(vm.schedule.id, viewModel.title.value)
                            navToExams(vm.schedule)
                        }, state
                    )
        }
    ) { pv ->
        if (scheduleId == null && openScheduleId == null) {
            NoScheduleAdded()
        } else
            if (vm.schedule != null) {
                ShowSchedule(vm.schedule, Modifier.padding(pv), state, isPrev, goToPreview)
            }

        if (vm.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(pv)
                    .fillMaxWidth()
            )
        }


        if (showBottomSheet) {
            BottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { showBottomSheet = false },
                closeBottomSheet = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                },
                onEditButtonClicked = { goToAddSchedule(); viewModel.isFromExams = false },
                selectScheduleClicked = { id, t ->
                    viewModel.getSchedule(id)
                    viewModel.title.value = t
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                    goBackSet(id, t)
                    scope.launch { state.animateScrollToItem(0, 0) }
                },
                saved = viewModel.savedSchedule,
                savedEmployees = viewModel.savedEmployees,
                savedGroups = viewModel.savedGroups
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowSchedule(
    it: ScheduleModel,
    modifier: Modifier,
    state: LazyListState,
    isPrev: Boolean,
    selectScheduleClicked: (id: String, title: String) -> Unit
) {
    val a = LocalContext.current.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE)
    val inst = Calendar.getInstance()
    val openDialog = remember { mutableStateOf(false) }
    var dialogLesson by remember { mutableStateOf<ScheduleModel.WeeksSchedule.Lesson?>(null) }

    val selectedSub = a.getInt(Constants.SELECTED_SUBGROUP, 0)

    val day = GregorianCalendar(
        inst.get(Calendar.YEAR),
        inst.get(Calendar.MONTH),
        inst.get(Calendar.DAY_OF_MONTH),
        0, 0, 0
    )
    val lastUpdate = a.getLong(Constants.LAST_UPDATE_CURRENT_WEEK, 0)
    val week = a.getInt(Constants.CURRENT_WEEK, 0)
    if (it.startLessonsDate != null && it.endLessonsDate != null) {
        if (day.timeInMillis < it.startLessonsDate) day.timeInMillis = it.startLessonsDate
        val endDay = max(it.endLessonsDate, it.endExamsDate ?: 0L)

        Log.e("SCHEDULE_DATE_TODAY", day.timeInMillis.toString())
        Log.e("SCHEDULE_DATE_START", it.startLessonsDate.toString())
        Log.e("SCHEDULE_DATE_END", endDay.toString())

        val lessons = getLessons(
            startDay = day,
            endDay = endDay,
            all = it.schedules,
            lastUpdate = lastUpdate,
            week = week
        )
        if (lessons.isNotEmpty()) {
            LazyColumn(modifier = modifier, state = state) {
                lessons.forEach { lesson ->
                    val filterOfLessons =
                        lesson.lessons.filter { it.numSubgroup == 0 || it.numSubgroup == selectedSub || selectedSub == 0 || isPrev }
                    if (filterOfLessons.isNotEmpty()) {
                        stickyHeader {
                            StickySchedule(lesson = lesson)
                        }
                        filterOfLessons.forEach { les ->
                            item {
                                if (!les.announcement) {
                                    LessonCard(
                                        lesson = les,
                                        isGroup = it.isGroupSchedule,
                                        click = {
                                            openDialog.value = true
                                            dialogLesson = les
                                        }
                                    )
                                } else {
                                    val l = les.copy(
                                        auditories = les.auditories,
                                        startLessonTime = les.startLessonTime,
                                        endLessonTime = les.endLessonTime,
                                        startLessonDate = les.startLessonDate,
                                        endLessonDate = les.endLessonDate,
                                        dateLesson = les.dateLesson,
                                        lessonTypeAbbrev = les.lessonTypeAbbrev,
                                        note = les.note,
                                        numSubgroup = les.numSubgroup,
                                        studentGroups = les.studentGroups,
                                        subject = stringResource(id = R.string.schedule_announcement),
                                        subjectFullName = stringResource(id = R.string.schedule_announcement),
                                        weekNumber = les.weekNumber,
                                        employees = les.employees,
                                        announcement = true,
                                        split = les.split,
                                    )
                                    LessonCard(
                                        lesson = l,
                                        isGroup = it.isGroupSchedule,
                                        click = {
                                            openDialog.value = true
                                            dialogLesson = l
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        } else {
            NoLessonsLeft()
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
}

@Composable
fun NoLessonsLeft() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = R.string.schedule_no_lessons),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoScheduleAdded() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.schedule_no_added_start_screen),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


