package com.example.testschedule.presentation.account.headman_screen.components.create_omissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.headman.create_omissions.HeadmanGetOmissionsModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LessonScheduleInfoSheet(
    nameAbbrev: String,
    lessonTypeAbbrev: String,
    subGroup: Int,
    lessonPeriod: HeadmanGetOmissionsModel.LessonModel.LessonPeriodModel,
    scheduleInfo: HeadmanGetOmissionsModel.LessonModel.ScheduleInfoModel?,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val subgroups = stringArrayResource(id = R.array.subgroups)
    val noData = stringResource(R.string.account_headman_schedule_no_data)
    val subjectTitle = scheduleInfo?.subjectFullName?.takeIf { it.isNotBlank() } ?: nameAbbrev

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.account_headman_schedule_info_title),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = subjectTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            scheduleInfo?.subjectAbbrev
                ?.takeIf { it.isNotBlank() && it != subjectTitle }
                ?.let { abbrev ->
                    Text(
                        text = abbrev,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            HorizontalDivider()

            ScheduleInfoRow(
                label = stringResource(R.string.account_headman_schedule_lesson_type),
                value = lessonTypeAbbrev
            )
            ScheduleInfoRow(
                label = stringResource(R.string.account_headman_schedule_time),
                value = if (lessonPeriod.startTime.isBlank()) {
                    stringResource(
                        R.string.account_headman_create_lesson_hours,
                        lessonPeriod.lessonPeriodHours
                    )
                } else {
                    stringResource(
                        R.string.account_headman_create_lesson_time,
                        lessonPeriod.startTime,
                        lessonPeriod.endTime,
                        lessonPeriod.lessonPeriodHours
                    )
                }
            )
            ScheduleInfoRow(
                label = stringResource(R.string.account_headman_schedule_subgroup),
                value = subgroups.getOrElse(subGroup) { subGroup.toString() }
            )
            ScheduleInfoRow(
                label = stringResource(R.string.account_headman_schedule_auditories),
                value = scheduleInfo?.auditories
                    ?.takeIf { it.isNotEmpty() }
                    ?.joinToString(", ")
                    ?: noData
            )

            Text(
                text = stringResource(R.string.account_headman_schedule_teachers),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            val teachers = scheduleInfo?.teacherFullNames.orEmpty()
            if (teachers.isEmpty()) {
                Text(text = noData, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                teachers.forEach { teacher -> Text(text = teacher) }
            }
        }
    }
}

@Composable
private fun ScheduleInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f)
        )
    }
}
