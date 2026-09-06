package com.example.testschedule.presentation.account.headman_screen.components.create_omissions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TextButton
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                datePickerState.selectedDateMillis =
                    (datePickerState.selectedDateMillis ?: cal.timeInMillis) - 24 * 60 * 60 * 1000L
            }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.account_headman_create_calendar_prev_day),
                modifier = Modifier.size(28.dp)
            )
        }
        Row(
            modifier = Modifier.clickable { calIsView.value = true }.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Filled.DateRange,
                stringResource(id = R.string.account_headman_create_calendar_select_date),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = SimpleDateFormat("dd.MM.yyyy (EEE)", Locale.getDefault())
                    .format(Date(datePickerState.selectedDateMillis ?: 0)),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        IconButton(
            onClick = {
                datePickerState.selectedDateMillis =
                    (datePickerState.selectedDateMillis ?: cal.timeInMillis) + 24 * 60 * 60 * 1000L
            }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.account_headman_create_calendar_next_day),
                modifier = Modifier.size(28.dp)
            )
        }
    }

    if (calIsView.value) DatePickerDialog(
        onDismissRequest = { calIsView.value = false },
        confirmButton = {
            TextButton(onClick = { calIsView.value = false }) {
                Text(stringResource(id = android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { calIsView.value = false }) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState, showModeToggle = false)
    }
}
