package com.example.testschedule.presentation.account.announcement_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.announcement.AnnouncementModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar

@Composable
fun AnnouncementsScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    goToSchedule: (urlId: String, title: String) -> Unit,
    viewModel: AnnouncementsViewModel = hiltViewModel()
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
                title = stringResource(id = R.string.account_announcement_title),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty(),
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        if (viewModel.announcement.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val announcements = viewModel.announcement.value
                announcements.forEach { item ->
                    item {
                        AnnouncementItem(item = item, goToSchedule = goToSchedule)
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.account_announcement_no_announcement),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun AnnouncementItem(
    item: AnnouncementModel,
    goToSchedule: (urlId: String, title: String) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            if (item.content != null)
                Text(
                    text = item.content,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

            if (item.date != null) {
                Text(
                    stringResource(
                        id = R.string.account_announcement_date,
                        item.date
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            if (item.startTime != null && item.endTime != null) {
                Text(
                    stringResource(
                        id = R.string.account_announcement_time,
                        item.startTime,
                        item.endTime
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            if(item.auditory != null) {
                Text(
                    stringResource(
                        id = R.string.account_announcement_place,
                        item.auditory,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }


            if (item.employee != null) {
                AssistChip(
                    onClick = {
                        if (item.urlId?.isNotEmpty() == true)
                            goToSchedule(
                                item.urlId, item.employee
                            )
                    },
                    label = {
                        Text(text = item.employee)
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

@Preview
@Composable
fun Preview() {
    AnnouncementItem(
        AnnouncementModel(
            1, "15.09.2021",
            "09:00",
            "11:00",
            "401-4к",
            "Мигалевич А. В.",
            "Пересдача ОАиП",
            null
        ),
        goToSchedule = { _, _ -> }
    )

}
