package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import android.content.Context
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testschedule.R
import com.example.testschedule.common.Constants
import com.example.testschedule.data.local.entity.ListOfSavedEntity
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun ViewScheduleScreen(
    scheduleId: String? = null,
    titleLink: String? = null,
    goToAddSchedule: () -> Unit,
    viewModel: ViewScheduleViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val sharedPref = cnt.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE)
    val openScheduleId = sharedPref.getString(Constants.PREF_OPEN_BY_DEFAULT_ID, null)
    val openScheduleTitle = sharedPref.getString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, null)
    LaunchedEffect(null) {
        viewModel.getSaved()
        viewModel.title.value = titleLink ?: ""
        if (scheduleId != null)
            viewModel.getSchedule(scheduleId)
        else {
            if (openScheduleTitle != null && openScheduleId != null) {
                viewModel.getSchedule(openScheduleId)
                viewModel.title.value = openScheduleTitle
            }
        }
    }

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
                { }
            )
        },
    ) { pv ->
        if (openScheduleId == null) {
            NoScheduleAdded()
        } else
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

@Composable
fun NoScheduleAdded() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_schedule_added_start_screen),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ShowSchedule(it: ScheduleModel, modifier: Modifier) {

    LazyColumn(modifier = modifier) {
        item {
            val cal = Calendar.getInstance().time
            Text(String.format("dd MM yyyy HH:mm:ss", cal), )
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