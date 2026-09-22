package com.example.testschedule.presentation.account.rating_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.common.CacheUpdateKeys
import com.example.testschedule.domain.model.account.rating.RatingModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.LastUpdateListItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun RatingScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: RatingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val loginError = stringResource(id = R.string.error_to_login)
    var backEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel.errorText.value) {
        if (viewModel.errorText.value == "WrongPassword") {
            Toast.makeText(context, loginError, Toast.LENGTH_LONG).show()
            onLogOut()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopBar(
                onBackPressed = {
                    onBackPressed()
                    backEnabled = false
                },
                title = stringResource(id = R.string.account_rating_title),
                enabled = backEnabled,
                isLoading = viewModel.isLoading.value
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            viewModel.rating.value?.let { RatingContent(it) }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RatingContent(data: RatingModel) {
    val tabs = remember(data.points) {
        buildList {
            if (data.points.containsKey("all_points")) add("all_points")
            addAll(data.points.keys.filter { it != "all_points" }.sorted())
        }
    }
    if (tabs.isEmpty()) {
        Text(
            text = stringResource(id = R.string.account_rating_title),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()
    var selectedSubject by remember { mutableStateOf<RatingModel.Point.LessonByName?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = pagerState.currentPage, edgePadding = 0.dp) {
            tabs.forEachIndexed { index, key ->
                val title = when (key) {
                    "all_points" -> stringResource(id = R.string.account_rating_all_point)
                    "Вне КТ" -> stringResource(id = R.string.account_rating_other_point)
                    else -> key
                }
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            data.points[tabs[page]]?.let { point ->
                PointContent(point = point, onSubjectClick = { selectedSubject = it })
            }
        }
    }

    selectedSubject?.let { subject ->
        ModalBottomSheet(
            onDismissRequest = { selectedSubject = null },
            sheetState = sheetState
        ) {
            SubjectDetailSheet(subject)
        }
    }
}

@Composable
private fun PointContent(
    point: RatingModel.Point,
    onSubjectClick: (RatingModel.Point.LessonByName) -> Unit
) {
    val average = point.listOfMarks.takeIf { it.isNotEmpty() }?.average()
    val subjects = point.listOfSubjects.sorted().mapNotNull { point.subjects[it] }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { RatingSummary(average, point.countOfOmissions) }
        items(subjects, key = { it.name }) { subject ->
            SubjectCard(subject = subject, onClick = { onSubjectClick(subject) })
        }
        item { LastUpdateListItem(CacheUpdateKeys.RATING) }
    }
}

@Composable
private fun RatingSummary(average: Double?, omissions: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        average?.let {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = markColor(it.toInt()).copy(alpha = 0.15f)
            ) {
                Text(
                    text = stringResource(R.string.account_rating_average_mark, "%.2f".format(it)),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = markColor(it.toInt()),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
        if (omissions > 0) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            ) {
                Text(
                    text = stringResource(R.string.account_rating_omissions, omissions),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubjectCard(subject: RatingModel.Point.LessonByName, onClick: () -> Unit) {
    val deadlineInfo = subject.deadlineInfo

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                subject.allTypes.sorted().forEach { type -> TypeBadge(type) }
            }

            if (subject.listOfMarks.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    subject.listOfMarks.forEach { MarkBadge(it) }
                }
            }

            if (subject.countOfOmissions > 0) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = stringResource(R.string.account_rating_omissions, subject.countOfOmissions),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (deadlineInfo != null) {
                DeadlineBadge(deadlineInfo)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubjectDetailSheet(subject: RatingModel.Point.LessonByName) {
    val deadlineInfo = subject.deadlineInfo

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = subject.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                subject.allTypes.sorted().forEach { TypeBadge(it) }
            }
        }
        HorizontalDivider()

        if (subject.listOfMarks.isNotEmpty()) {
            Text(
                text = stringResource(R.string.account_rating_dialog_marks).trim(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            subject.types.toSortedMap().forEach { (type, details) ->
                Text(type, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                details.all.filter { it.marks.isNotEmpty() }.forEach { lesson ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = lesson.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            lesson.marks.forEach { MarkBadge(it) }
                        }
                    }
                }
            }
        }

        if (subject.countOfOmissions > 0) {
            HorizontalDivider()
            Text(
                text = stringResource(R.string.account_rating_dialog_omissions).trim(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            subject.types.toSortedMap().forEach { (type, details) ->
                details.all.filter { it.omissions > 0 }.forEach { lesson ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$type · ${lesson.date}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = stringResource(R.string.account_rating_omissions_detailed, "", lesson.omissions)
                                .trim()
                                .trimStart('-')
                                .trim(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        if (subject.percentageMarks.isNotEmpty()) {
            HorizontalDivider()
            TypeBadge(stringResource(R.string.account_rating_percentage_type))

            subject.percentageMarks.forEach { mark ->
                Text(
                    text = stringResource(
                        R.string.account_rating_percentage_mark,
                        mark.date,
                        mark.number
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (deadlineInfo != null) {
            HorizontalDivider()
            Text(
                text = stringResource(R.string.account_rating_deadlines_title),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = deadlineInfo.progressText(),
                style = MaterialTheme.typography.bodyMedium
            )

            if (deadlineInfo.deadlines.isEmpty()) {
                Text(
                    text = stringResource(R.string.account_rating_deadlines_missing),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            deadlineInfo.deadlines
                .sortedBy { deadlineTimestamp(it.date) }
                .forEach { deadline ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        deadline.taskNumber?.let { taskNumber ->
                            Text(
                                text = stringResource(R.string.account_rating_deadline_task, taskNumber),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(
                                R.string.account_rating_deadline_date,
                                deadline.date
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (deadline.isOverdue()) {
                            Text(
                                text = stringResource(R.string.account_rating_deadline_overdue),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeadlineBadge(deadlineInfo: RatingModel.Point.LessonByName.DeadlineInfo) {
    val hasProblems = deadlineInfo.totalLabs == null ||
        deadlineInfo.deadlines.isEmpty() ||
        deadlineInfo.deadlines.any { it.isOverdue() }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (hasProblems) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.tertiaryContainer
        }
    ) {
        Text(
            text = deadlineInfo.progressText(),
            style = MaterialTheme.typography.labelSmall,
            color = if (hasProblems) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                MaterialTheme.colorScheme.onTertiaryContainer
            },
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun RatingModel.Point.LessonByName.DeadlineInfo.progressText(): String =
    totalLabs?.let { total ->
        stringResource(R.string.account_rating_deadlines_progress, submittedLabs, total)
    } ?: stringResource(R.string.account_rating_deadlines_submitted, submittedLabs)

private fun RatingModel.Point.LessonByName.DeadlineInfo.Deadline.isOverdue(): Boolean =
    deadlineTimestamp(date) < startOfTodayTimestamp()

private fun deadlineTimestamp(date: String): Long =
    runCatching {
        SimpleDateFormat("dd.MM.yyyy", Locale.ROOT).apply {
            isLenient = false
        }.parse(date)?.time
    }.getOrNull() ?: Long.MAX_VALUE

private fun startOfTodayTimestamp(): Long = Calendar.getInstance().run {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    timeInMillis
}

@Composable
private fun TypeBadge(type: String) {
    Surface(shape = RoundedCornerShape(4.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
        Text(
            text = type,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun MarkBadge(mark: Int) {
    Surface(shape = RoundedCornerShape(6.dp), color = markColor(mark)) {
        Text(
            text = mark.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun markColor(mark: Int): Color = when (mark) {
    in 9..10 -> Color(0xFF388E3C)
    in 7..8 -> MaterialTheme.colorScheme.primary
    in 4..6 -> Color(0xFFF57C00)
    else -> MaterialTheme.colorScheme.error
}
