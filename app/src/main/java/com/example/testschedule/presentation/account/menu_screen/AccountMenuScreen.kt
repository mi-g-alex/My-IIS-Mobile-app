package com.example.testschedule.presentation.account.menu_screen

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.testschedule.R
import com.example.testschedule.common.CacheUpdateKeys
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.presentation.LoadingTopBar
import com.example.testschedule.presentation.account.additional_elements.LastUpdateListItem

@Composable
fun AccountMenuScreen(
    goBack: () -> Unit,
    goToNotifications: () -> Unit,
    goToDormitory: () -> Unit,
    goToGroup: () -> Unit,
    goToMarkBook: () -> Unit,
    goToOmissions: () -> Unit,
    goToPenalty: () -> Unit,
    goToRating: () -> Unit,
    goToStudy: () -> Unit,
    goToSettings: () -> Unit,
    goToHeadman: () -> Unit,
    viewModel: AccountProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val loginError = stringResource(id = R.string.error_to_login)

    LaunchedEffect(viewModel.errorText.value) {
        if (viewModel.errorText.value == "WrongPassword") {
            Toast.makeText(context, loginError, Toast.LENGTH_LONG).show()
            goBack()
        }
    }
    LaunchedEffect(Unit) { viewModel.getNotifications() }

    Scaffold(
        topBar = {
            AccountMenuTopAppBar(
                onCloseClicked = goBack,
                isLoading = viewModel.isLoading.value
            )
        }
    ) { innerPadding ->
        viewModel.userInfo.value?.let { user ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item { AccountMenuProfileCard(user) }
                item { HorizontalDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.AutoMirrored.Filled.LibraryBooks,
                        title = stringResource(id = R.string.account_menu_card_markbook),
                        onClick = goToMarkBook
                    )
                }
                item { AccountMenuDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.Filled.School,
                        title = stringResource(id = R.string.account_menu_card_study),
                        onClick = goToStudy
                    )
                }
                if (viewModel.basicInfo.value?.isGroupHead == true || viewModel.basicInfo.value?.canStudentNote == true) {
                    item { AccountMenuDivider() }
                    item {
                        AccountMenuRow(
                            icon = Icons.Filled.Star,
                            title = stringResource(id = R.string.account_menu_card_headman),
                            onClick = goToHeadman
                        )
                    }
                }
                item { AccountMenuDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.Filled.Leaderboard,
                        title = stringResource(id = R.string.account_menu_card_rating),
                        onClick = goToRating
                    )
                }
                item { AccountMenuDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.Filled.EventBusy,
                        title = stringResource(id = R.string.account_menu_card_omissions),
                        onClick = goToOmissions
                    )
                }
                item { AccountMenuDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.Filled.Groups,
                        title = stringResource(id = R.string.account_menu_card_group),
                        onClick = goToGroup
                    )
                }
                item { AccountMenuDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.Filled.Apartment,
                        title = stringResource(id = R.string.account_menu_card_dormitory),
                        onClick = goToDormitory
                    )
                }
                item { AccountMenuDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.Filled.Gavel,
                        title = stringResource(id = R.string.account_menu_card_penalty),
                        onClick = goToPenalty
                    )
                }
                item { AccountMenuDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.Outlined.Notifications,
                        title = stringResource(id = R.string.account_menu_card_notifications),
                        badge = viewModel.notificationsCount.intValue,
                        onClick = goToNotifications
                    )
                }
                item { AccountMenuDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.Filled.Settings,
                        title = stringResource(id = R.string.account_menu_settings_desc),
                        onClick = goToSettings
                    )
                }
                item { HorizontalDivider() }
                item {
                    AccountMenuRow(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = stringResource(id = R.string.account_menu_exit_desc),
                        tint = MaterialTheme.colorScheme.error,
                        onClick = {
                            viewModel.exit()
                            goBack()
                        }
                    )
                }
                item { LastUpdateListItem(CacheUpdateKeys.PROFILE) }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun AccountMenuTopAppBar(
    onCloseClicked: () -> Unit,
    isLoading: Boolean
) {
    LoadingTopBar(isLoading = isLoading) {
        TopAppBar(
            title = { Text(stringResource(id = R.string.account_menu_title)) },
            navigationIcon = {
                IconButton(onClick = onCloseClicked) {
                    Icon(Icons.Outlined.Close, stringResource(id = R.string.close))
                }
            }
        )
    }
}

@Composable
private fun AccountMenuProfileCard(user: AccountProfileModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val photoModifier = Modifier
            .size(88.dp)
            .clip(CircleShape)
        var showFallback by remember(user.photoUrl) { mutableStateOf(true) }
        Box(modifier = photoModifier, contentAlignment = Alignment.Center) {
            if (showFallback) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            SubcomposeAsyncImage(
                model = user.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = photoModifier,
                onSuccess = { showFallback = false },
                onError = { showFallback = true }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = listOfNotNull(user.firstName, user.lastName)
                .filter { it.isNotBlank() }
                .joinToString(" "),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        val groupAndCourse = buildString {
            user.group?.takeIf { it.isNotBlank() }?.let { append(it) }
            user.course?.let {
                if (isNotEmpty()) append(" · ")
                append(it)
            }
        }
        if (groupAndCourse.isNotBlank()) {
            Text(
                text = groupAndCourse,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val specialityAndFaculty = listOfNotNull(user.speciality, user.faculty)
            .filter { it.isNotBlank() }
            .joinToString(", ")
        if (specialityAndFaculty.isNotBlank()) {
            Text(
                text = specialityAndFaculty,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AccountMenuRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    badge: Int = 0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (tint == MaterialTheme.colorScheme.error) tint else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        if (badge > 0) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = badge.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun AccountMenuDivider() {
    HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
}
