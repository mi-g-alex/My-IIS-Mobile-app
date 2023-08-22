@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.example.testschedule.presentation.account.mark_book_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.mark_book.MarkBookModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun MarkBookScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: MarkBookViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val errorText = stringResource(id = R.string.error_to_login)
    var enabled by remember { mutableStateOf(true) }
    LaunchedEffect(viewModel.errorText.value) {
        if (viewModel.errorText.value == "WrongPassword") {
            Toast.makeText(cnt, errorText, Toast.LENGTH_LONG).show()
            onLogOut()
        }
    }
    /*fun getTitle(it: Double): String {
        var s = it.toString()
        if (s.length == 1) s += ".0"
        return s
    }*/
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopBar(
                onBackPressed = { onBackPressed(); enabled = false },
                title = if (viewModel.markBook.value?.averageMark != 0.0 && viewModel.markBook.value != null) stringResource(
                    id = R.string.account_mark_book_title_with_mark,
                    viewModel.markBook.value?.averageMark!!.toString()
                ) else stringResource(id = R.string.account_mark_book_title),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty()
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.markBook.value != null)
                if (viewModel.markBook.value?.semesters?.isNotEmpty() == true) {
                    TabScreen(viewModel.markBook.value!!)
                } else {
                    Text(
                        text = stringResource(id = R.string.account_mark_book_no_semesters),
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                    )
                }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(item: MarkBookModel) {
    val pagerState = rememberPagerState(
        initialPage = getInitialPage(item),
        pageCount = { item.semesters.size }
    )
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Tabs(pagerState = pagerState, item.semesters.size)
        TabsContent(pagerState = pagerState, item = item)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState, count: Int) {
    val listOfTabs = (1..count).toList().map { it.toString() }
    val scope = rememberCoroutineScope()
    if (listOfTabs.size > 4)
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            divider = {}
        ) {
            listOfTabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
    else {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            divider = {}
        ) {
            listOfTabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(pagerState: PagerState, item: MarkBookModel) {
    val dialogData = remember {
        mutableStateOf<MarkBookModel.Semester.Mark?>(null)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    HorizontalPager(
        state = pagerState
    ) { page ->
        item.semesters[page + 1]?.let { SemesterPage(item = it, dialogData, showDialog) }
    }
    if (showDialog.value) {
        dialogData.value?.let { MarkBookExamDialog(it) { showDialog.value = false } }
            ?: { showDialog.value = false }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SemesterPage(
    item: MarkBookModel.Semester,
    dialogData: MutableState<MarkBookModel.Semester.Mark?>,
    showDialog: MutableState<Boolean>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (item.averageMark > 0)
            stickyHeader {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        stringResource(
                            id = R.string.account_mark_book_semester_average_mark,
                            item.averageMark.toString()
                        ),
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        item {
            item.marks.forEach {
                MarkBookExamCard(it) {
                    dialogData.value = it
                    showDialog.value = true
                }
            }
        }
    }
}

@Composable
fun MarkBookExamDialog(
    item: MarkBookModel.Semester.Mark,
    closeDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { closeDialog() },
        confirmButton = {
            TextButton(
                onClick = {
                    closeDialog()
                }
            ) {
                Text(stringResource(id = R.string.ok))
            }
        },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = item.fullSubject,
                    style = MaterialTheme.typography.titleLarge
                )
                if (item.mark.isNotEmpty())
                    Text(
                        text = stringResource(id = R.string.account_mark_book_exam_mark, item.mark),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                if (item.teacher.isNotEmpty()) {
                    Text(
                        text = item.teacher,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (item.formOfControl.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            id = R.string.account_mark_book_exam_type,
                            item.formOfControl
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (item.date.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.account_mark_book_exam_date, item.date),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (item.retakesCount > 0) {
                    Text(
                        text = stringResource(
                            id = R.string.account_mark_book_exam_date,
                            item.retakesCount.toString()
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (item.hours.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            id = R.string.account_mark_book_exam_hours,
                            item.hours
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (item.commonMark != null && item.commonMark > 0) {
                    val mark = (item.commonMark * 100).roundToInt() / 100.0
                    Text(
                        text = stringResource(
                            id = R.string.account_mark_book_exam_average_mark_four_year,
                            mark.toString()
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (item.commonRetakes != null) {
                    val mark = (item.commonRetakes * 10000).roundToInt() / 100.0
                    Text(
                        text = stringResource(
                            id = R.string.account_mark_book_exam_average_retakes_four_year,
                            mark.toString()
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    )
}

@Composable
fun MarkBookExamCard(item: MarkBookModel.Semester.Mark, onClick: () -> Unit) {
    OutlinedCard(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.subject,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = item.mark,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            if (item.teacher.isNotEmpty()) {
                Text(
                    text = item.teacher,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (item.formOfControl.isNotEmpty()) {
                Text(
                    text = stringResource(
                        id = R.string.account_mark_book_exam_type,
                        item.formOfControl
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (item.date.isNotEmpty()) {
                Text(
                    text = stringResource(id = R.string.account_mark_book_exam_date, item.date),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (item.retakesCount > 0) {
                Text(
                    text = stringResource(
                        id = R.string.account_mark_book_exam_date,
                        item.retakesCount.toString()
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


fun getInitialPage(item: MarkBookModel): Int {
    var def = 0
    item.semesters.forEach {
        var hasMarks = false
        it.value.marks.forEach { it1 ->
            if (it1.mark != "") hasMarks = true
        }
        if (hasMarks) {
            def = it.key - 1
            if (def < 0) def = 0
        } else {
            return@forEach
        }
    }
    return def
}