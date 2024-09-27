package com.example.testschedule.presentation.account.study_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.material3.TextButton
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
import com.example.testschedule.domain.model.account.study.mark_sheet.MarkSheetModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.ListDataSection
import com.example.testschedule.presentation.account.additional_elements.SectionItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudyScreen(
    onBackPressed: () -> Unit,
    onLogOut: () -> Unit,
    goToCreateCertificate: () -> Unit,
    goToCreateMarkSheet: () -> Unit,
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

    LaunchedEffect(viewModel.cnt.intValue) { }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var collapseMarkSheet by remember { mutableStateOf(true) }
    var collapseCertificates by remember { mutableStateOf(true) }

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
    ) { padVal ->

        val listOfItem = mutableListOf<SectionItem>()

        val markSheets: MutableList<@Composable () -> Unit> = mutableListOf()
        viewModel.markSheets.forEachIndexed { index, markSheet ->
                if (index > 2 && collapseMarkSheet) return@forEachIndexed
                markSheets.add {
                    val textSuccess = stringResource(
                        id = R.string.account_study_mark_sheet_close_success,
                        markSheet.number
                    )
                    val textError = stringResource(
                        id = R.string.account_study_mark_sheet_close_error,
                        markSheet.number
                    )
                    MarkSheetItemView(
                        markSheet,
                    ) {
                        viewModel.closeMarkSheet(
                            markSheet.id,
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

        if (viewModel.markSheets.size > 3)
            markSheets.add {
                TextButton(onClick = { collapseMarkSheet = !collapseMarkSheet }) {
                    Icon(
                        if (collapseMarkSheet)
                            Icons.Outlined.KeyboardArrowDown
                        else
                            Icons.Outlined.KeyboardArrowUp,
                        null
                    )
                    Text(stringResource(id = if (!collapseMarkSheet) R.string.less else R.string.more))
                }
            }


        listOfItem += SectionItem(
            title = stringResource(id = R.string.account_study_mark_sheet_title),
            emptyText = stringResource(id = R.string.account_study_mark_sheet_no_yet),
            itemList = markSheets,
            icon = {
                FilledIconButton(onClick = { goToCreateCertificate() }) {
                    Icon(
                        Icons.Outlined.Add,
                        stringResource(id = R.string.account_study_certificates_create)
                    )
                }
            }
        )

        var certificates: MutableList<@Composable () -> Unit> = mutableListOf()

        viewModel.certificates.forEachIndexed { index, certificate ->
            if (index > 2 && collapseCertificates) return@forEachIndexed
            certificates.add {
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

        if (viewModel.certificates.size > 3) {
            certificates.add {
                TextButton(onClick = { collapseCertificates = !collapseCertificates }) {
                    Icon(
                        if (collapseCertificates)
                            Icons.Outlined.KeyboardArrowDown
                        else
                            Icons.Outlined.KeyboardArrowUp,
                        null
                    )
                    Text(stringResource(id = if (!collapseCertificates) R.string.less else R.string.more))
                }
            }
        }

        listOfItem += SectionItem(
            title = stringResource(id = R.string.account_study_certificates_title),
            emptyText = stringResource(id = R.string.account_study_certificates_no_yet),
            itemList = certificates,
            icon = {
                FilledIconButton(onClick = { goToCreateCertificate() }) {
                    Icon(
                        Icons.Outlined.Add,
                        stringResource(id = R.string.account_study_certificates_create)
                    )
                }
            }
        )

        ListDataSection(
            listOfItems = listOfItem,
            paddingValues = padVal
        )
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


@Composable
fun MarkSheetItemView(
    markSheet: MarkSheetModel,
    onCancelClicked: (id: Int) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
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
//                val icon = when (certificate.status) {
//                    1 -> Icons.Outlined.Done
//                    3 -> Icons.Outlined.Clear
//                    2 -> ImageVector.vectorResource(id = R.drawable.study_certificate_in_progressing)
//                    else -> Icons.Outlined.Warning
//                }

                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_number,
                        markSheet.number
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.0f)
                )

//                Icon(
//                    imageVector = icon,
//                    contentDescription = stringArrayResource(id = R.array.account_study_certificates_status_array)[certificate.status],
//                )
            }

            val statusId = when (markSheet.status) {
                "обрабатывается" -> 1
                "оплачена" -> 1
                "одобрена" -> 3
                "напечатана" -> 4
                "отклонена" -> 5
                else -> 0
            }
            val statusText =
                stringArrayResource(id = R.array.account_study_mark_sheet_status_array)[statusId]
            Text(
                text = stringResource(
                    id = R.string.account_study_mark_sheet_status,
                    statusText
                ),
                style = MaterialTheme.typography.bodyLarge,
            )

            if ((markSheet.subjectName + markSheet.subjectType).isNotBlank())
                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_subject,
                        markSheet.subjectName,
                        markSheet.subjectType
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )

            if (markSheet.employeeFIO.isNotBlank())
                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_employee,
                        markSheet.employeeFIO
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )

            if (markSheet.term != 0)
                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_term,
                        markSheet.term
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )

            Text(
                text = stringResource(
                    id = if (markSheet.isGoodReason) R.string.account_study_mark_sheet_reason_good else R.string.account_study_mark_sheet_reason_not_good,
                ),
                style = MaterialTheme.typography.bodyLarge
            )



            if (markSheet.absentDate.isNotBlank())
                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_skip_date,
                        markSheet.absentDate
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )

            if (markSheet.price > 0) {
                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_price,
                        markSheet.price.toString()
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (markSheet.createDate.isNotBlank()) {
                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_create_date,
                        markSheet.createDate
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (markSheet.rejectionReason.isNotBlank()) {
                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_reject_reason,
                        markSheet.rejectionReason
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (markSheet.status == "обрабатывается") {
                OutlinedButton(
                    onClick = { onCancelClicked(markSheet.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.account_study_mark_sheet_cancel_request))
                }
            }

        }
    }
}