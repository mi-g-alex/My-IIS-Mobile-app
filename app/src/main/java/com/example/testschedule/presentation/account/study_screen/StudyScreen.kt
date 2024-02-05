package com.example.testschedule.presentation.account.study_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudyScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    goToCreateCertificate: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {

    val cnt = LocalContext.current
    val errorText = stringResource(id = R.string.error_to_login)
    var enabled by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel.errorText.value) {
        if (viewModel.errorText.value == "WrongPassword") {
            Toast.makeText(cnt, errorText, Toast.LENGTH_LONG).show()
            onLogOut()
        }
    }

    LaunchedEffect(viewModel.cnt.value) { }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopBar(
                onBackPressed = { onBackPressed(); enabled = false },
                title = stringResource(id = R.string.account_study_title),
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

            stickyHeader {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(id = R.string.account_study_certificates_title),
                            style = MaterialTheme.typography.displaySmall
                        )

                        Button(onClick = {
                            goToCreateCertificate()
                        }
                        ) {
                            Text(stringResource(id = R.string.account_study_certificates_create))
                        }
                    }
                }
            }

            if (viewModel.certificates.isNotEmpty()) {
                viewModel.certificates.forEach { certificate ->
                    item {
                        val textSuccess = stringResource(
                            id = R.string.account_study_certificates_close_success,
                            certificate.id
                        )
                        val textError = stringResource(
                            id = R.string.account_study_certificates_close_error,
                            certificate.id
                        )
                        CertificateItemView(
                            certificate = certificate,
                        ) {
                            viewModel.closeCertificate(
                                certificate.id,
                                success = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = textSuccess,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                error = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = textError,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                item {
                    Text(
                        stringResource(id = R.string.account_study_certificates_no_yet)
                    )
                }
            }
        }
    }
}

@Composable
fun CertificateItemView(
    certificate: CertificateModel,
    onCancelClicked: (id: Int) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val icon = when (certificate.status) {
                    1 -> Icons.Outlined.Done
                    3 -> Icons.Outlined.Clear
                    2 -> ImageVector.vectorResource(id = R.drawable.study_certificate_in_progressing)
                    else -> Icons.Outlined.Warning
                }

                Text(
                    text = stringResource(
                        id = R.string.account_study_certificates_number,
                        certificate.number,
                        certificate.provisionPlace
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.0f)
                )

                Icon(
                    imageVector = icon,
                    contentDescription = stringArrayResource(id = R.array.account_study_certificates_status_array)[certificate.status],
                )
            }

            Text(
                text = stringResource(
                    id = R.string.account_study_certificates_status,
                    stringArrayResource(id = R.array.account_study_certificates_status_array)[certificate.status]
                ),
                style = MaterialTheme.typography.bodyLarge,
            )

            val certificateType = when (certificate.certificateType) {
                "обычная" -> stringResource(id = R.string.account_study_certificates_type_common)
                "гербовая" -> stringResource(id = R.string.account_study_certificates_type_herb)
                else -> certificate.certificateType
            }

            Text(
                text = stringResource(
                    id = R.string.account_study_certificates_type,
                    certificateType
                ),
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = stringResource(
                    id = R.string.account_study_certificates_order_date,
                    certificate.dateOrder
                ),
                style = MaterialTheme.typography.bodyLarge
            )

            if (certificate.issueDate?.isNotBlank() == true)
                Text(
                    text = stringResource(
                        id = R.string.account_study_certificates_issue_date,
                        certificate.issueDate
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )

            if (certificate.status == 3 && certificate.rejectionReason?.isNotBlank() == true) {
                Text(
                    text = stringResource(
                        id = R.string.account_study_certificates_reject_reason,
                        certificate.rejectionReason
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (certificate.status == 2) {
                OutlinedButton(
                    onClick = { onCancelClicked(certificate.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.account_study_certificates_cancel_request))
                }
            }

        }
    }
}