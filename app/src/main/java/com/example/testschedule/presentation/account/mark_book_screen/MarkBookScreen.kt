package com.example.testschedule.presentation.account.mark_book_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.common.CacheUpdateKeys
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.LastUpdateListItem
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun MarkBookScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: MarkBookViewModel = hiltViewModel()
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
                title = stringResource(id = R.string.account_mark_book_title),
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
            viewModel.markBook.value?.let { markBook ->
                if (markBook.semesters.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.account_mark_book_no_semesters),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                } else {
                    MarkBookContent(markBook)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun MarkBookContent(markBook: MarkBookModel) {
    val semesters = remember(markBook.semesters) { markBook.semesters.entries.sortedBy { it.key } }
    val pagerState = rememberPagerState(
        initialPage = getInitialPage(markBook).coerceIn(0, semesters.lastIndex),
        pageCount = { semesters.size }
    )
    val scope = rememberCoroutineScope()
    var selectedMark by remember { mutableStateOf<MarkBookModel.Semester.Mark?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(modifier = Modifier.fillMaxSize()) {
        if (markBook.averageMark > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        id = R.string.account_mark_book_title_with_mark,
                        markBook.averageMark.toString()
                    ).substringBefore("|").trim(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatMark(markBook.averageMark),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = markColor(markBook.averageMark)
                )
            }
            HorizontalDivider()
        }

        ScrollableTabRow(selectedTabIndex = pagerState.currentPage, edgePadding = 0.dp) {
            semesters.forEachIndexed { index, semester ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(semester.key.toString()) }
                )
            }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            val semester = semesters[page].value
            Column(modifier = Modifier.fillMaxSize()) {
                if (semester.averageMark > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.account_mark_book_semester_average_mark,
                                ""
                            ).substringBefore(":").trim(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatMark(semester.averageMark),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = markColor(semester.averageMark)
                        )
                    }
                    HorizontalDivider()
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(semester.marks) { mark ->
                        MarkBookExamCard(mark, onClick = { selectedMark = mark })
                        HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
                    }
                    item { LastUpdateListItem(CacheUpdateKeys.MARK_BOOK) }
                }
            }
        }
    }

    selectedMark?.let { mark ->
        ModalBottomSheet(
            onDismissRequest = { selectedMark = null },
            sheetState = sheetState
        ) {
            MarkBookDetailSheet(mark)
        }
    }
}

@Composable
fun MarkBookExamCard(item: MarkBookModel.Semester.Mark, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = markBadgeColor(item.mark),
                modifier = Modifier.size(width = 44.dp, height = 36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = item.mark.ifBlank { "-" },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.subject.ifBlank { item.fullSubject },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                if (item.formOfControl.isNotBlank()) {
                    Text(
                        text = item.formOfControl,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (item.date.isNotBlank()) {
                Text(
                    text = item.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MarkBookDetailSheet(item: MarkBookModel.Semester.Mark) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = item.fullSubject.ifBlank { item.subject },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = markBadgeColor(item.mark)) {
                Text(
                    text = item.mark.ifBlank { "-" },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
            Column {
                if (item.formOfControl.isNotBlank()) {
                    Text(item.formOfControl, style = MaterialTheme.typography.bodyMedium)
                }
                if (item.retakesCount > 0) {
                    Text(
                        text = stringResource(
                            id = R.string.account_mark_book_exam_retakes,
                            item.retakesCount.toString()
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        HorizontalDivider()
        if (item.date.isNotBlank()) DetailText(stringResource(R.string.account_mark_book_exam_date, item.date))
        if (item.teacher.isNotBlank()) DetailText(item.teacher)
        if (item.hours.isNotBlank()) DetailText(stringResource(R.string.account_mark_book_exam_hours, item.hours))
        if (item.credits != 0) DetailText(stringResource(R.string.account_mark_book_exam_credit, item.credits))
        item.commonMark?.takeIf { it > 0 }?.let {
            DetailText(
                stringResource(
                    R.string.account_mark_book_exam_average_mark_four_year,
                    formatMark(it)
                )
            )
        }
        item.commonRetakes?.let {
            val percent = (it * 10000).roundToInt() / 100.0
            DetailText(
                stringResource(
                    R.string.account_mark_book_exam_average_retakes_four_year,
                    percent.toString()
                )
            )
        }
    }
}

@Composable
private fun DetailText(text: String) {
    Text(text = text, style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun markBadgeColor(mark: String): Color = when (mark.toIntOrNull()) {
    in 9..10 -> Color(0xFF388E3C)
    in 7..8 -> MaterialTheme.colorScheme.primary
    in 4..6 -> Color(0xFFF57C00)
    in 1..3 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.secondary
}

@Composable
private fun markColor(mark: Double): Color = when {
    mark >= 9 -> Color(0xFF388E3C)
    mark >= 7 -> MaterialTheme.colorScheme.primary
    mark >= 4 -> Color(0xFFF57C00)
    else -> MaterialTheme.colorScheme.error
}

private fun formatMark(value: Double): String = "%.2f".format(value)

fun getInitialPage(item: MarkBookModel): Int {
    val semesters = item.semesters.entries.sortedBy { it.key }
    return semesters.indexOfLast { semester -> semester.value.marks.any { it.mark.isNotBlank() } }
        .coerceAtLeast(0)
}
