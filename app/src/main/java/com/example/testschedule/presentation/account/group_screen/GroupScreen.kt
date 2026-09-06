package com.example.testschedule.presentation.account.group_screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.common.CacheUpdateKeys
import com.example.testschedule.domain.model.account.group.GroupModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.LastUpdateListItem

@Composable
fun GroupScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    goToSchedule: (urlId: String, title: String) -> Unit,
    viewModel: GroupViewModel = hiltViewModel()
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
                title = viewModel.group.value?.numberOfGroup
                    ?.takeIf { it.isNotBlank() }
                    ?.let { stringResource(R.string.account_group_title_with_number, it) }
                    ?: stringResource(R.string.account_group_title),
                enabled = backEnabled,
                isLoading = viewModel.isLoading.value
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            viewModel.group.value?.let { group ->
                GroupContent(group = group, goToSchedule = goToSchedule)
            }
        }
    }
}

@Composable
private fun GroupContent(
    group: GroupModel,
    goToSchedule: (urlId: String, title: String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        group.studentGroupCurator?.let { curator ->
            item {
                CuratorCard(
                    item = curator,
                    goToSchedule = goToSchedule,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
        items(group.groupInfoStudent.sortedBy { it.fio }) { student ->
            StudentCard(student)
            HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
        }
        item { LastUpdateListItem(CacheUpdateKeys.GROUP) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CuratorCard(
    item: GroupModel.StudentGroupCurator,
    goToSchedule: (urlId: String, title: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.position,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = item.fio,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item.phone?.takeIf { it.isNotBlank() }?.let { phone ->
                    AssistChip(
                        onClick = {
                            try {
                                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
                            } catch (_: Exception) {
                            }
                        },
                        label = { Text(phone) },
                        leadingIcon = {
                            Icon(Icons.Filled.Phone, contentDescription = null)
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            leadingIconContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                item.email?.takeIf { it.isNotBlank() }?.let { email ->
                    AssistChip(
                        onClick = {
                            try {
                                context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")))
                            } catch (_: Exception) {
                            }
                        },
                        label = { Text(email) },
                        leadingIcon = {
                            Icon(Icons.Filled.Email, contentDescription = null)
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            leadingIconContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                item.urlId?.takeIf { it.isNotBlank() }?.let { urlId ->
                    AssistChip(
                        onClick = { goToSchedule(urlId, item.fio) },
                        label = { Text(stringResource(id = R.string.account_group_curator_schedule)) },
                        leadingIcon = {
                            Icon(Icons.Filled.CalendarMonth, contentDescription = null)
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            leadingIconContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StudentCard(item: GroupModel.GroupInfoStudent) {
    val position = when (item.position) {
        "Староста группы" -> stringResource(id = R.string.account_group_monitor)
        "Заместитель старосты группы" -> stringResource(id = R.string.account_group_deputy_monitor)
        else -> item.position
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = item.fio, style = MaterialTheme.typography.bodyLarge)
        if (position.isNotBlank()) {
            Text(
                text = position,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
