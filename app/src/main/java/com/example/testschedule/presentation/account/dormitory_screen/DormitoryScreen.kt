package com.example.testschedule.presentation.account.dormitory_screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.common.CacheUpdateKeys
import com.example.testschedule.domain.model.account.dormitory.DormitoryModel
import com.example.testschedule.domain.model.account.dormitory.PrivilegesModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.LastUpdateListItem
import java.util.Calendar
import java.util.GregorianCalendar

@Composable
fun DormitoryScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: DormitoryViewModel = hiltViewModel()
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
                title = stringResource(id = R.string.account_dormitory_title),
                enabled = backEnabled,
                isLoading = viewModel.isLoading.value
            )
        }
    ) { innerPadding ->
        DormitoryContent(
            applications = viewModel.dormitory.value.orEmpty(),
            privileges = viewModel.privileges.value.orEmpty(),
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun DormitoryContent(
    applications: List<DormitoryModel>,
    privileges: List<PrivilegesModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Spacer(Modifier.height(4.dp)) }
        item { SectionTitle(stringResource(id = R.string.account_dormitory_dormitory_title)) }
        if (applications.isEmpty()) {
            item { EmptySection(stringResource(id = R.string.account_dormitory_dormitory_no_requests)) }
        } else {
            items(applications.sortedByDescending { it.acceptedDate }, key = { it.id }) {
                DormitoryCard(it)
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
        item { SectionTitle(stringResource(id = R.string.account_dormitory_privileges_title)) }
        if (privileges.isEmpty()) {
            item { EmptySection(stringResource(id = R.string.account_dormitory_privileges_no_privileges)) }
        } else {
            items(privileges.sortedByDescending { it.year }, key = { it.id }) {
                PrivilegeCard(it)
            }
        }
        item { LastUpdateListItem(CacheUpdateKeys.DORMITORY) }
        item { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun EmptySection(text: String) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DormitoryCard(item: DormitoryModel) {
    val status = when (item.status) {
        "Документы приняты" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_doc_accepted)
        "Ожидание" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_pending)
        "К заселению" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_await_settlement)
        "Заселён" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_settled)
        "Отклонена" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_rejection)
        "Выселен" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_evicted)
        else -> stringResource(id = R.string.account_dormitory_dormitory_card_status_unknown)
    }
    val dateFormat = stringResource(id = R.string.account_dormitory_dormitory_date_format)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = status, sourceStatus = item.status)
                item.number?.let {
                    Text(
                        text = stringResource(
                            id = R.string.account_dormitory_dormitory_card_request_number,
                            it
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item.roomInfo?.takeIf { it.isNotBlank() }?.let { room ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Apartment,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = room, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                }
            }

            item.applicationDate?.let {
                DateText(stringResource(id = R.string.account_dormitory_dormitory_card_request_time, timeLongToString(it, dateFormat)))
            }
            item.acceptedDate?.let {
                DateText(stringResource(id = R.string.account_dormitory_dormitory_card_accepted_time, timeLongToString(it, dateFormat)))
            }
            item.settledDate?.let {
                DateText(stringResource(id = R.string.account_dormitory_dormitory_card_settled_time, timeLongToString(it, dateFormat)))
            }
            item.rejectionReason?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = stringResource(id = R.string.account_dormitory_dormitory_card_rejection_reason, it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String, sourceStatus: String?) {
    val color = when (sourceStatus) {
        "Заселён" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
        "Ожидание", "К заселению" -> Color(0xFFFFA726).copy(alpha = 0.2f)
        "Отклонена" -> MaterialTheme.colorScheme.errorContainer
        "Документы приняты" -> Color(0xFF2196F3).copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun DateText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun PrivilegeCard(item: PrivilegesModel) {
    val type = when (item.dormitoryPrivilegeCategoryName) {
        "Внеочередное право" -> stringResource(id = R.string.account_dormitory_privileges_type_0)
        "Первоочередное право" -> stringResource(id = R.string.account_dormitory_privileges_type_1)
        "Приоритетное право" -> stringResource(id = R.string.account_dormitory_privileges_type_2)
        else -> item.dormitoryPrivilegeCategoryName.orEmpty()
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = type, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                item.year?.let {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item.dormitoryPrivilegeName?.takeIf { it.isNotBlank() }?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall)
            }
            item.note?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = stringResource(id = R.string.account_dormitory_privileges_note, it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun timeLongToString(time: Long, format: String): String {
    val calendar = GregorianCalendar().apply { timeInMillis = time }
    val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
    val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
    val year = calendar.get(Calendar.YEAR).toString()
    return format.replace("dd", day).replace("MM", month).replace("yyyy", year)
}
