package com.example.testschedule.presentation.schedule_screen.add_schedule_screen.spec_items

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.testschedule.R
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel

@Composable
fun MoreDetailsAboutSchedule(
    closeDialog: () -> Unit,
    isGroup: Boolean,
    dataOfGroup: ListOfGroupsModel?,
    dataOfEmployee: ListOfEmployeesModel?,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = closeDialog) {
        when {
            isGroup && dataOfGroup != null -> GroupInfoSheet(
                group = dataOfGroup,
                isFavorite = isFavorite,
                onFavoriteToggle = onFavoriteToggle
            )

            !isGroup && dataOfEmployee != null -> EmployeeInfoSheet(
                employee = dataOfEmployee,
                isFavorite = isFavorite,
                onFavoriteToggle = onFavoriteToggle
            )
        }
    }
}

@Composable
private fun GroupInfoSheet(
    group: ListOfGroupsModel,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(group.name, style = MaterialTheme.typography.headlineSmall)
        if (group.course > 0) {
            Text(
                stringResource(R.string.schedule_ui_course, group.course),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        if (group.facultyAbbrev.isNotBlank()) {
            InfoDetailRow(stringResource(R.string.schedule_ui_faculty), group.facultyAbbrev)
        }
        if (group.specialityName.isNotBlank()) {
            InfoDetailRow(stringResource(R.string.schedule_ui_speciality), group.specialityName)
        }
        if (group.specialityAbbrev.isNotBlank()) {
            InfoDetailRow(stringResource(R.string.schedule_ui_code), group.specialityAbbrev)
        }
        SheetFavoriteButton(isFavorite, onFavoriteToggle)
        CalendarButton {
            openGoogleCalendar(
                context,
                "webcal://iis.bsuir.by/api/v1/iCal/schedule?group=${group.name}"
            )
        }
    }
}

@Composable
private fun EmployeeInfoSheet(
    employee: ListOfEmployeesModel,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    val context = LocalContext.current
    val fullName = buildString {
        append(employee.lastName)
        if (employee.firstName.isNotBlank()) append(" ${employee.firstName}")
        employee.middleName?.takeIf(String::isNotBlank)?.let { append(" $it") }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmployeePhoto(employee.photoLink)
        Spacer(Modifier.size(4.dp))
        Text(
            fullName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth()
        )
        if (!employee.rank.isNullOrBlank()) {
            Text(
                employee.rank,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        employee.degree?.takeIf(String::isNotBlank)?.let {
            InfoDetailRow(stringResource(R.string.schedule_ui_degree), it)
        }
        employee.academicDepartment.forEach { department ->
            InfoDetailRow(stringResource(R.string.schedule_ui_department), department)
        }
        SheetFavoriteButton(isFavorite, onFavoriteToggle)
        CalendarButton {
            openGoogleCalendar(
                context,
                "webcal://iis.bsuir.by/api/v1/iCal/schedule?employeeUrl=${employee.urlId}"
            )
        }
    }
}

@Composable
private fun InfoDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.35f)
        )
        Text(value, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.65f))
    }
}

@Composable
private fun SheetFavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFavorite) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            },
            contentColor = if (isFavorite) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            }
        )
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            if (isFavorite) {
                stringResource(R.string.schedule_ui_remove_favorite)
            } else {
                stringResource(R.string.schedule_ui_add_favorite)
            }
        )
    }
}

@Composable
private fun CalendarButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Icon(
            Icons.Default.CalendarMonth,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(R.string.schedule_ui_add_calendar))
    }
}

@Composable
private fun EmployeePhoto(photoLink: String?) {
    SubcomposeAsyncImage(
        model = photoLink,
        contentDescription = null,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    ) {
        if (painter.state is AsyncImagePainter.State.Success) {
            SubcomposeAsyncImageContent()
        } else {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

private fun openGoogleCalendar(context: Context, calendarId: String) {
    val uri = Uri.parse("https://calendar.google.com/calendar/r?cid=${Uri.encode(calendarId)}")
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}
