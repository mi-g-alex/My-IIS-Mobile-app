@file:OptIn(ExperimentalLayoutApi::class)

package com.example.testschedule.presentation.account.group_screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.group.GroupModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    goToSchedule: (urlId: String, title: String) -> Unit,
    viewModel: GroupViewModel = hiltViewModel()
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
                title =
                if (viewModel.group.value?.numberOfGroup?.isNotEmpty() == true)
                    stringResource(
                        id = R.string.account_group_title_with_number,
                        viewModel.group.value!!.numberOfGroup
                    )
                else
                    stringResource(id = R.string.account_group_title),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty(),
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (viewModel.group.value?.studentGroupCurator != null) {
                stickyHeader {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Text(
                            stringResource(id = R.string.account_group_curator),
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
                item {
                    CuratorCard(
                        item = viewModel.group.value!!.studentGroupCurator!!,
                        goToSchedule = goToSchedule
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            if (viewModel.group.value?.groupInfoStudent?.isNotEmpty() == true) {
                stickyHeader {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Text(
                            stringResource(id = R.string.account_group_title),
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
                viewModel.group.value?.groupInfoStudent!!.sortedBy { i -> i.fio }
                    .forEach { groupInfoStudent ->
                        item {
                            StudentCard(groupInfoStudent)
                        }
                    }
            }
        }
    }
}

@Composable
fun CuratorCard(
    item: GroupModel.StudentGroupCurator,
    goToSchedule: (urlId: String, title: String) -> Unit
) {
    val ctx = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val copiedPhone = stringResource(id = R.string.copy_success, item.phone.toString())
    val copiedEmail = stringResource(id = R.string.copy_success, item.email.toString())
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = item.fio,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (item.phone?.isNotEmpty() == true) {
                    AssistChip(
                        onClick = {
                            val u = Uri.parse("tel:" + item.phone)
                            val i = Intent(Intent.ACTION_DIAL, u)
                            try {
                                ctx.startActivity(i)
                            } catch (_: Exception) {
                            }
                        },
                        label = {
                            Text(text = item.phone)
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Call,
                                item.phone,
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(item.phone))
                                    Toast.makeText(ctx, copiedPhone, Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.copy_icon),
                                    stringResource(id = R.string.copy),
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        }
                    )
                }
                if (item.email?.isNotEmpty() == true) {
                    AssistChip(
                        onClick = {
                            val emailIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + item.email))
                            try {
                                ctx.startActivity(emailIntent)
                            } catch (_: Exception) {
                            }
                        },
                        label = {
                            Text(text = item.email)
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Email,
                                item.email,
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(item.email))
                                    Toast.makeText(ctx, copiedEmail, Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.copy_icon),
                                    stringResource(id = R.string.copy),
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        }
                    )
                }
                if (item.urlId?.isNotEmpty() == true) {
                    AssistChip(
                        onClick = {
                            goToSchedule(
                                item.urlId, item.fio
                            )
                        },
                        label = {
                            Text(stringResource(id = R.string.account_group_curator_schedule))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.DateRange,
                                stringResource(id = R.string.account_group_curator_schedule),
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                    )
                }
            }
        }

    }
}

@Composable
fun StudentCard(item: GroupModel.GroupInfoStudent) {
    val status = when (item.position) {
        "Староста группы" -> stringResource(id = R.string.account_group_monitor)
        "Заместитель старосты группы" -> stringResource(id = R.string.account_group_deputy_monitor)
        else -> item.position
    }
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = item.fio,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (status.isNotEmpty()) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}