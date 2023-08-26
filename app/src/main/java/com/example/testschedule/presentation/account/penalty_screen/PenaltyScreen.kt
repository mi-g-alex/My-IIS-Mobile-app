package com.example.testschedule.presentation.account.penalty_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.penalty.PenaltyModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar

@Composable
fun PenaltyScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: PenaltyViewModel = hiltViewModel()
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
                title = stringResource(id = R.string.account_penalty_title),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty(),
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        if (viewModel.penalty.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                viewModel.penalty.value.sortedByDescending { i -> i.id }.forEach {
                    item {
                        PenaltyItem(item = it)
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.account_penalty_no_penalty),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}

@Composable
fun PenaltyItem(
    item: PenaltyModel
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = item.reason,
                style = MaterialTheme.typography.titleLarge
            )
            if (item.date.isNotEmpty())
                Text(
                    text = stringResource(id = R.string.account_penalty_date, item.status),
                    style = MaterialTheme.typography.bodyLarge
                )
            if (item.type.isNotEmpty())
                Text(
                    text = stringResource(id = R.string.account_penalty_type, item.type),
                    style = MaterialTheme.typography.bodyLarge
                )
            if (item.reason.isNotEmpty())
                Text(
                    text = stringResource(id = R.string.account_penalty_status, item.status),
                    style = MaterialTheme.typography.bodyLarge
                )
            if (item.note.isNotEmpty())
                Text(
                    text = stringResource(id = R.string.account_penalty_note, item.note),
                    style = MaterialTheme.typography.bodyLarge
                )
        }
    }
}

@Preview
@Composable
fun PCard() {
    PenaltyItem(
        item = PenaltyModel(
            id = 1,
            reason = "QWERTY",
            date = "21.12.2004",
            status = "asdfg",
            type = "12345",
            note = "zxcvb"
        )
    )
}
