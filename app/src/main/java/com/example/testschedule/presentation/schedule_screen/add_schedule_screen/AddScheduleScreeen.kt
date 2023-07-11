package com.example.testschedule.presentation.schedule_screen.add_schedule_screen

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Person
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
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import kotlinx.coroutines.launch

@Composable
fun AddScheduleScreen(
    goBackWhenSelect: (id: String, title: String) -> Unit,
    vm: AddScheduleViewModel = hiltViewModel()
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MySearchBar(goBackWhenSelect, vm.groups!!, vm.employees!!) }
    ) {

        Box(Modifier.padding(it)) {
            if (vm.groups?.isNotEmpty() == true && vm.employees?.isNotEmpty() == true) {
                TabScreen(goBackWhenSelect, vm.groups!!, vm.employees!!)
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
    goBackWhenSelect: (id: String, title: String) -> Unit,
    groups: List<ListOfGroupsModel>,
    employees: List<ListOfEmployeesModel>
) {
    val pagerState = rememberPagerState(0)

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Tabs(pagerState = pagerState)
        TabsContent(
            goBackWhenSelect = goBackWhenSelect,
            pagerState = pagerState,
            groups = groups,
            employees = employees
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val listOfTabs = listOf(
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
    goBackWhenSelect: (id: String, title: String) -> Unit,
    pagerState: PagerState,
    groups: List<ListOfGroupsModel>,
    employees: List<ListOfEmployeesModel>
) {
    HorizontalPager(
        state = pagerState,
        pageCount = 2
    ) { page ->
        when (page) {
            0 -> {
                GroupListView(goBackWhenSelect, groups)
            }

            1 -> {
                StaffListView(goBackWhenSelect, employees)
            }
        }
    }
}

@Composable
fun GroupListView(
    goBackWhenSelect: (id: String, title: String) -> Unit,
    groups: List<ListOfGroupsModel>
) {
    LazyColumn {
        groups.forEach {
            item {
                GroupItemCard(goBackWhenSelect = goBackWhenSelect, item = it)
            }
        }
    }
}


@Composable
fun SearchListView(
    goBackWhenSelect: (id: String, title: String) -> Unit,
    groups: List<ListOfGroupsModel>,
    employees: List<ListOfEmployeesModel>
) {
    LazyColumn {
        groups.forEach {
            item {
                GroupItemCard(goBackWhenSelect = goBackWhenSelect, item = it)
            }
        }
        employees.forEach {
            item {
                EmployeeItemCard(goBackWhenSelect = goBackWhenSelect, item = it)
            }
        }
    }
}

@Composable
fun StaffListView(
    goBackWhenSelect: (id: String, title: String) -> Unit,
    employees: List<ListOfEmployeesModel>
) {
    LazyColumn {
        employees.forEach {
            item {
                EmployeeItemCard(goBackWhenSelect = goBackWhenSelect, item = it)
            }
        }
    }
}


@Composable
fun MySearchBar(
    goBackWhenSelect: (id: String, title: String) -> Unit,
    groups: List<ListOfGroupsModel>,
    employees: List<ListOfEmployeesModel>
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
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    stringResource(id = R.string.search_schedule_hint)
                )
            }
        ) {
            var tmpText = text
            while (tmpText.isNotEmpty() && tmpText[0] == ' ') tmpText = tmpText.removePrefix(" ")

            if (tmpText.isNotEmpty()) {
                Column {
                    SearchListView(
                        goBackWhenSelect,
                        groups = groups.filter {
                            it.name.contains(tmpText) ||
                                    it.specialityAbbrev.contains(tmpText, true) ||
                                    it.facultyAbbrev.contains(tmpText, true)
                        },
                        employees = employees.filter { employee ->
                            employee.fio.contains(tmpText, true) ||
                                    employee.academicDepartment.any { it.contains(tmpText, true) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GroupItemCard(
    goBackWhenSelect: (id: String, title: String) -> Unit,
    item: ListOfGroupsModel
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
                onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    Icons.Outlined.Add, null,
                    Modifier,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun EmployeeItemCard(
    goBackWhenSelect: (id: String, title: String) -> Unit,
    item: ListOfEmployeesModel
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
                onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column {
                    Icon(
                        Icons.Outlined.Add, null,
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
        item = ListOfGroupsModel(
            1,
            "ФКСиС",
            "253501",
            "ИиТП",
            "Информатика и Технологии Программирования"
        )
    )
}

@Preview
@Composable
fun EmployeeCardPreview() {
    EmployeeItemCard(
        { _, _ -> },
        item = ListOfEmployeesModel(
            listOf("ФКП"),
            "Владымцев В. Д. (доцент)",
            "Вадим",
            "Владымцев",
            "Денисович",
            "htttp",
            "доцент",
            "v-vlad"
        )
    )
}
