package com.example.testschedule.presentation.account.notifications_screen

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.testschedule.domain.model.account.notifications.NotificationModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.LastUpdateListItem

@Composable
fun NotificationsScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: NotificationsViewModel = hiltViewModel()
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
                title = stringResource(id = R.string.account_notification_title),
                enabled = backEnabled,
                isLoading = viewModel.isLoading.value
            )
        }
    ) { innerPadding ->
        val notifications = viewModel.notifications.value.sortedByDescending { it.id }
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.account_notification_no_notifications),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                items(notifications, key = { it.id }) { NotificationItem(it) }
                item { LastUpdateListItem(CacheUpdateKeys.NOTIFICATIONS) }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
fun NotificationItem(item: NotificationModel) {
    val typeInfo = notificationTypeInfo(item.type)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isViewed) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = typeInfo.color.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = typeInfo.icon,
                        contentDescription = typeInfo.description,
                        modifier = Modifier.size(20.dp),
                        tint = typeInfo.color
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = typeInfo.description,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (item.isViewed) FontWeight.Normal else FontWeight.SemiBold,
                        color = typeInfo.color
                    )
                    Text(
                        text = item.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = item.message.replace('\n', ' ').trim(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (item.isViewed) FontWeight.Normal else FontWeight.Medium
                )
            }
        }
    }
}

private data class NotificationTypeInfo(
    val icon: ImageVector,
    val color: Color,
    val description: String
)

@Composable
private fun notificationTypeInfo(type: String): NotificationTypeInfo = when (type) {
    "SUCCESS" -> NotificationTypeInfo(
        icon = Icons.Outlined.CheckCircle,
        color = Color(0xFF388E3C),
        description = stringResource(id = R.string.account_notification_icon_success)
    )
    "FAILURE" -> NotificationTypeInfo(
        icon = Icons.Outlined.Warning,
        color = MaterialTheme.colorScheme.error,
        description = stringResource(id = R.string.account_notification_icon_failure)
    )
    "INFO" -> NotificationTypeInfo(
        icon = Icons.Outlined.Info,
        color = MaterialTheme.colorScheme.primary,
        description = stringResource(id = R.string.account_notification_icon_info)
    )
    else -> NotificationTypeInfo(
        icon = Icons.Outlined.Info,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        description = stringResource(id = R.string.account_notification_icon_other)
    )
}
