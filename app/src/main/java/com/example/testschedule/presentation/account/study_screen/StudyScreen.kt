package com.example.testschedule.presentation.account.study_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testschedule.R
import com.example.testschedule.common.CacheUpdateKeys
import com.example.testschedule.domain.model.account.study.certificate.CertificateModel
import com.example.testschedule.domain.model.account.study.mark_sheet.MarkSheetModel
import com.example.testschedule.presentation.account.additional_elements.BasicTopBar
import com.example.testschedule.presentation.account.additional_elements.LastUpdateListItem
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
    val context = LocalContext.current
    val loginError = stringResource(id = R.string.error_to_login)
    var backEnabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val tabs = listOf(
        stringResource(id = R.string.account_study_mark_sheet_title),
        stringResource(id = R.string.account_study_certificates_title)
    )
    val pagerState = rememberPagerState { tabs.size }

    LaunchedEffect(viewModel.errorText.value) {
        if (viewModel.errorText.value == "WrongPassword") {
            Toast.makeText(context, loginError, Toast.LENGTH_LONG).show()
            onLogOut()
        }
    }
    LaunchedEffect(viewModel.cnt.intValue) { }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopBar(
                onBackPressed = {
                    onBackPressed()
                    backEnabled = false
                },
                title = stringResource(id = R.string.account_study_title),
                enabled = backEnabled,
                isLoading = viewModel.isLoading.value
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = if (pagerState.currentPage == 0) goToCreateMarkSheet else goToCreateCertificate
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(id = R.string.account_study_certificates_create)
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = title) }
                    )
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                if (page == 0) {
                    MarkSheetsTab(
                        markSheets = viewModel.markSheets,
                        onCancel = { markSheet ->
                            val successText = context.getString(
                                R.string.account_study_mark_sheet_close_success,
                                markSheet.number
                            )
                            val errorText = context.getString(
                                R.string.account_study_mark_sheet_close_error,
                                markSheet.number
                            )
                            viewModel.closeMarkSheet(
                                markSheet.id,
                                success = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = successText,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                error = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = errorText,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        }
                    )
                } else {
                    CertificatesTab(
                        certificates = viewModel.certificates,
                        onCancel = { certificate ->
                            val successText = context.getString(
                                R.string.account_study_certificates_close_success,
                                certificate.id
                            )
                            val errorText = context.getString(
                                R.string.account_study_certificates_close_error,
                                certificate.id
                            )
                            viewModel.closeCertificate(
                                certificate.id,
                                success = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = successText,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                error = {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = errorText,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MarkSheetsTab(
    markSheets: List<MarkSheetModel>,
    onCancel: (MarkSheetModel) -> Unit
) {
    if (markSheets.isEmpty()) {
        EmptyStudyState(stringResource(id = R.string.account_study_mark_sheet_no_yet))
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Spacer(Modifier.height(8.dp)) }
        items(markSheets, key = { it.id }) { markSheet ->
            MarkSheetItemView(markSheet = markSheet) { onCancel(markSheet) }
        }
        item { LastUpdateListItem(CacheUpdateKeys.STUDY) }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun CertificatesTab(
    certificates: List<CertificateModel>,
    onCancel: (CertificateModel) -> Unit
) {
    if (certificates.isEmpty()) {
        EmptyStudyState(stringResource(id = R.string.account_study_certificates_no_yet))
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Spacer(Modifier.height(8.dp)) }
        items(certificates, key = { it.id }) { certificate ->
            CertificateItemView(certificate = certificate) { onCancel(certificate) }
        }
        item { LastUpdateListItem(CacheUpdateKeys.STUDY) }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun EmptyStudyState(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CertificateItemView(
    certificate: CertificateModel,
    onCancelClicked: (id: Int) -> Unit
) {
    val statusItems = stringArrayResource(id = R.array.account_study_certificates_status_array)
    val statusText = statusItems.getOrElse(certificate.status) { statusItems.firstOrNull().orEmpty() }
    val certificateType = when (certificate.certificateType) {
        "обычная" -> stringResource(id = R.string.account_study_certificates_type_common)
        "гербовая" -> stringResource(id = R.string.account_study_certificates_type_herb)
        else -> certificate.certificateType
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StudyStatusBadge(text = statusText, color = certificateStatusColor(certificate.status))
                Text(
                    text = certificate.number.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = certificate.provisionPlace,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(id = R.string.account_study_certificates_type, certificateType),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
            StudyInfoText(stringResource(id = R.string.account_study_certificates_order_date, certificate.dateOrder))
            certificate.issueDate?.takeIf { it.isNotBlank() }?.let {
                StudyInfoText(stringResource(id = R.string.account_study_certificates_issue_date, it))
            }
            if (certificate.status == 3) {
                certificate.rejectionReason?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = stringResource(id = R.string.account_study_certificates_reject_reason, it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
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
    val statusId = when (markSheet.status) {
        "обрабатывается", "оплачена" -> 1
        "одобрена" -> 3
        "напечатана" -> 4
        "отклонена" -> 5
        else -> 0
    }
    val statuses = stringArrayResource(id = R.array.account_study_mark_sheet_status_array)
    val statusText = statuses.getOrElse(statusId) { statuses.firstOrNull().orEmpty() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StudyStatusBadge(text = statusText, color = markSheetStatusColor(markSheet.status))
                Text(
                    text = stringResource(id = R.string.account_study_mark_sheet_number, markSheet.number),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if ((markSheet.subjectName + markSheet.subjectType).isNotBlank()) {
                Text(
                    text = stringResource(
                        id = R.string.account_study_mark_sheet_subject,
                        markSheet.subjectName,
                        markSheet.subjectType
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            if (markSheet.employeeFIO.isNotBlank()) {
                Text(
                    text = stringResource(id = R.string.account_study_mark_sheet_employee, markSheet.employeeFIO),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
            if (markSheet.term != 0) {
                StudyInfoText(stringResource(id = R.string.account_study_mark_sheet_term, markSheet.term))
            }
            StudyInfoText(
                stringResource(
                    id = if (markSheet.isGoodReason) {
                        R.string.account_study_mark_sheet_reason_good
                    } else {
                        R.string.account_study_mark_sheet_reason_not_good
                    }
                )
            )
            if (markSheet.absentDate.isNotBlank()) {
                StudyInfoText(stringResource(id = R.string.account_study_mark_sheet_skip_date, markSheet.absentDate))
            }
            if (markSheet.price > 0) {
                StudyInfoText(stringResource(id = R.string.account_study_mark_sheet_price, markSheet.price.toString()))
            }
            if (markSheet.createDate.isNotBlank()) {
                StudyInfoText(stringResource(id = R.string.account_study_mark_sheet_create_date, markSheet.createDate))
            }
            if (markSheet.rejectionReason.isNotBlank()) {
                Text(
                    text = stringResource(id = R.string.account_study_mark_sheet_reject_reason, markSheet.rejectionReason),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
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

@Composable
private fun StudyStatusBadge(text: String, color: Color) {
    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun StudyInfoText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun markSheetStatusColor(status: String): Color = when (status) {
    "напечатана" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
    "отклонена" -> MaterialTheme.colorScheme.errorContainer
    "обрабатывается", "оплачена", "одобрена" -> Color(0xFFFFA726).copy(alpha = 0.2f)
    else -> MaterialTheme.colorScheme.surfaceVariant
}

@Composable
private fun certificateStatusColor(status: Int): Color = when (status) {
    1 -> Color(0xFF4CAF50).copy(alpha = 0.2f)
    2 -> Color(0xFFFFA726).copy(alpha = 0.2f)
    3 -> MaterialTheme.colorScheme.errorContainer
    else -> MaterialTheme.colorScheme.surfaceVariant
}
