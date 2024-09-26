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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CreateCertificateScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: CreateCertificateViewModel = hiltViewModel()
) {
    val cnt = LocalContext.current
    val errorText = stringResource(id = R.string.error_to_login)
    var enabled by remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }


    var selectedPlace by remember { viewModel.selectedPlace }
    var noteText by remember { viewModel.noteText }
    var selectedPlaceType by remember { viewModel.selectedPlaceType }
    var isHerbOption by remember { viewModel.isHerbOption }
    var countOfCertificates by remember { viewModel.countOfCertificates }

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
                title = stringResource(id = R.string.account_study_certificates_create_title),
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
            item {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .menuAnchor(),
                        readOnly = true,
                        value = selectedPlace,
                        onValueChange = {},
                        label = { Text(stringResource(id = R.string.account_study_certificates_create_select_hint)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        viewModel.listOfPlaces.forEach { place ->
                            Text(
                                place.title,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            place.places.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.name.toString()) },
                                    onClick = {
                                        selectedPlace = it.name.toString()
                                        selectedPlaceType = it.type
                                        expanded = false
                                        isHerbOption = when (selectedPlaceType) {
                                            0 -> false
                                            else -> true
                                        }
                                        if (selectedPlaceType != 0) {
                                            noteText = ""
                                        }
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }

            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(id = R.string.account_study_certificates_create_herb))
                    },
                    supportingContent = {
                        Text(stringResource(id = R.string.account_study_certificates_create_herb_desc))
                    },
                    overlineContent = {
                        if (selectedPlaceType != 0)
                            Text(stringResource(id = R.string.account_study_certificates_create_herb_warn))
                    },
                    trailingContent = {
                        Switch(
                            checked = isHerbOption,
                            onCheckedChange = { isHerbOption = !isHerbOption },
                            enabled = selectedPlaceType == 0
                        )
                    },
                    modifier = Modifier.clickable {
                        if (selectedPlaceType == 0) {
                            isHerbOption = !isHerbOption
                        }
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
                        value = countOfCertificates.toFloat(),
                        onValueChange = { v -> countOfCertificates = v.toInt() },
                        valueRange = 1f..10f,
                        steps = 8
                    )
                    Text(
                        text = stringResource(
                            id = R.string.account_study_certificates_create_count,
                            countOfCertificates
                        ),
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { v -> noteText = v },
                    label = { Text(stringResource(id = R.string.account_study_certificates_create_note)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    enabled = selectedPlaceType == 0
                )
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
                            viewModel.createCertificate(
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
                        enabled = !viewModel.isLoading.value && selectedPlace.isNotEmpty()
                    ) {
                        Text(stringResource(id = R.string.account_study_certificates_create_send))
                    }
                }
            }
        }
    }
}