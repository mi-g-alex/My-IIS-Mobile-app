package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testschedule.R
import com.example.testschedule.data.local.entity.ListOfSavedEntity
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import kotlinx.coroutines.launch

@Composable
fun ViewScheduleScreen(
    scheduleId: String? = null,
    titleLink: String? = null,
    goToAddSchedule: () -> Unit,
    viewModel: ViewScheduleViewModel = hiltViewModel()
) {
    LaunchedEffect(null) {
        viewModel.getSaved()
        viewModel.title.value = titleLink ?: ""
        if (scheduleId != null)
            viewModel.getSchedule(scheduleId)
        else {
            viewModel.getSchedule("253501")
            viewModel.title.value = "253501"
        }
    }

    val cnt = LocalContext.current

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true }
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val vm = viewModel.state.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                titleText = viewModel.title.value,
                { showBottomSheet = true },
                { showToast(cnt, "ActionButton", Toast.LENGTH_LONG) })
        },
    ) { pv ->
        if (vm.schedule != null) {
            ShowSchedule(vm.schedule, Modifier.padding(pv))
        }

        if (vm.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(pv)
                    .fillMaxWidth(

                    )
            )
        }

        if (!vm.error.isNullOrBlank()) {
            Text(
                vm.error.toString(),
                modifier = Modifier
                    .padding(pv)
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
                onEditButtonClicked = { goToAddSchedule() },
                selectScheduleClicked = { id, t ->
                    viewModel.getSchedule(id)
                    viewModel.title.value = t
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
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
fun ShowSchedule(it: ScheduleModel, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        for (i in 1..it.schedules.size) {
            val week = it.schedules[i - 1]
            stickyHeader {
                Text(
                    text = "Понедельник Неделя $i",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            week.monday.forEach { lesson ->
                item {
                    Text(
                        text = lesson.subjectFullName + " (${lesson.lessonTypeAbbrev})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
            stickyHeader {
                Text(
                    text = "Вторник Неделя $i",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            week.tuesday.forEach { lesson ->
                item {
                    Text(
                        text = lesson.subjectFullName + " (${lesson.lessonTypeAbbrev})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
            stickyHeader {
                Text(
                    text = "Среда Неделя $i",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            week.wednesday.forEach { lesson ->
                item {
                    Text(
                        text = lesson.subjectFullName + " (${lesson.lessonTypeAbbrev})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
            stickyHeader {
                Text(
                    text = "Четверг Неделя $i",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            week.thursday.forEach { lesson ->
                item {
                    Text(
                        text = lesson.subjectFullName + " (${lesson.lessonTypeAbbrev})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
            stickyHeader {
                Text(
                    text = "Пятница Неделя $i",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            week.friday.forEach { lesson ->
                item {
                    Text(
                        text = lesson.subjectFullName + " (${lesson.lessonTypeAbbrev})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
            stickyHeader {
                Text(
                    text = "Суббота Неделя $i",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            week.saturday.forEach { lesson ->
                item {
                    Text(
                        text = lesson.subjectFullName + " (${lesson.lessonTypeAbbrev})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }

        }
    }
}

@Composable
fun MyTopAppBar(
    titleText: String,
    navIconClicked: () -> Unit,
    actionIconClicked: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                titleText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = navIconClicked) {
                Icon(
                    Icons.Default.Menu,
                    stringResource(id = R.string.open_list_of_saved_schedule_desc)
                )
            }
        },
        actions = {
            Row { // TODO
                IconButton(onClick = actionIconClicked) {
                    Icon(
                        Icons.Default.Favorite,
                        stringResource(id = R.string.open_list_of_saved_schedule_desc)
                    )
                }
            }
        }
    )
}

@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    closeBottomSheet: () -> Unit,
    onEditButtonClicked: () -> Unit,
    selectScheduleClicked: (id: String, title: String) -> Unit,
    saved: MutableState<List<ListOfSavedEntity>?>,
    savedGroups: MutableState<List<ListOfGroupsModel>>,
    savedEmployees: MutableState<List<ListOfEmployeesModel>>
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.bottom_sheet_title),
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onEditButtonClicked(); closeBottomSheet(); },
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            stringResource(id = R.string.bottom_sheet_edit_btn_desc)
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            saved.value?.forEach {
                if (it.isGroup) {
                    savedGroups.value.find { item -> item.name == it.id }?.let { it1 ->
                        item {
                            GroupItemCard(
                                selectScheduleClicked,
                                item = it1
                            )
                        }
                    }
                } else {
                    savedEmployees.value.find { item -> item.urlId == it.id }?.let { it1 ->
                        item {
                            EmployeeItemCard(
                                selectScheduleClicked,
                                item = it1
                            )
                        }
                    }
                }
            }
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
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.photoLink)
                                .crossfade(true).build(),
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

private fun showToast(cnt: Context, text: String, length: Int) {
    Toast.makeText(cnt, text, length).show()
}