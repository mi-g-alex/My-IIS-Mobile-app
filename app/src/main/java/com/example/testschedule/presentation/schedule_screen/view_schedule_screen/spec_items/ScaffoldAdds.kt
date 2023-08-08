package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.spec_items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.testschedule.R

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
                stringResource(id = R.string.exams_fab),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun MyTopAppBar(
    titleText: String,
    navIconClicked: () -> Unit,
    actionIconClicked: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                titleText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = navIconClicked) {
                Icon(
                    Icons.Default.Menu,
                    stringResource(id = R.string.open_list_of_saved_schedule_desc)
                )
            }
        },
        actions = {
            Row {
                IconButton(onClick = actionIconClicked) {
                    Icon(
                        Icons.Default.Favorite,
                        stringResource(id = R.string.open_list_of_saved_schedule_desc)
                    )
                }
            }
        }
    )
}