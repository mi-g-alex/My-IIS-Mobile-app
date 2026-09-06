package com.example.testschedule.presentation.account.omissions_screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.common.CacheUpdateKeys
import com.example.testschedule.domain.model.account.omissions.OmissionsModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.LastUpdateListItem
import com.example.testschedule.presentation.account.dormitory_screen.timeLongToString
import kotlinx.coroutines.launch

@Composable
fun OmissionsScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: OmissionsViewModel = hiltViewModel()
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
                title = stringResource(id = R.string.account_omissions_title),
                enabled = backEnabled,
                isLoading = viewModel.isLoading.value
            )
        }
    ) { innerPadding ->
        OmissionsContent(
            omissions = viewModel.omissions.value,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OmissionsContent(omissions: List<OmissionsModel>, modifier: Modifier = Modifier) {
    val terms = remember(omissions) {
        omissions
            .groupBy { it.term }
            .toList()
            .sortedByDescending { it.first.toIntOrNull() ?: Int.MIN_VALUE }
    }

    if (terms.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(id = R.string.account_omissions_no_omissions),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val pagerState = rememberPagerState { terms.size }
    val scope = rememberCoroutineScope()
    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = pagerState.currentPage, edgePadding = 16.dp) {
            terms.forEachIndexed { index, term ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Text(
                            text = stringResource(
                                id = R.string.account_omissions_semester_number,
                                term.first
                            ),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
            }
        }
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                items(terms[page].second.sortedByDescending { it.dateFrom }, key = { it.id }) {
                    OmissionsItem(it)
                }
                item { LastUpdateListItem(CacheUpdateKeys.OMISSIONS) }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
fun OmissionsItem(item: OmissionsModel) {
    val icon = omissionIcon(item.name)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon.first,
                contentDescription = null,
                modifier = Modifier.size(20.dp).padding(top = 2.dp),
                tint = icon.second
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = omissionDate(item),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (item.note.isNotBlank()) {
                    Text(
                        text = item.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun omissionIcon(name: String): Pair<ImageVector, Color> = when {
    name.contains("Мед", ignoreCase = true) || name.contains("ОРВИ", ignoreCase = true) ->
        Icons.Filled.LocalHospital to Color(0xFF4CAF50)
    name.contains("Заявление", ignoreCase = true) ->
        Icons.Filled.Person to MaterialTheme.colorScheme.secondary
    name.contains("Приказ", ignoreCase = true) || name.contains("Распоряжение", ignoreCase = true) ->
        Icons.Filled.MedicalServices to MaterialTheme.colorScheme.tertiary
    else -> Icons.Filled.Description to MaterialTheme.colorScheme.primary
}

@Composable
private fun omissionDate(item: OmissionsModel): String {
    val format = stringResource(id = R.string.account_omissions_date_format)
    val from = timeLongToString(item.dateFrom, format)
    return if (item.dateFrom == item.dateTo) {
        stringResource(id = R.string.account_omissions_date, from).substringAfter(":").trim()
    } else {
        val to = timeLongToString(item.dateTo, format)
        stringResource(id = R.string.account_omissions_dates, from, to)
            .substringAfter(":")
            .trim()
    }
}
