package com.example.testschedule.presentation.account.rating_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.rating.RatingModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RatingScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: RatingViewModel = hiltViewModel()
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopBar(
                onBackPressed = { onBackPressed(); enabled = false },
                title = stringResource(id = R.string.account_rating_title),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty(),
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        if (viewModel.rating.value != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                TabScreen(data = viewModel.rating.value!!)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(data: RatingModel) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { data.allPoints.size }
    )
    val listOfTabs = mutableListOf("all_points")
    listOfTabs.addAll(data.points.keys.filter { it != "all_points" })

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        RatingTabs(pagerState = pagerState, data = data, listOfTabs = listOfTabs)
        TabsContent(pagerState = pagerState, data = data, listOfTabs = listOfTabs)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RatingTabs(data: RatingModel, pagerState: PagerState, listOfTabs: List<String>) {

    val scope = rememberCoroutineScope()
    val listOfTabsName = listOfTabs.map {
        when (it) {
            "Вне КТ" -> stringResource(id = R.string.account_rating_other_point)
            "all_points" -> stringResource(id = R.string.account_rating_all_point)
            else -> it
        }
    }

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier.fillMaxWidth(),
        divider = {}
    ) {
        listOfTabsName.forEachIndexed { index, title ->
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(pagerState: PagerState, data: RatingModel, listOfTabs: List<String>) {
    val dialogData = remember {
        mutableStateOf<RatingModel.Point.LessonByName?>(null)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    HorizontalPager(
        state = pagerState
    ) { page ->
        data.points[listOfTabs[page]]?.let { PointSubjectList(it, dialogData, showDialog) }
    }
    if (showDialog.value) {
        dialogData.value?.let { RatingDialog(it) { showDialog.value = false } }
            ?: { showDialog.value = false }
    }
}

@Composable
fun PointSubjectList(
    data: RatingModel.Point,
    dialogData: MutableState<RatingModel.Point.LessonByName?>,
    showDialog: MutableState<Boolean>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        var sum = 0f
        data.listOfMarks.forEach { sum += it }
        if (data.listOfMarks.isNotEmpty())
            sum /= data.listOfMarks.size
        val round = (sum * 100).roundToInt() / 100.0
        if (data.listOfMarks.isNotEmpty())
            item {
                Text(
                    stringResource(
                        id = R.string.account_rating_average_mark,
                        round.toString()
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        item {
            Text(
                stringResource(
                    id = R.string.account_rating_omissions,
                    data.countOfOmissions
                ),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }

        data.listOfSubjects.sorted().forEach { lesson ->
            data.subjects[lesson]?.let {
                item {
                    SubjectCard(
                        data = it,
                        lessonName = lesson,
                        dialogData,
                        showDialog
                    )
                }
            }
        }
    }
}

@Composable
fun SubjectCard(
    data: RatingModel.Point.LessonByName,
    lessonName: String,
    dialogData: MutableState<RatingModel.Point.LessonByName?>,
    showDialog: MutableState<Boolean>
) {
    OutlinedCard(
        onClick = {
            dialogData.value = data
            showDialog.value = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text(
                text = lessonName,
                style = MaterialTheme.typography.headlineMedium
            )
            data.allTypes.sorted().forEach { type ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = stringResource(
                            id = R.string.account_rating_main_info,
                            data.types[type]?.countOfMarks?.size ?: 0,
                            data.types[type]?.countOfOmissions ?: 0
                        ),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
fun RatingDialog(data: RatingModel.Point.LessonByName, closeDialog: () -> Unit) {
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
        title = {
            Text(text = data.name)
        },
        text = {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
            ) {
                if (data.listOfMarks.isNotEmpty()) {
                    var average = 0f
                    data.listOfMarks.forEach { average += it.toFloat() }
                    average /= data.listOfMarks.size
                    average = (average * 100).roundToInt() / 100f
                    item {
                        Text(
                            text = stringResource(
                                R.string.account_rating_average_mark,
                                average.toString()
                            ),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
                item {
                    Text(
                        text = stringResource(
                            R.string.account_rating_omissions,
                            data.countOfOmissions
                        ),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
                if (data.listOfMarks.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.account_rating_dialog_marks),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    val a = data.types.filter { it.value.countOfMarks.isNotEmpty() }
                    a.forEach { i ->
                        item {
                            Text(
                                text = "\n${i.key}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        i.value.all.filter { it.marks.isNotEmpty() }.forEach { it ->
                            item {
                                Text(
                                    text = it.date + " – " + it.marks.toString().removePrefix("[")
                                        .removeSuffix("]"),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                if (data.countOfOmissions != 0) {
                    item {
                        Text(
                            text = stringResource(R.string.account_rating_dialog_omissions),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    val a = data.types.filter { it.value.countOfOmissions != 0 }
                    a.forEach { i ->
                        item {
                            Text(
                                text = "\n${i.key}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        i.value.all.filter { it.omissions != 0 }.forEach {
                            item {
                                Text(
                                    text = stringResource(id = R.string.account_rating_omissions_detailed, it.date, it.omissions),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    )
}