package com.example.testschedule.presentation.account.headman_screen.components.create_omissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.testschedule.R
import com.example.testschedule.presentation.account.headman_screen.HeadmanViewModel

@Composable
fun SetHoursItem(
    padVal: PaddingValues,
    viewModel: HeadmanViewModel,
    isSortByLesson: Boolean
) {

    LaunchedEffect(viewModel.cnt.intValue) {}

    Column(modifier = Modifier.padding(padVal)) {

        LessonsDatePicker { date ->
            viewModel.getOmissionsByDate(date)
        }

        val selectOmission: (lessonId: Int, userId: Int, hours: Int?) -> Unit =
            { lessonId, userId, hours ->
                if (viewModel.selectedOmissions[lessonId] == null) {
                    viewModel.selectedOmissions[lessonId] = mutableMapOf()
                }
                if (hours == null || hours == 0) {
                    viewModel.selectedOmissions[lessonId]?.remove(userId)
                    if (viewModel.selectedOmissions[lessonId]?.isEmpty() == true) {
                        viewModel.selectedOmissions.remove(lessonId)
                    }
                    viewModel.cnt.intValue--
                } else {
                    viewModel.selectedOmissions[lessonId]?.set(userId, hours)
                    viewModel.cnt.intValue++
                }
            }

        val checkCheck: (lessonId: Int, userId: Int) -> ToggleableState =
            { lessonId: Int, userId: Int ->
                if (viewModel.lessonsList.first { it.id == lessonId }.students.first { it.id == userId }.omission != null ||
                    viewModel.selectedOmissions[lessonId]?.get(userId) ==
                    viewModel.lessonsList.first { it.id == lessonId }.lessonPeriod.lessonPeriodHours
                ) {
                    ToggleableState.On
                } else {
                    if (viewModel.selectedOmissions[lessonId]?.get(userId) == null) {
                        ToggleableState.Off
                    } else {
                        ToggleableState.Indeterminate
                    }
                }
            }

        if (!viewModel.isLoading.value && viewModel.errorText.value.isEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {

                if (viewModel.lessonsList.isEmpty()) {
                    item {
                        Text(
                            stringResource(id = R.string.account_headman_create_no_lessons),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                item {
                    if (isSortByLesson)
                        ViewOmissionsByLesson(
                            viewModel.lessonsList,
                            hours = viewModel.selectedOmissions,
                            selectOmission = selectOmission,
                            checkCheck = checkCheck
                        )
                    else
                        ViewOmissionsByStudent(
                            omissions = viewModel.lessonsList,
                            hours = viewModel.selectedOmissions,
                            selectOmission = selectOmission,
                            checkCheck = checkCheck
                        )
                }
            }
        }
    }
}