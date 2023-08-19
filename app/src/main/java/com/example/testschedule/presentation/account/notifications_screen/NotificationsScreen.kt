package com.example.testschedule.presentation.account.notifications_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar

@Composable
fun NotificationsScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: NotificationsViewModel = hiltViewModel()
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
                title = stringResource(id = R.string.account_notification_title),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty(),
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        if (viewModel.notifications.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                viewModel.notifications.value.sortedByDescending { it.id }.forEach {
                    item {
                        NotificationItem(item = it)
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.account_notification_no_notifications),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}

@Composable
fun NotificationItem(
    item: NotificationModel
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val icon = when (item.type) {
                    "SUCCESS" -> Icons.Outlined.CheckCircle
                    "FAILURE" -> Icons.Outlined.Warning
                    "INFO" -> Icons.Outlined.Info
                    else -> Icons.Outlined.Info
                }
                val text = when (item.type) {
                    "SUCCESS" -> stringResource(id = R.string.account_notification_icon_success)
                    "FAILURE" -> stringResource(id = R.string.account_notification_icon_failure)
                    "INFO" -> stringResource(id = R.string.account_notification_icon_info)
                    else -> stringResource(id = R.string.account_notification_icon_other)
                }
                Icon(
                    imageVector = icon,
                    contentDescription = text
                )
                Text(
                    text = item.date,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Text(
            text = item.message.replace('\n', ' '),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
}
