package com.example.testschedule.presentation.account.menu_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.profile.AccountProfileModel

@Composable
fun AccountMenuScreen(
    goBack: () -> Unit,
    viewModel: AccountProfileViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val errorText = stringResource(id = R.string.error_to_login)
    LaunchedEffect(viewModel.errorText.value) {
        if(viewModel.errorText.value == "WrongPassword") {
            Toast.makeText(cnt, errorText, Toast.LENGTH_LONG).show()
            goBack()
        }
    }

    Scaffold(
        topBar = {
            AccountMenuTopAppBar(
                onCloseClicked = { goBack() }, onSettingsClicked = {}, onExitClicked = {}
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
        }
    ) {
        if (viewModel.userInfo.value != null)
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AccountMenuProfileCard(user = viewModel.userInfo.value!!)
            }
    }
}

@Composable
fun AccountMenuTopAppBar(
    onCloseClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onExitClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.account_menu_title)) },
        navigationIcon = {
            IconButton(onClick = { onCloseClicked() }) {
                Icon(
                    Icons.Outlined.Close,
                    stringResource(id = R.string.close)
                )
            }
        },
        actions = {
            IconButton(onClick = { onSettingsClicked() }) {
                Icon(
                    Icons.Outlined.Settings,
                    stringResource(id = R.string.account_menu_settings_desc)
                )
            }
            IconButton(onClick = { onExitClicked() }) {
                Icon(
                    Icons.Outlined.ExitToApp,
                    stringResource(id = R.string.account_menu_exit_desc)
                )
            }
        }
    )
}

@Composable
fun AccountMenuProfileCard(user: AccountProfileModel) {
    OutlinedCard(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                var showIcon by remember { mutableStateOf(true) }
                if (showIcon) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        null,
                        Modifier.size(48.dp)
                    )
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.photoUrl)
                        .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp),
                    onSuccess = { showIcon = false }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(
                        id = R.string.account_menu_main_card_spec,
                        user.faculty ?: "",
                        user.speciality ?: "",
                        user.course?.toString() ?: "-"
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun Prev() {
    AccountMenuProfileCard(
        user = AccountProfileModel(
            id = 1,
            lastName = "Gorgun",
            firstName = "Alexander",
            middleName = "Vitalievich",
            photoUrl = null,
            birthDate = "21.12.2004",
            group = "253501",
            faculty = "ФКСиС",
            speciality = "ИиТП",
            course = 2,
            rating = 5,
            bio = " ",
            references = emptyList(),
            skills = emptyList(),
            settingPublished = false,
            settingSearchJob = false,
            settingShowRating = false,
            outlookLogin = "23",
            outlookPassword = "23"
        )
    )
}