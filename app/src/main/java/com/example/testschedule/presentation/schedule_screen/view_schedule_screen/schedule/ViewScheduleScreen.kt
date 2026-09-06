package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.schedule

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.testschedule.R
import com.example.testschedule.common.CacheUpdateKeys
import com.example.testschedule.common.Constants
import com.example.testschedule.domain.model.auth.UserBasicDataModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.presentation.LoadingTopBar
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.LessonCard
import com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items.MoreDetailCard
import com.example.testschedule.presentation.LastUpdateText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Suppress("UNUSED_PARAMETER")
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
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE)
    }
    val openScheduleId = preferences.getString(Constants.PREF_OPEN_BY_DEFAULT_ID, null)
    val openScheduleTitle = preferences.getString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, null)
    var selectedScheduleId by rememberSaveable {
        mutableStateOf(preferences.getString(Constants.PREF_PENDING_SCHEDULE_ID, null))
    }
    var selectedScheduleTitle by rememberSaveable {
        mutableStateOf(preferences.getString(Constants.PREF_PENDING_SCHEDULE_TITLE, null))
    }
    var showAsPreview by rememberSaveable(isPrev) { mutableStateOf(isPrev) }
    val state = viewModel.state.value
    val schedule = state.schedule
    val listState = rememberLazyListState()

    var viewMode by rememberSaveable { mutableStateOf(ScheduleViewMode.BY_DAYS) }
    var subgroup by rememberSaveable {
        mutableStateOf(
            when (preferences.getInt(Constants.SELECTED_SUBGROUP, 0)) {
                1 -> SubgroupFilter.FIRST
                2 -> SubgroupFilter.SECOND
                else -> SubgroupFilter.ALL
            }
        )
    }
    var selectedLesson by remember {
        mutableStateOf<ScheduleModel.WeeksSchedule.Lesson?>(null)
    }
    var showOwnerSheet by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(preferences) {
        fun consumeSelection(sharedPreferences: SharedPreferences) {
            sharedPreferences.getString(Constants.PREF_PENDING_SCHEDULE_ID, null)
                ?.let { pendingId ->
                    selectedScheduleId = pendingId
                    selectedScheduleTitle = sharedPreferences.getString(
                        Constants.PREF_PENDING_SCHEDULE_TITLE,
                        null
                    )
                    showAsPreview = false
                }
        }
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == Constants.PREF_PENDING_SCHEDULE_ID) {
                consumeSelection(sharedPreferences)
            }
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        consumeSelection(preferences)
        onDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    val requestedScheduleId = selectedScheduleId ?: scheduleId ?: openScheduleId
    val requestedScheduleTitle = selectedScheduleTitle
        ?: if (scheduleId == null) openScheduleTitle else titleLink

    LaunchedEffect(requestedScheduleId, requestedScheduleTitle) {
        viewModel.getSaved()
        viewModel.getProfile()
        viewModel.title.value = requestedScheduleTitle.orEmpty()
        if (!requestedScheduleId.isNullOrBlank()) {
            viewModel.getSchedule(requestedScheduleId)
        }
        if (selectedScheduleId != null) {
            preferences.edit {
                remove(Constants.PREF_PENDING_SCHEDULE_ID)
                remove(Constants.PREF_PENDING_SCHEDULE_TITLE)
            }
        }
    }

    LaunchedEffect(viewMode, schedule?.id) {
        listState.scrollToItem(0)
    }

    LaunchedEffect(schedule?.id, schedule?.exams?.isNotEmpty()) {
        if (schedule != null && viewMode == ScheduleViewMode.EXAMS && schedule.exams.isNullOrEmpty()) {
            viewMode = ScheduleViewMode.BY_DAYS
        }
    }

    val currentWeek = viewModel.currentWeek.takeIf { it in 1..4 } ?: 1
    val lastWeekUpdate = viewModel.lastWeekUpdate
        .takeIf { it > 0L }
        ?: currentMondayMillis()
    val displayedCurrentWeek = currentAcademicWeek(currentWeek, lastWeekUpdate)
    val effectiveSubgroup = subgroup
    val days = schedule?.let {
        when (viewMode) {
            ScheduleViewMode.BY_DAYS -> ScheduleUiBuilder.buildByDays(
                schedule = it,
                currentWeek = currentWeek,
                lastWeekUpdate = lastWeekUpdate,
                subgroup = effectiveSubgroup
            )

            ScheduleViewMode.BY_WEEKS -> ScheduleUiBuilder.buildByWeeks(
                schedule = it,
                subgroup = effectiveSubgroup,
                currentWeek = currentWeek,
                lastWeekUpdate = lastWeekUpdate
            )
            ScheduleViewMode.EXAMS -> ScheduleUiBuilder.buildExams(it, effectiveSubgroup)
        }
    }.orEmpty()
    val isFavorite = schedule?.let { current ->
        viewModel.savedSchedule.value.orEmpty().any { it.id == current.id }
    } == true
    val cacheKey = (schedule?.id ?: requestedScheduleId)
        ?.takeIf { it.isNotBlank() }
        ?.let(CacheUpdateKeys::schedule)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LoadingTopBar(isLoading = state.isLoading) {
                ScheduleTopBar(
                    title = viewModel.title.value,
                    viewMode = viewMode,
                    subgroup = subgroup,
                    hasExams = schedule?.exams?.isNotEmpty() == true,
                    isFavorite = isFavorite,
                    isPreview = showAsPreview,
                    userData = viewModel.userData,
                    onMenuClick = goToAddSchedule,
                    onTitleClick = { showOwnerSheet = schedule != null },
                    onViewModeSelected = { viewMode = it },
                    onSubgroupSelected = {
                        subgroup = it
                        preferences.edit { putInt(Constants.SELECTED_SUBGROUP, it.number) }
                    },
                    onFavoriteToggle = {
                        schedule?.let { current ->
                            val currentDefault = preferences.getString(
                                Constants.PREF_OPEN_BY_DEFAULT_ID,
                                null
                            )
                            if (isFavorite && currentDefault == current.id) {
                                val replacement = viewModel.savedSchedule.value.orEmpty()
                                    .firstOrNull { it.id != current.id }
                                preferences.edit {
                                    putString(Constants.PREF_OPEN_BY_DEFAULT_ID, replacement?.id)
                                    putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, replacement?.title)
                                }
                            } else if (!isFavorite && currentDefault.isNullOrBlank()) {
                                preferences.edit {
                                    putString(Constants.PREF_OPEN_BY_DEFAULT_ID, current.id)
                                    putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, current.title)
                                }
                            }
                            viewModel.toggleFavorite(current)
                        }
                    },
                    onLogin = navToLogin,
                    onProfile = {
                        viewModel.getProfile()
                        navToProfile()
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                LastUpdateText(
                    cacheKey = cacheKey,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                when {
                    scheduleId == null && openScheduleId.isNullOrBlank() -> EmptySchedule(
                        onOpen = goToAddSchedule,
                        modifier = Modifier.fillMaxSize()
                    )

                    schedule == null && !state.error.isNullOrBlank() -> ScheduleError(
                        onRetry = {
                            (scheduleId ?: openScheduleId)?.let(viewModel::getSchedule)
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    schedule != null && days.isEmpty() -> NoLessonsLeft(Modifier.fillMaxSize())

                    schedule != null -> ScheduleContent(
                        days = days,
                        currentWeek = displayedCurrentWeek,
                        isGroupSchedule = schedule.isGroupSchedule,
                        listState = listState,
                        onLessonClick = { selectedLesson = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    selectedLesson?.let { lesson ->
        MoreDetailCard(
            lesson = lesson,
            onDismissRequest = { selectedLesson = null },
            selectScheduleClicked = { id, title ->
                selectedLesson = null
                goBackSet(schedule?.id.orEmpty(), schedule?.title.orEmpty())
                goToPreview(id, title)
            }
        )
    }

    if (showOwnerSheet && schedule != null) {
        ScheduleOwnerSheet(schedule = schedule, onDismiss = { showOwnerSheet = false })
    }
}

@Composable
private fun ScheduleTopBar(
    title: String,
    viewMode: ScheduleViewMode,
    subgroup: SubgroupFilter,
    hasExams: Boolean,
    isFavorite: Boolean,
    isPreview: Boolean,
    userData: MutableState<UserBasicDataModel?>,
    onMenuClick: () -> Unit,
    onTitleClick: () -> Unit,
    onViewModeSelected: (ScheduleViewMode) -> Unit,
    onSubgroupSelected: (SubgroupFilter) -> Unit,
    onFavoriteToggle: () -> Unit,
    onLogin: () -> Unit,
    onProfile: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable(enabled = title.isNotEmpty(), onClick = onTitleClick)
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Outlined.Menu,
                    contentDescription = stringResource(R.string.schedule_open_list_of_saved_desc)
                )
            }
        },
        actions = {
            if (isPreview && title.isNotEmpty()) {
                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = if (isFavorite) {
                            stringResource(R.string.schedule_remove_desc)
                        } else {
                            stringResource(R.string.schedule_save_desc)
                        },
                        tint = if (isFavorite) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
            ViewModeDropdown(viewMode, hasExams, onViewModeSelected)
            SubgroupDropdown(subgroup, onSubgroupSelected)
            IconButton(onClick = { if (userData.value == null) onLogin() else onProfile() }) {
                var showIcon by remember { mutableStateOf(true) }
                if (showIcon) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(R.string.login_to_account_button)
                    )
                }
                SubcomposeAsyncImage(
                    model = userData.value?.photoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp),
                    onSuccess = { showIcon = false }
                )
            }
        }
    )
}

@Composable
private fun ViewModeDropdown(
    current: ScheduleViewMode,
    hasExams: Boolean,
    onSelected: (ScheduleViewMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.AutoMirrored.Filled.List,
                contentDescription = stringResource(R.string.schedule_ui_view_mode)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ScheduleViewMode.entries
                .filter { it != ScheduleViewMode.EXAMS || hasExams }
                .forEach { mode ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = mode.label(),
                                fontWeight = if (mode == current) FontWeight.Bold else null
                            )
                        },
                        onClick = {
                            onSelected(mode)
                            expanded = false
                        },
                        trailingIcon = {
                            if (mode == current) {
                                Icon(Icons.Filled.Check, contentDescription = null)
                            }
                        }
                    )
                }
        }
    }
}

@Composable
private fun SubgroupDropdown(
    current: SubgroupFilter,
    onSelected: (SubgroupFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Text(
                current.label(),
                color = if (current == SubgroupFilter.ALL) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            SubgroupFilter.entries.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Text(
                            filter.label(),
                            fontWeight = if (filter == current) FontWeight.Bold else null
                        )
                    },
                    onClick = {
                        onSelected(filter)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ScheduleViewMode.label(): String = when (this) {
    ScheduleViewMode.BY_DAYS -> stringResource(R.string.schedule_ui_by_days)
    ScheduleViewMode.BY_WEEKS -> stringResource(R.string.schedule_ui_by_weeks)
    ScheduleViewMode.EXAMS -> stringResource(R.string.schedule_ui_exams)
}

@Composable
private fun SubgroupFilter.label(): String = when (this) {
    SubgroupFilter.ALL -> stringResource(R.string.schedule_ui_subgroup_all)
    SubgroupFilter.FIRST -> stringResource(R.string.schedule_ui_subgroup_first)
    SubgroupFilter.SECOND -> stringResource(R.string.schedule_ui_subgroup_second)
}

@Composable
private fun ScheduleContent(
    days: List<ScheduleDayDisplay>,
    currentWeek: Int,
    isGroupSchedule: Boolean,
    listState: LazyListState,
    onLessonClick: (ScheduleModel.WeeksSchedule.Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(state = listState, modifier = modifier.fillMaxSize()) {
        item {
            Text(
                text = stringResource(R.string.schedule_ui_current_week, currentWeek),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            )
        }
        items(days) { day ->
            ScheduleDaySection(
                day = day,
                isGroupSchedule = isGroupSchedule,
                onLessonClick = onLessonClick
            )
            HorizontalDivider()
        }
        item { Spacer(Modifier.height(32.dp)) }
    }
}

@Composable
private fun ScheduleDaySection(
    day: ScheduleDayDisplay,
    isGroupSchedule: Boolean,
    onLessonClick: (ScheduleModel.WeeksSchedule.Lesson) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = day.header(), fontWeight = FontWeight.Medium)
        if (day.isRestDay) {
            Text(
                stringResource(R.string.schedule_ui_rest_day),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            day.lessons.forEach { lesson ->
                LessonCard(
                    lesson = lesson,
                    isGroup = isGroupSchedule,
                    click = { onLessonClick(lesson) }
                )
            }
        }
    }
}

@Composable
private fun ScheduleDayDisplay.header(): String {
    val days = stringArrayResource(R.array.days_of_week)
    val weekSuffix = week?.let { " · ${stringResource(R.string.schedule_week, it)}" }.orEmpty()
    if (date == null) return days[dayOfWeek - 1] + weekSuffix

    val calendar = Calendar.getInstance().apply { timeInMillis = date }
    val months = stringArrayResource(R.array.months)
    return "${calendar.get(Calendar.DAY_OF_MONTH)} ${months[calendar.get(Calendar.MONTH)]}, " +
        days[dayOfWeek - 1] + weekSuffix
}

@Composable
private fun ScheduleOwnerSheet(schedule: ScheduleModel, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            schedule.employeeInfo?.photoLink?.let { photo ->
                SubcomposeAsyncImage(
                    model = photo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.height(4.dp))
            }
            val employee = schedule.employeeInfo
            val group = schedule.studentGroupInfo
            Text(
                text = if (employee != null) {
                    listOfNotNull(employee.lastName, employee.firstName, employee.middleName)
                        .joinToString(" ")
                } else {
                    group?.name ?: schedule.title
                },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
            val subtitle = employee?.rank ?: group?.course?.let {
                stringResource(R.string.schedule_ui_course, it)
            }
            if (!subtitle.isNullOrBlank()) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            employee?.degree?.takeIf(String::isNotBlank)?.let {
                OwnerDetailRow(stringResource(R.string.schedule_ui_degree), it)
            }
            group?.facultyAbbrev?.takeIf(String::isNotBlank)?.let {
                OwnerDetailRow(stringResource(R.string.schedule_ui_faculty), it)
            }
            group?.specialityName?.takeIf(String::isNotBlank)?.let {
                OwnerDetailRow(stringResource(R.string.schedule_ui_speciality), it)
            }
            formatRange(schedule.startLessonsDate, schedule.endLessonsDate)?.let {
                OwnerDetailRow(stringResource(R.string.schedule_ui_lessons_range), it)
            }
            formatRange(schedule.startExamsDate, schedule.endExamsDate)?.let {
                OwnerDetailRow(stringResource(R.string.schedule_ui_exams_range), it)
            }
        }
    }
}

@Composable
private fun OwnerDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.35f)
        )
        Text(value, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.65f))
    }
}

@Composable
private fun EmptySchedule(onOpen: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.schedule_ui_select_schedule),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onOpen) { Text(stringResource(R.string.schedule_ui_open)) }
    }
}

@Composable
private fun ScheduleError(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.schedule_ui_load_error),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onRetry) { Text(stringResource(R.string.schedule_ui_retry)) }
    }
}

@Composable
fun NoLessonsLeft(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.schedule_no_lessons),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoScheduleAdded() {
    EmptySchedule(onOpen = {})
}

private fun currentMondayMillis(): Long {
    val calendar = Calendar.getInstance()
    val daysFromMonday = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> 6
        else -> calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY
    }
    calendar.add(Calendar.DAY_OF_MONTH, -daysFromMonday)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

private fun currentAcademicWeek(referenceWeek: Int, referenceMonday: Long): Int {
    val weekOffset = ((currentMondayMillis() - referenceMonday) / (7 * 24 * 60 * 60 * 1000L)).toInt()
    return Math.floorMod(referenceWeek - 1 + weekOffset, 4) + 1
}

private fun formatRange(start: Long?, end: Long?): String? {
    if (start == null || end == null) return null
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return "${formatter.format(Date(start))} - ${formatter.format(Date(end))}"
}
