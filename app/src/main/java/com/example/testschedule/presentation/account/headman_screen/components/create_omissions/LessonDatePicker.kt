package com.example.testschedule.presentation.account.headman_screen.components.create_omissions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.testschedule.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

@Composable
fun LessonsDatePicker(
    datePickerState: DatePickerState,
    onDateSelect: (date: Date) -> Unit
) {
    val cal = Calendar.getInstance().apply {
        GregorianCalendar(
            get(Calendar.YEAR),
            get(Calendar.MONTH),
            get(Calendar.DAY_OF_MONTH),
            0, 0, 0
        )
    }


    val calIsView = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        onDateSelect(Date(datePickerState.selectedDateMillis ?: 0))
        calIsView.value = false
    }

    Row(
        Modifier
            .fillMaxWidth()
            .clickable { calIsView.value = true }
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    datePickerState.selectedDateMillis =
                        (datePickerState.selectedDateMillis
                            ?: cal.timeInMillis) - 24 * 60 * 60 * 1000L

                },
                modifier = Modifier.fillMaxWidth(0.1f)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.account_headman_create_calendar_prev_day)
                )
            }

            Text(
                text = SimpleDateFormat("dd.MM.yyyy (EEE)", Locale.getDefault())
                    .format(Date(datePickerState.selectedDateMillis ?: 0)),
                modifier = Modifier
                    .padding(8.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.DateRange,
                stringResource(id = R.string.account_headman_create_calendar_select_date)
            )
            IconButton(
                onClick = {
                    datePickerState.selectedDateMillis = (
                        (datePickerState.selectedDateMillis
                            ?: cal.timeInMillis) + 24 * 60 * 60 * 1000L
                    )
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(id = R.string.account_headman_create_calendar_next_day)
                )
            }
        }


        if (calIsView.value) DatePickerDialog(
            onDismissRequest = { calIsView.value = false },
            confirmButton = {}
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }
}