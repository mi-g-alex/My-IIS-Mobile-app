package com.example.testschedule.presentation.account.dormitory_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.dormitory.DormitoryModel
import com.example.testschedule.domain.model.account.dormitory.PrivilegesModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.ListDataSection
import com.example.testschedule.presentation.account.additional_elements.SectionItem
import java.util.Calendar
import java.util.GregorianCalendar

@Composable
fun DormitoryScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: DormitoryViewModel = hiltViewModel()
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
                title = stringResource(id = R.string.account_dormitory_title),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty(),
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) { padVal ->

        val listOfItem = mutableListOf<SectionItem>()

        if (viewModel.dormitory.value != null) {
            listOfItem += SectionItem(
                title = stringResource(id = R.string.account_dormitory_dormitory_title),
                emptyText = stringResource(id = R.string.account_dormitory_dormitory_no_requests),
                itemList = viewModel.dormitory.value!!.sortedByDescending { i -> i.acceptedDate }
                    .map { { DormitoryCard(it) } }
            )
        }

        if (viewModel.privileges.value != null) {
            listOfItem += SectionItem(
                title = stringResource(id = R.string.account_dormitory_privileges_title),
                emptyText = stringResource(id = R.string.account_dormitory_privileges_no_privileges),
                itemList = viewModel.privileges.value!!.sortedByDescending { i -> i.year }
                    .map { { PrivilegeCard(item = it) } }
            )
        }

        ListDataSection(
            listOfItems = listOfItem,
            paddingValues = padVal
        )
    }
}

@Composable
fun DormitoryCard(item: DormitoryModel) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        val status = when (item.status) {
            "Документы приняты" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_doc_accepted)
            "Ожидание" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_pending)
            "К заселению" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_await_settlement)
            "Заселён" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_settled)
            "Отклонена" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_rejection)
            "Выселен" -> stringResource(id = R.string.account_dormitory_dormitory_card_status_evicted)
            else -> stringResource(id = R.string.account_dormitory_dormitory_card_status_unknown)
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                if (item.roomInfo != null) {
                    Text(
                        text = stringResource(
                            id = R.string.account_dormitory_dormitory_card_room_info,
                            item.roomInfo
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (item.number != null) {
                Text(
                    text = stringResource(
                        id = R.string.account_dormitory_dormitory_card_request_number,
                        item.number
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            if (item.applicationDate != null) {
                Text(
                    text = stringResource(
                        id = R.string.account_dormitory_dormitory_card_request_time,
                        timeLongToString(
                            item.applicationDate,
                            stringResource(id = R.string.account_dormitory_dormitory_date_format)
                        )
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            if (item.acceptedDate != null) {
                Text(
                    text = stringResource(
                        id = R.string.account_dormitory_dormitory_card_accepted_time,
                        timeLongToString(
                            item.acceptedDate,
                            stringResource(id = R.string.account_dormitory_dormitory_date_format)
                        )
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            if (item.settledDate != null) {
                Text(
                    text = stringResource(
                        id = R.string.account_dormitory_dormitory_card_settled_time,
                        timeLongToString(
                            item.settledDate,
                            stringResource(id = R.string.account_dormitory_dormitory_date_format)
                        )
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            if (item.rejectionReason?.isNotEmpty() == true) {
                Text(
                    text = stringResource(
                        id = R.string.account_dormitory_dormitory_card_rejection_reason,
                        item.rejectionReason
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
fun PrivilegeCard(item: PrivilegesModel) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        val type = when (item.dormitoryPrivilegeCategoryName) {
            "Внеочередное право" -> stringResource(id = R.string.account_dormitory_privileges_type_0)
            "Первоочередное право" -> stringResource(id = R.string.account_dormitory_privileges_type_1)
            "Приоритетное право" -> stringResource(id = R.string.account_dormitory_privileges_type_2)
            else -> item.dormitoryPrivilegeName ?: ""
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            if (type.isNotBlank() || item.year != null) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    if (item.year != null) {
                        Text(
                            text = item.year.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (item.dormitoryPrivilegeName != null) {
                Text(
                    text = "\n" + item.dormitoryPrivilegeName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (item.note?.isNotEmpty() == true) {
                Text(
                    text = stringResource(
                        id = R.string.account_dormitory_privileges_note,
                        item.note
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


fun timeLongToString(time: Long, format: String): String {
    val cal = GregorianCalendar()
    cal.timeInMillis = time
    var day = cal.get(Calendar.DAY_OF_MONTH).toString()
    if (day.length == 1) day = "0$day"

    var month = (cal.get(Calendar.MONTH) + 1).toString()
    if (month.length == 1) month = "0$month"

    val year = cal.get(Calendar.YEAR).toString()

    return format.replace("dd", day).replace("MM", month).replace("yyyy", year)
}