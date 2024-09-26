package com.example.testschedule.presentation.account.study_screen.create

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CreateMarkSheetScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: CreateMarkSheetViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val errorText = stringResource(id = R.string.error_to_login)
    var enabled by remember { mutableStateOf(true) }

    var expandedSubj by remember { mutableStateOf(false) }
    var expandedEmpl by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var selectedType by remember { viewModel.selectedType }
    var selectedSubject by remember { viewModel.selectedSubject }
    var isGoodReason by remember { viewModel.isGoodReason }
    var needAddDate by remember { viewModel.needAddDate }
    var hours by remember { viewModel.hours }
    var selectedLesson by remember { viewModel.selectedLesson }
    var selectedEmployee by remember { viewModel.selectedEmployee }

    var employeeText by remember { mutableStateOf("") }

    val calTmp = Calendar.getInstance()
    val cal = GregorianCalendar(
        calTmp.get(Calendar.YEAR),
        calTmp.get(Calendar.MONTH),
        calTmp.get(Calendar.DAY_OF_MONTH),
        3, 0, 0
    )

    val calIsView = remember {
        mutableStateOf(false)
    }

    val datePickerState = remember {
        DatePickerState(
            yearRange = ((cal.get(GregorianCalendar.YEAR) - 10)..(cal.get(
                GregorianCalendar.YEAR
            ) + 10)),
            initialDisplayedMonthMillis = cal.timeInMillis,
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = cal.timeInMillis,
            locale = Locale.getDefault()
        )
    }

    LaunchedEffect(viewModel.errorText.value) {
        if (viewModel.errorText.value == "WrongPassword") {
            Toast.makeText(cnt, errorText, Toast.LENGTH_LONG).show()
            onLogOut()
        }
    }

    LaunchedEffect(viewModel.allEmployees.size) {
        expandedEmpl = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopBar(
                onBackPressed = { onBackPressed(); enabled = false },
                title = stringResource(id = R.string.account_study_mark_sheet_create),
                enabled = enabled,
                isOfflineResult = viewModel.isLoading.value || viewModel.errorText.value.isNotEmpty(),
            )
            if (viewModel.isLoading.value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Выбор предмета
            item {
                ExposedDropdownMenuBox(
                    expanded = expandedSubj,
                    onExpandedChange = {
                        expandedSubj = !expandedSubj
                    }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .padding(8.dp),
                        readOnly = true,
                        value = if (selectedSubject != null && selectedLesson != null)
                            stringResource(
                                id = R.string.account_study_mark_sheet_create_select_subject,
                                selectedSubject!!.abbrev,
                                selectedSubject!!.term,
                                selectedLesson!!.abbrev
                            ) else "",
                        onValueChange = {},
                        label = { Text(stringResource(id = R.string.account_study_mark_sheet_create_select_subject_title)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubj) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )

                    DropdownMenu(
                        expanded = expandedSubj,
                        onDismissRequest = {
                            expandedSubj = false
                        }
                    ) {
                        viewModel.subjectsList.forEach { subject ->
                            subject.lessonTypes.forEach { lesson ->
                                val text = stringResource(
                                    id = R.string.account_study_mark_sheet_create_select_subject,
                                    subject.abbrev,
                                    subject.term,
                                    lesson.abbrev
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(text)
                                    },
                                    onClick = {
                                        selectedSubject = subject
                                        selectedLesson = lesson
                                        expandedSubj = false
                                        selectedEmployee = null
                                        employeeText = ""
                                        viewModel.getSpecEmployee()
                                        selectedType = viewModel.typesList.find { h ->
                                            h.isExam == selectedLesson?.isExam &&
                                                    h.isCourseWork == selectedLesson?.isCourseWork &&
                                                    h.isOffset == selectedLesson?.isOffset &&
                                                    h.isRemote == selectedLesson?.isRemote &&
                                                    h.isLab == selectedLesson?.isLab
                                        }

                                        hours = selectedType?.coefficient ?: 1.0
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }

            // Поиск препода
            item {
                ExposedDropdownMenuBox(
                    expanded = expandedEmpl,
                    onExpandedChange = { expandedEmpl = !expandedEmpl }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = employeeText,
                        onValueChange = { t ->
                            employeeText = t
                            expandedEmpl = true
                        },
                        label = { Text(stringResource(id = R.string.account_study_mark_sheet_create_select_employee_title)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEmpl) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    )

                    ExposedDropdownMenu(
                        expanded = expandedEmpl,
                        onDismissRequest = {
                            expandedEmpl = false
                        }
                    ) {
                        if (viewModel.specEmployees.any { f ->
                                f.fio.startsWith(employeeText)
                            }) {
                            Text(stringResource(id = R.string.account_study_mark_sheet_create_select_employee_recommended))
                            viewModel.specEmployees.filter { f ->
                                f.fio.startsWith(employeeText)
                            }.forEach { employee ->
                                DropdownMenuItem(
                                    text = {
                                        Text(employee.fio)
                                    },
                                    onClick = {
                                        selectedEmployee = employee
                                        employeeText = employee.fio
                                        expandedEmpl = false

                                        hours = viewModel.typesList.find { h ->
                                            h.isExam == selectedLesson?.isExam &&
                                                    h.isCourseWork == selectedLesson?.isCourseWork &&
                                                    h.isOffset == selectedLesson?.isOffset &&
                                                    h.isRemote == selectedLesson?.isRemote &&
                                                    h.isLab == selectedLesson?.isLab
                                        }?.coefficient ?: 1.0
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                        if (viewModel.allEmployees.filter { f ->
                                f.fio.startsWith(employeeText) &&
                                        !viewModel.specEmployees.contains(f)
                            }.size in (1..20)) {
                            Text(stringResource(id = R.string.account_study_mark_sheet_create_select_employee_other))
                            viewModel.allEmployees.filter { f ->
                                f.fio.startsWith(employeeText) && !viewModel.specEmployees.contains(
                                    f
                                )
                            }.forEach { employee ->
                                DropdownMenuItem(
                                    text = {
                                        Text(employee.fio)
                                    },
                                    onClick = {
                                        selectedEmployee = employee
                                        employeeText = employee.fio
                                        expandedEmpl = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }

            // Уваж причина
            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(id = R.string.account_study_mark_sheet_create_is_good_reason))
                    },
                    supportingContent = {
                        Text(
                            stringResource(
                                id = R.string.account_study_mark_sheet_create_is_good_reason_selected,
                                stringResource(
                                    id = if (isGoodReason) R.string.account_study_mark_sheet_reason_good else R.string.account_study_mark_sheet_reason_not_good
                                )
                            )
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = isGoodReason,
                            onCheckedChange = {
                                isGoodReason = !isGoodReason; if (isGoodReason) needAddDate = true
                            }
                        )
                    },
                    modifier = Modifier.clickable {
                        isGoodReason = !isGoodReason
                    },
                )
            }

            // Дата
            item {

                OutlinedTextField(
                    readOnly = true,
                    value = SimpleDateFormat(
                        "dd.MM.yyyy", Locale.getDefault()
                    ).format(Date(datePickerState.selectedDateMillis!!)),
                    onValueChange = {},
                    modifier = Modifier
                        .clickable {
                            calIsView.value = true
                        }
                        .fillMaxWidth()
                        .padding(8.dp),
                    trailingIcon = {
                        IconButton(onClick = { calIsView.value = true }) {

                            Icon(Icons.Filled.DateRange, null, Modifier.clickable {
                                calIsView.value = true
                            })
                        }
                    })


                if (calIsView.value) DatePickerDialog(onDismissRequest = {
                    calIsView.value = false
                },
                    confirmButton = {
                        TextButton(onClick = { calIsView.value = false }) {
                            Text("Выбрать")
                        }
                    }) {
                    DatePicker(state = datePickerState, showModeToggle = false)
                }
            }

            // Прикреплять дату?
            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(id = R.string.account_study_mark_sheet_create_add_date))
                    },
                    trailingContent = {
                        Checkbox(
                            checked = needAddDate,
                            onCheckedChange = { needAddDate = !needAddDate },
                            enabled = isGoodReason
                        )
                    },
                    modifier = Modifier.clickable {
                        if (!isGoodReason)
                            needAddDate = !needAddDate
                    },
                )
            }


            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Slider(
                        value = hours.toFloat(),
                        onValueChange = { v -> hours = v.toDouble() },
                        valueRange = if (selectedLesson?.isLab == true) (1f..4f) else (0f..1f),
                        steps = if (selectedLesson?.isLab == true) 2 else 0,
                        enabled = selectedLesson?.isLab == true
                    )
                    Text(
                        text = stringResource(
                            id = R.string.account_study_mark_sheet_create_count_hours,
                            hours.toString()
                        ),
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        stringResource(
                            id = R.string.account_study_mark_sheet_price,
                            "~" + viewModel.calcPrice().toString()
                        )
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    val createErrorText =
                        stringResource(id = R.string.account_study_certificates_create_error)
                    Button(
                        onClick = {
                            viewModel.createMarkSheet(
                                date = SimpleDateFormat(
                                    "dd.MM.yyyy", Locale.getDefault()
                                ).format(Date(datePickerState.selectedDateMillis!!)),
                                onSuccess = {
                                    onBackPressed()
                                },
                                onError = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            createErrorText,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        },
                        enabled = !viewModel.isLoading.value && selectedLesson != null && selectedEmployee != null && hours > 0
                    ) {
                        Text(stringResource(id = R.string.account_study_mark_sheet_create_send))
                    }
                }
            }
        }
    }
}