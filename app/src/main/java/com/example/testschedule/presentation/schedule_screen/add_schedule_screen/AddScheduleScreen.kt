package com.example.testschedule.presentation.schedule_screen.add_schedule_screen

import android.content.Context
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testschedule.R
import com.example.testschedule.common.Constants
import com.example.testschedule.data.local.entity.ListOfSavedEntity
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import kotlinx.coroutines.launch

@Composable
fun AddScheduleScreen(
    goBack: () -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    vm: AddScheduleViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val sharedPref = cnt.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MySearchBar(
                goBack,
                { item, deleting ->
                    val tmp = sharedPref.getString(Constants.PREF_OPEN_BY_DEFAULT_ID, null)
                    if (deleting) {
                        if (tmp == item.id) {
                            sharedPref.edit()
                                .putString(Constants.PREF_OPEN_BY_DEFAULT_ID, null)
                                .putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, null)
                                .apply()
                        }
                    } else {
                        if (tmp == null) {
                            sharedPref.edit()
                                .putString(Constants.PREF_OPEN_BY_DEFAULT_ID, item.id)
                                .putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, item.title)
                                .apply()
                        }
                    }
                    vm.saveOrRemoveFromSaved(item)
                }, goBackWhenSelect, vm.groups, vm.employees, vm.savedSchedule
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            if (vm.groups.value?.isNotEmpty() == true && vm.employees.value?.isNotEmpty() == true) {
                TabScreen(
                    { item, deleting ->
                        var saved = vm.savedSchedule.value?.toMutableList()
                        val tmp = sharedPref.getString(Constants.PREF_OPEN_BY_DEFAULT_ID, null)
                        if (deleting) {
                            saved =
                                saved?.filter { item1 -> item1.id != tmp } as MutableList<ListOfSavedEntity>?
                            if (tmp == item.id) {
                                if (saved?.isNotEmpty() == true) {
                                    sharedPref.edit()
                                        .putString(
                                            Constants.PREF_OPEN_BY_DEFAULT_ID,
                                            saved[0].id
                                        )
                                        .putString(
                                            Constants.PREF_OPEN_BY_DEFAULT_TITLE,
                                            saved[0].title
                                        )
                                        .apply()
                                } else {
                                    sharedPref.edit()
                                        .putString(
                                            Constants.PREF_OPEN_BY_DEFAULT_ID,
                                            null
                                        )
                                        .putString(
                                            Constants.PREF_OPEN_BY_DEFAULT_TITLE,
                                            null
                                        )
                                        .apply()
                                }
                            }
                        } else {
                            if (tmp == null) {
                                sharedPref.edit()
                                    .putString(Constants.PREF_OPEN_BY_DEFAULT_ID, item.id)
                                    .putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, item.title)
                                    .apply()
                            }
                        }
                        vm.saveOrRemoveFromSaved(item)
                    },
                    goBackWhenSelect,
                    vm.groups,
                    vm.employees,
                    vm.savedSchedule,
                    vm.savedGroups,
                    vm.savedEmployees
                )
            }
        }
    }
    if (vm.state.value.isLoading) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    groups: MutableState<List<ListOfGroupsModel>?>,
    employees: MutableState<List<ListOfEmployeesModel>?>,
    saved: MutableState<List<ListOfSavedEntity>?>,
    savedGroups: MutableState<List<ListOfGroupsModel>>,
    savedEmployees: MutableState<List<ListOfEmployeesModel>>
) {
    val pagerState = rememberPagerState(if (saved.value?.size == 0) 1 else 0)

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Tabs(pagerState = pagerState)
        TabsContent(
            onAddRemoveButtonClicked = onAddRemoveButtonClicked,
            goBackWhenSelect = goBackWhenSelect,
            pagerState = pagerState,
            groups = groups,
            employees = employees,
            saved = saved,
            savedGroups = savedGroups,
            savedEmployees = savedEmployees
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val listOfTabs = listOf(
        stringResource(id = R.string.add_schedule_saved_title),
        stringResource(id = R.string.add_schedule_group_title),
        stringResource(id = R.string.add_schedule_staff_title)
    )
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) {
        listOfTabs.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    pagerState: PagerState,
    groups: MutableState<List<ListOfGroupsModel>?>,
    employees: MutableState<List<ListOfEmployeesModel>?>,
    saved: MutableState<List<ListOfSavedEntity>?>,
    savedGroups: MutableState<List<ListOfGroupsModel>>,
    savedEmployees: MutableState<List<ListOfEmployeesModel>>
) {
    HorizontalPager(
        state = pagerState,
        pageCount = 3
    ) { page ->
        when (page) {
            0 -> {
                SavedListView(
                    onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                    goBackWhenSelect = goBackWhenSelect,
                    saved = saved,
                    savedGroups = savedGroups,
                    savedEmployees = savedEmployees
                )
            }

            1 -> {
                GroupListView(
                    onAddRemoveButtonClicked,
                    goBackWhenSelect,
                    groups,
                    saved
                )
            }

            2 -> {
                EmployeeListView(
                    onAddRemoveButtonClicked,
                    goBackWhenSelect,
                    employees,
                    saved
                )
            }
        }
    }
}

@Composable
fun SavedListView(
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    saved: MutableState<List<ListOfSavedEntity>?>,
    savedGroups: MutableState<List<ListOfGroupsModel>>,
    savedEmployees: MutableState<List<ListOfEmployeesModel>>
) {
    val sharedPref =
        LocalContext.current.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE)
    val openDialog = remember { mutableStateOf(false) }
    saved.value?.let {
        if (openDialog.value) {
            OpenByDefaultScheduleDialog(
                closeDialog = { openDialog.value = false },
                clickOk = { item ->
                    if (item != null) {
                        sharedPref.edit()
                            .putString(Constants.PREF_OPEN_BY_DEFAULT_ID, item.id)
                            .putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, item.title)
                            .apply()
                    }
                    openDialog.value = false
                },
                saved = it,
                selected = sharedPref.getString(Constants.PREF_OPEN_BY_DEFAULT_ID, "") ?: ""
            )
        }
    }
    if (saved.value?.isNotEmpty() == true)
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                ElevatedCard(
                    onClick = {
                        openDialog.value = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(id = R.string.select_default_schedule_text),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = sharedPref.getString(
                                    Constants.PREF_OPEN_BY_DEFAULT_TITLE,
                                    null
                                ) ?: stringResource(id = R.string.nothing_selected_schedule),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Icon(
                            Icons.Outlined.KeyboardArrowRight,
                            stringResource(id = R.string.add_schedule_saved_title)
                        )
                    }
                }
            }

            item {
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 8.dp)
                )
            }

            saved.value?.forEach {
                if (it.isGroup) {
                    if (savedGroups.value.any { item -> item.name == it.id }) {
                        savedGroups.value.find { item -> item.name == it.id }?.let { it1 ->
                            item {
                                GroupItemCard(
                                    onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                                    goBackWhenSelect = goBackWhenSelect,
                                    item = it1,
                                    isSaved = true
                                )
                            }
                        }
                    } else {
                        item {
                            GroupItemCard(
                                onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                                goBackWhenSelect = goBackWhenSelect,
                                item = ListOfGroupsModel(
                                    0,
                                    stringResource(id = R.string.removed_from_server_schedules_list),
                                    it.title,
                                    "",
                                    ""
                                ),
                                isSaved = true
                            )
                        }
                    }
                } else {
                    if (savedEmployees.value.any { item -> item.urlId == it.id }) {
                        savedEmployees.value.find { item -> item.urlId == it.id }?.let { it1 ->
                            item {
                                EmployeeItemCard(
                                    onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                                    goBackWhenSelect = goBackWhenSelect,
                                    item = it1,
                                    isSaved = true
                                )
                            }
                        }
                    } else {
                        item {
                            EmployeeItemCard(
                                onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                                goBackWhenSelect = goBackWhenSelect,
                                item = ListOfEmployeesModel(
                                    listOf(stringResource(id = R.string.removed_from_server_schedules_list)),
                                    it.title,
                                    it.title,
                                    "",
                                    null,
                                    null,
                                    null,
                                    it.id
                                ),
                                isSaved = true
                            )
                        }
                    }
                }
            }
        }
    else {
        Box(Modifier.fillMaxSize()) {
            Text(
                stringResource(id = R.string.no_schedule_saved_title),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}


@Composable
fun GroupListView(
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    groups: MutableState<List<ListOfGroupsModel>?>,
    saved: MutableState<List<ListOfSavedEntity>?>
) {
    LazyColumn {
        groups.value?.forEach {
            val isSaved =
                saved.value?.contains(ListOfSavedEntity(it.name, true, it.name)) ?: false
            item {
                GroupItemCard(
                    onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                    goBackWhenSelect = goBackWhenSelect,
                    item = it,
                    isSaved = isSaved
                )
            }
        }
    }
}


@Composable
fun SearchListView(
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    groups: List<ListOfGroupsModel>,
    employees: List<ListOfEmployeesModel>,
    saved: MutableState<List<ListOfSavedEntity>?>
) {
    LazyColumn {
        groups.forEach {
            item {
                val isSaved =
                    saved.value?.contains(ListOfSavedEntity(it.name, true, it.name)) ?: false
                GroupItemCard(
                    onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                    goBackWhenSelect = goBackWhenSelect,
                    item = it,
                    isSaved = isSaved
                )
            }
        }
        employees.forEach {
            item {
                val isSaved =
                    saved.value?.contains(ListOfSavedEntity(it.urlId, false, it.fio)) ?: false
                EmployeeItemCard(
                    onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                    goBackWhenSelect = goBackWhenSelect,
                    item = it,
                    isSaved = isSaved
                )
            }
        }
    }
}

@Composable
fun EmployeeListView(
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    employees: MutableState<List<ListOfEmployeesModel>?>,
    saved: MutableState<List<ListOfSavedEntity>?>
) {
    LazyColumn {
        employees.value?.forEach {
            val isSaved =
                saved.value?.contains(ListOfSavedEntity(it.urlId, false, it.fio)) ?: false
            item {
                EmployeeItemCard(
                    onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                    goBackWhenSelect = goBackWhenSelect,
                    item = it,
                    isSaved = isSaved
                )
            }
        }
    }
}


@Composable
fun MySearchBar(
    goBack: () -> Unit,
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    groups: MutableState<List<ListOfGroupsModel>?>,
    employees: MutableState<List<ListOfEmployeesModel>?>,
    saved: MutableState<List<ListOfSavedEntity>?>
) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        SearchBar(
            query = text,
            onQueryChange = { text = it },
            onSearch = { keyboardController?.hide() },
            active = active,
            onActiveChange = { active = !(active); if (!active) text = "" },
            placeholder = {
                Text(stringResource(id = R.string.search_schedule_hint))
            },
            trailingIcon = {
                Icon(
                    Icons.Default.Search,
                    stringResource(id = R.string.search_schedule_hint)
                )
            },
            leadingIcon = {
                IconButton(onClick = { if(active) active = false else goBack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        ) {
            var tmpText = text
            while (tmpText.isNotEmpty() && tmpText[0] == ' ') tmpText = tmpText.removePrefix(" ")

            if (tmpText.isNotEmpty()) {
                Column {
                    SearchListView(
                        onAddRemoveButtonClicked = onAddRemoveButtonClicked,
                        goBackWhenSelect = goBackWhenSelect,
                        groups = groups.value?.filter {
                            it.name.contains(tmpText) ||
                                    it.specialityAbbrev.contains(tmpText, true) ||
                                    it.facultyAbbrev.contains(tmpText, true)
                        } ?: emptyList(),
                        employees = employees.value?.filter { employee ->
                            employee.fio.contains(tmpText, true) ||
                                    employee.academicDepartment.any {
                                        it.contains(
                                            tmpText,
                                            true
                                        )
                                    }
                        } ?: emptyList(),
                        saved = saved
                    )
                }
            }
        }
    }
}

@Composable
fun GroupItemCard(
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    item: ListOfGroupsModel,
    isSaved: Boolean
) {
    ElevatedCard(
        onClick = {
            goBackWhenSelect(item.name, item.name)
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

            IconButton(
                onClick = {
                    onAddRemoveButtonClicked(
                        ListOfSavedEntity(
                            item.name,
                            true,
                            item.name
                        ),
                        isSaved
                    )
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    if (isSaved) Icons.Outlined.Delete else Icons.Outlined.Add,
                    if (isSaved) stringResource(id = R.string.remove_schedule_desc)
                    else stringResource(id = R.string.save_schedule_desc),
                    Modifier,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun EmployeeItemCard(
    onAddRemoveButtonClicked: (item: ListOfSavedEntity, deleting: Boolean) -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    item: ListOfEmployeesModel,
    isSaved: Boolean
) {
    ElevatedCard(
        onClick = {
            goBackWhenSelect(item.urlId, item.fio)
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

            IconButton(
                onClick = {
                    onAddRemoveButtonClicked(
                        ListOfSavedEntity(
                            item.urlId,
                            false,
                            item.fio
                        ),
                        isSaved
                    )
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column {
                    Icon(
                        if (isSaved) Icons.Outlined.Delete else Icons.Outlined.Add,
                        if (isSaved) stringResource(id = R.string.remove_schedule_desc)
                        else stringResource(id = R.string.save_schedule_desc),
                        Modifier,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CardPreview() {
    GroupItemCard(
        { _, _ -> },
        { _, _ -> },
        item = ListOfGroupsModel(
            1,
            "ФКСиС",
            "253501",
            "ИиТП",
            "Информатика и Технологии Программирования"
        ),
        true
    )
}

@Preview
@Composable
fun EmployeeCardPreview() {
    EmployeeItemCard(
        { _, _ -> },
        { _, _ -> },
        item = ListOfEmployeesModel(
            listOf("ФКП"),
            "Владымцев В. Д. (доцент)",
            "Вадим",
            "Владымцев",
            "Денисович",
            "htttp",
            "доцент",
            "v-vlad",
        ),
        false
    )
}