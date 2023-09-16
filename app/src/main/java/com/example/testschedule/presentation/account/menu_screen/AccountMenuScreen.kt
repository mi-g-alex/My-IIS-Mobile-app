package com.example.testschedule.presentation.account.menu_screen

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.profile.AccountProfileModel

@Composable
fun AccountMenuScreen(
    goBack: () -> Unit,
    goToNotifications: () -> Unit,
    goToDormitory: () -> Unit,
    goToGroup: () -> Unit,
    goToMarkBook: () -> Unit,
    goToOmissions: () -> Unit,
    goToPenalty: () -> Unit,
    goToAnnouncements: () -> Unit,
    viewModel: AccountProfileViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val errorText = stringResource(id = R.string.error_to_login)
    LaunchedEffect(viewModel.errorText.value) {
        if (viewModel.errorText.value == "WrongPassword") {
            Toast.makeText(cnt, errorText, Toast.LENGTH_LONG).show()
            goBack()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getNotifications()
    }

    Scaffold(
        topBar = {
            AccountMenuTopAppBar(
                onCloseClicked = { goBack() },
                onSettingsClicked = {},
                onExitClicked = { viewModel.exit(); goBack() },
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty(),
                onNotificationClicked = goToNotifications,
                notificationCount = viewModel.notificationsCount.intValue
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
        }
    ) {
        if (viewModel.userInfo.value != null)
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item { AccountMenuProfileCard(user = viewModel.userInfo.value!!) }
                /*item {
                    MenuNotificationItem(
                        icon = R.drawable.acc_menu_notifications,
                        title = stringResource(id = R.string.account_menu_card_notifications),
                        number = viewModel.notificationsCount.intValue
                    ) { goToNotifications() }
                }*/
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item {
                    MenuItem(
                        R.drawable.acc_menu_markbook,
                        stringResource(id = R.string.account_menu_card_markbook),
                    ) { goToMarkBook() }
                }
                item {
                    MenuItem(
                        R.drawable.acc_menu_study,
                        stringResource(id = R.string.account_menu_card_study),
                        {})
                }
                if (viewModel.basicInfo.value?.isGroupHead == true) {
                    item {
                        MenuItem(
                            R.drawable.acc_menu_headman,
                            stringResource(id = R.string.account_menu_card_headman),
                            {})
                    }
                }
                item {
                    MenuItem(
                        R.drawable.acc_menu_rating,
                        stringResource(id = R.string.account_menu_card_rating),
                        {})
                }
                item {
                    MenuItem(
                        R.drawable.acc_menu_omissions,
                        stringResource(id = R.string.account_menu_card_omissions),
                    ) { goToOmissions() }
                }
                item {
                    MenuItem(
                        R.drawable.acc_menu_group,
                        stringResource(id = R.string.account_menu_card_group),
                    ) { goToGroup() }
                }
                item {
                    MenuItem(
                        R.drawable.acc_menu_announcements,
                        stringResource(id = R.string.account_menu_card_announcements),
                        ) { goToAnnouncements() }
                }
                item {
                    MenuItem(
                        R.drawable.acc_menu_graduate,
                        stringResource(id = R.string.account_menu_card_graduate),
                        {})
                }
                item {
                    MenuItem(
                        R.drawable.acc_menu_dormitory,
                        stringResource(id = R.string.account_menu_card_dormitory),
                    ) { goToDormitory() }
                }
                item {
                    MenuItem(
                        R.drawable.acc_menu_penalty,
                        stringResource(id = R.string.account_menu_card_penalty),
                    ) { goToPenalty() }
                }
            }
    }
}

@Composable
fun AccountMenuTopAppBar(
    onCloseClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onExitClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    notificationCount: Int,
    isOfflineResult: Boolean
) {
    val cnt = LocalContext.current
    val toastText = stringResource(id = R.string.offline_mode_desc)
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
            if (isOfflineResult) {
                IconButton(onClick = {
                    Toast.makeText(cnt, toastText, Toast.LENGTH_LONG).show()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.offline_mode),
                        contentDescription = stringResource(id = R.string.offline_mode_desc)
                    )
                }
            }
            IconButton(onClick = { onExitClicked() }) {
                Icon(
                    Icons.Outlined.ExitToApp,
                    stringResource(id = R.string.account_menu_exit_desc)
                )
            }
            IconButton(onClick = { onSettingsClicked() }) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(id = R.string.account_menu_settings_desc),
                )
            }
            IconButton(onClick = { onNotificationClicked() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.acc_menu_notifications),
                        contentDescription = stringResource(id = R.string.account_menu_card_notifications)
                    )
                    if (notificationCount != 0)
                        Text(
                            notificationCount.toString(),
                            style = MaterialTheme.typography.titleSmall
                        )
                }
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
            .padding(8.dp)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuItem(
    icon: Int,
    title: String,
    navTo: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navTo()
            },
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.padding(8.dp)
            )
        },
        text = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailing = {
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = title,
            )
        }
    )
}

/*@Composable
fun MenuNotificationItem(
    icon: Int,
    title: String,
    number: Int,
    navTo: () -> Unit
) {
    OutlinedCard(
        onClick = { navTo() },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (number != 0)
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(end = 16.dp, start = 8.dp)
                )
        }
    }
}*/
