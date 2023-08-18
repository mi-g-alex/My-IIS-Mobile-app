package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testschedule.R
import com.example.testschedule.domain.model.auth.UserBasicDataModel

@Composable
fun ExamsFAB(
    goToExams: () -> Unit,
    state: LazyListState
) {
    AnimatedVisibility(
        visible = state.isScrollingUp().value,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight * 2 }
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight * 2 }
        )
    ) {
        ExtendedFloatingActionButton(onClick = { goToExams() }) {
            Text(
                stringResource(id = R.string.schedule_exams_fab),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun MyTopAppBar(
    titleText: String,
    navIconClicked: () -> Unit,
    goToAuth: () -> Unit,
    isOfflineResult: Boolean,
    userData: MutableState<UserBasicDataModel?>,
    goToProfile: () -> Unit,
    isPrev: Boolean = false
) {
    val cnt = LocalContext.current
    val toastText = stringResource(id = R.string.offline_mode_desc)
    CenterAlignedTopAppBar(
        title = {
            Text(
                titleText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if(!isPrev) IconButton(onClick = navIconClicked) {
                Icon(
                    Icons.Outlined.Menu,
                    stringResource(id = R.string.schedule_open_list_of_saved_desc)
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
            if(!isPrev) IconButton(onClick = {
                if(userData.value != null) {
                    goToProfile()
                } else {
                    goToAuth()
                }
            }) {
                var showIcon by remember { mutableStateOf(true) }
                if (showIcon) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        stringResource(id = R.string.login_to_account_button)
                    )
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userData.value?.photoUrl)
                        .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp),
                    onSuccess = { showIcon = false }
                )
            }
        }
    )
}