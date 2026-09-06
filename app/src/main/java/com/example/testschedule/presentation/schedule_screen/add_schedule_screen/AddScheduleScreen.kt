package com.example.testschedule.presentation.schedule_screen.add_schedule_screen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.testschedule.R
import com.example.testschedule.common.Constants
import com.example.testschedule.data.local.entity.schedule.ListOfSavedEntity
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.presentation.LoadingTopBar
import com.example.testschedule.presentation.schedule_screen.add_schedule_screen.spec_items.MoreDetailsAboutSchedule
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddScheduleScreen(
    goBack: () -> Unit,
    goBackWhenSelect: (id: String, title: String) -> Unit,
    vm: AddScheduleViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE)
    }
    val groups = vm.groups.value.orEmpty()
    val employees = vm.employees.value.orEmpty()
    val saved = vm.savedSchedule.value.orEmpty()
    val favoriteIds = remember(saved) { saved.mapTo(mutableSetOf()) { it.id } }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    var initializedPage by rememberSaveable { mutableStateOf(false) }
    var defaultScheduleId by rememberSaveable {
        mutableStateOf(preferences.getString(Constants.PREF_OPEN_BY_DEFAULT_ID, null))
    }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var groupNumber by rememberSaveable { mutableStateOf("") }
    var showGroupNumberDialog by rememberSaveable { mutableStateOf(false) }
    var infoGroup by remember { mutableStateOf<ListOfGroupsModel?>(null) }
    var infoEmployee by remember { mutableStateOf<ListOfEmployeesModel?>(null) }

    LaunchedEffect(vm.hasSavedLoaded.value) {
        if (vm.hasSavedLoaded.value && !initializedPage) {
            pagerState.scrollToPage(if (saved.isEmpty()) 1 else 0)
            initializedPage = true
        }
    }

    fun toggleFavorite(item: ListOfSavedEntity) {
        val deleting = item.id in favoriteIds
        val currentDefault = preferences.getString(Constants.PREF_OPEN_BY_DEFAULT_ID, null)
        if (deleting && currentDefault == item.id) {
            val replacement = saved.firstOrNull { it.id != item.id }
            defaultScheduleId = replacement?.id
            preferences.edit {
                putString(Constants.PREF_OPEN_BY_DEFAULT_ID, replacement?.id)
                putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, replacement?.title)
            }
        } else if (!deleting && currentDefault.isNullOrBlank()) {
            defaultScheduleId = item.id
            preferences.edit {
                putString(Constants.PREF_OPEN_BY_DEFAULT_ID, item.id)
                putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, item.title)
            }
        }
        vm.saveOrRemoveFromSaved(item)
    }

    fun selectSchedule(id: String, title: String) {
        preferences.edit {
            putString(Constants.PREF_PENDING_SCHEDULE_ID, id)
            putString(Constants.PREF_PENDING_SCHEDULE_TITLE, title)
        }
        goBackWhenSelect(id, title)
    }

    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        searchQuery = ""
    }

    AnimatedContent(
        targetState = isSearchActive,
        transitionSpec = {
            (fadeIn() + scaleIn(initialScale = 0.97f)) togetherWith
                (fadeOut() + scaleOut(targetScale = 0.97f))
        },
        label = "ScheduleSearchTransition"
    ) { searchActive ->
        if (searchActive) {
            ScheduleSearch(
                query = searchQuery,
                groups = groups.filtered(searchQuery) {
                    listOf(it.name, it.facultyAbbrev, it.specialityName, it.specialityAbbrev)
                },
                employees = employees.filtered(searchQuery) {
                    listOf(it.fio, it.lastName, it.firstName, it.middleName, it.rank, it.degree) +
                        it.academicDepartment
                },
                favoriteIds = favoriteIds,
                onQueryChange = { searchQuery = it },
                onClose = {
                    isSearchActive = false
                    searchQuery = ""
                },
                onGroupSelected = { selectSchedule(it.name, it.name) },
                onEmployeeSelected = { selectSchedule(it.urlId, it.fio) },
                onGroupInfo = { infoGroup = it },
                onEmployeeInfo = { infoEmployee = it },
                onFavoriteToggle = ::toggleFavorite
            )
        } else {
            Scaffold(
                topBar = {
                    LoadingTopBar(isLoading = vm.state.value.isLoading) {
                        TopAppBar(
                            title = { Text(stringResource(R.string.schedule_ui_title)) },
                            navigationIcon = {
                                IconButton(onClick = goBack) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { isSearchActive = true }) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = stringResource(R.string.schedule_ui_search)
                                    )
                                }
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
                    TabRow(selectedTabIndex = pagerState.currentPage) {
                        listOf(
                            R.string.schedule_add_saved_title,
                            R.string.schedule_add_groups_title,
                            R.string.schedule_add_employees_title
                        ).forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                text = { Text(stringResource(title)) }
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        when (page) {
                            0 -> SavedSchedulesList(
                                saved = saved,
                                groups = groups,
                                employees = employees,
                                defaultScheduleId = defaultScheduleId,
                                onSelect = ::selectSchedule,
                                onRemove = ::toggleFavorite,
                                onSetDefault = { item ->
                                    defaultScheduleId = item.id
                                    preferences.edit {
                                        putString(Constants.PREF_OPEN_BY_DEFAULT_ID, item.id)
                                        putString(Constants.PREF_OPEN_BY_DEFAULT_TITLE, item.title)
                                    }
                                }
                            )

                            1 -> ScheduleListState(
                                isLoading = vm.state.value.isLoading,
                                error = vm.state.value.error,
                                isEmpty = groups.isEmpty(),
                                onRetry = vm::retry
                            ) {
                                GroupList(
                                    groups = groups,
                                    favoriteIds = favoriteIds,
                                    onSelect = { selectSchedule(it.name, it.name) },
                                    onInfo = { infoGroup = it },
                                    onFavoriteToggle = ::toggleFavorite,
                                    onManualEntry = { showGroupNumberDialog = true }
                                )
                            }

                            else -> ScheduleListState(
                                isLoading = vm.state.value.isLoading,
                                error = vm.state.value.error,
                                isEmpty = employees.isEmpty(),
                                onRetry = vm::retry
                            ) {
                                EmployeeList(
                                    employees = employees,
                                    favoriteIds = favoriteIds,
                                    onSelect = { selectSchedule(it.urlId, it.fio) },
                                    onInfo = { infoEmployee = it },
                                    onFavoriteToggle = ::toggleFavorite
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showGroupNumberDialog) {
        GroupNumberDialog(
            input = groupNumber,
            onInputChange = { groupNumber = it.filter(Char::isDigit).take(6) },
            onConfirm = {
                val id = groupNumber
                vm.addToSaved(ListOfSavedEntity(id, true, id)) {
                    selectSchedule(id, id)
                    showGroupNumberDialog = false
                    groupNumber = ""
                }
            },
            onDismiss = {
                showGroupNumberDialog = false
                groupNumber = ""
            }
        )
    }

    infoGroup?.let { group ->
        MoreDetailsAboutSchedule(
            closeDialog = { infoGroup = null },
            isGroup = true,
            dataOfGroup = group,
            dataOfEmployee = null,
            isFavorite = group.name in favoriteIds,
            onFavoriteToggle = {
                toggleFavorite(ListOfSavedEntity(group.name, true, group.name))
            }
        )
    }
    infoEmployee?.let { employee ->
        MoreDetailsAboutSchedule(
            closeDialog = { infoEmployee = null },
            isGroup = false,
            dataOfGroup = null,
            dataOfEmployee = employee,
            isFavorite = employee.urlId in favoriteIds,
            onFavoriteToggle = {
                toggleFavorite(ListOfSavedEntity(employee.urlId, false, employee.fio))
            }
        )
    }
}

@Composable
private fun ScheduleListState(
    isLoading: Boolean,
    error: String?,
    isEmpty: Boolean,
    onRetry: () -> Unit,
    content: @Composable () -> Unit
) {
    when {
        isLoading && isEmpty -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        error != null && isEmpty -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.schedule_ui_load_error),
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry, modifier = Modifier.padding(top = 12.dp)) {
                Text(stringResource(R.string.schedule_ui_retry))
            }
        }

        else -> content()
    }
}

@Composable
private fun GroupList(
    groups: List<ListOfGroupsModel>,
    favoriteIds: Set<String>,
    onSelect: (ListOfGroupsModel) -> Unit,
    onInfo: (ListOfGroupsModel) -> Unit,
    onFavoriteToggle: (ListOfSavedEntity) -> Unit,
    onManualEntry: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item { EnterGroupNumberCard(onManualEntry) }
        items(groups, key = { it.name }) { group ->
            GroupCard(
                group = group,
                isFavorite = group.name in favoriteIds,
                onClick = { onSelect(group) },
                onMoreClick = { onInfo(group) },
                onFavoriteToggle = {
                    onFavoriteToggle(ListOfSavedEntity(group.name, true, group.name))
                }
            )
        }
    }
}

@Composable
private fun EmployeeList(
    employees: List<ListOfEmployeesModel>,
    favoriteIds: Set<String>,
    onSelect: (ListOfEmployeesModel) -> Unit,
    onInfo: (ListOfEmployeesModel) -> Unit,
    onFavoriteToggle: (ListOfSavedEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(employees, key = { it.urlId }) { employee ->
            EmployeeCard(
                employee = employee,
                isFavorite = employee.urlId in favoriteIds,
                onClick = { onSelect(employee) },
                onMoreClick = { onInfo(employee) },
                onFavoriteToggle = {
                    onFavoriteToggle(ListOfSavedEntity(employee.urlId, false, employee.fio))
                }
            )
        }
    }
}

@Composable
private fun SavedSchedulesList(
    saved: List<ListOfSavedEntity>,
    groups: List<ListOfGroupsModel>,
    employees: List<ListOfEmployeesModel>,
    defaultScheduleId: String?,
    onSelect: (String, String) -> Unit,
    onRemove: (ListOfSavedEntity) -> Unit,
    onSetDefault: (ListOfSavedEntity) -> Unit
) {
    if (saved.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.schedule_no_saved_title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(saved, key = { it.id }) { item ->
            val group = groups.firstOrNull { it.name == item.id }
            val employee = employees.firstOrNull { it.urlId == item.id }
            SavedScheduleCard(
                item = item,
                subtitle = if (item.isGroup) {
                    listOfNotNull(
                        group?.facultyAbbrev?.takeIf(String::isNotBlank),
                        group?.specialityAbbrev?.takeIf(String::isNotBlank),
                        group?.course?.takeIf { it > 0 }?.let { stringResource(R.string.schedule_ui_course, it) }
                    ).joinToString(" · ").ifBlank { stringResource(R.string.schedule_ui_group) }
                } else {
                    employee?.academicDepartment?.joinToString(", ")?.takeIf(String::isNotBlank)
                        ?: employee?.rank?.takeIf(String::isNotBlank)
                        ?: stringResource(R.string.schedule_ui_employee)
                },
                photoLink = employee?.photoLink,
                isDefault = item.id == defaultScheduleId,
                onClick = { onSelect(item.id, item.title) },
                onRemove = { onRemove(item) },
                onSetDefault = { onSetDefault(item) }
            )
        }
    }
}

@Composable
private fun ScheduleSearch(
    query: String,
    groups: List<ListOfGroupsModel>,
    employees: List<ListOfEmployeesModel>,
    favoriteIds: Set<String>,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    onGroupSelected: (ListOfGroupsModel) -> Unit,
    onEmployeeSelected: (ListOfEmployeesModel) -> Unit,
    onGroupInfo: (ListOfGroupsModel) -> Unit,
    onEmployeeInfo: (ListOfEmployeesModel) -> Unit,
    onFavoriteToggle: (ListOfSavedEntity) -> Unit
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {},
        active = true,
        onActiveChange = { if (!it) onClose() },
        placeholder = { Text(stringResource(R.string.schedule_ui_search_hint)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (groups.isNotEmpty()) {
                item { SearchSectionHeader(stringResource(R.string.schedule_add_groups_title)) }
                items(groups, key = { it.name }) { group ->
                    GroupCard(
                        group = group,
                        isFavorite = group.name in favoriteIds,
                        onClick = { onGroupSelected(group) },
                        onMoreClick = { onGroupInfo(group) },
                        onFavoriteToggle = {
                            onFavoriteToggle(ListOfSavedEntity(group.name, true, group.name))
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.dp)
                    )
                }
            }
            if (employees.isNotEmpty()) {
                item { SearchSectionHeader(stringResource(R.string.schedule_add_employees_title)) }
                items(employees, key = { it.urlId }) { employee ->
                    EmployeeCard(
                        employee = employee,
                        isFavorite = employee.urlId in favoriteIds,
                        onClick = { onEmployeeSelected(employee) },
                        onMoreClick = { onEmployeeInfo(employee) },
                        onFavoriteToggle = {
                            onFavoriteToggle(ListOfSavedEntity(employee.urlId, false, employee.fio))
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchSectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun GroupCard(
    group: ListOfGroupsModel,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 2.dp,
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Text(group.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    listOf(group.facultyAbbrev, group.specialityAbbrev)
                        .filter(String::isNotBlank)
                        .joinToString(" · ")
                        .ifBlank { " " },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (group.course > 0) {
                Text(
                    stringResource(R.string.schedule_ui_course, group.course),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            FavoriteButton(isFavorite, onFavoriteToggle)
            IconButton(onClick = onMoreClick, modifier = Modifier.size(40.dp)) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.more),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmployeeCard(
    employee: ListOfEmployeesModel,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 2.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EmployeePhoto(employee.photoLink)
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    employee.fio,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    employee.academicDepartment.joinToString(", ").ifBlank { employee.rank.orEmpty() }
                        .ifBlank { " " },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            FavoriteButton(isFavorite, onFavoriteToggle)
            IconButton(onClick = onMoreClick, modifier = Modifier.size(40.dp)) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.more),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
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

@Composable
private fun SavedScheduleCard(
    item: ListOfSavedEntity,
    subtitle: String,
    photoLink: String?,
    isDefault: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    onSetDefault: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 2.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.isGroup) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Groups,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            } else {
                EmployeePhoto(photoLink)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (isDefault) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = stringResource(R.string.schedule_ui_default),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(18.dp)
                        )
                    }
                }
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            FavoriteButton(isFavorite = true, onClick = onRemove)
            Box {
                IconButton(onClick = { showMenu = true }, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.more),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (isDefault) {
                                    stringResource(R.string.schedule_ui_default)
                                } else {
                                    stringResource(R.string.schedule_ui_set_default)
                                }
                            )
                        },
                        onClick = {
                            if (!isDefault) onSetDefault()
                            showMenu = false
                        },
                        enabled = !isDefault,
                        leadingIcon = if (isDefault) {
                            {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmployeePhoto(photoLink: String?) {
    SubcomposeAsyncImage(
        model = photoLink,
        contentDescription = null,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    ) {
        if (painter.state is AsyncImagePainter.State.Success) {
            SubcomposeAsyncImageContent()
        } else {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun EnterGroupNumberCard(onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(
                stringResource(R.string.schedule_ui_enter_group),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun GroupNumberDialog(
    input: String,
    onInputChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.schedule_ui_enter_group)) },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                label = { Text(stringResource(R.string.schedule_ui_group_hint)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = input.length == 6) {
                Text(stringResource(R.string.schedule_ui_open))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

private fun <T> List<T>.filtered(query: String, fields: (T) -> List<String?>): List<T> {
    if (query.isBlank()) return this
    val words = query.trim().split("\\s+".toRegex()).filter(String::isNotEmpty)
    return filter { item ->
        words.all { word -> fields(item).any { it?.contains(word, ignoreCase = true) == true } }
    }
}
