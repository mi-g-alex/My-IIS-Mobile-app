package com.example.testschedule.presentation.account.settings.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.testschedule.R

@Composable
fun DialogOutlook(
    login: String,
    password: String,
    copy: (text: String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(id = R.string.account_settings_outlook_view_title)) },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(id = R.string.close))
            }
        },
        text = {
            Column {
                ListItem(
                    headlineContent = { Text(login) },
                    overlineContent = { Text(stringResource(id = R.string.account_settings_outlook_view_login)) },
                    supportingContent = { Text(stringResource(id = R.string.copy_tap)) },
                    modifier = Modifier
                        .clickable { copy(login) }
                )
                ListItem(
                    headlineContent = { Text(password) },
                    overlineContent = { Text(stringResource(id = R.string.account_settings_outlook_view_password)) },
                    supportingContent = { Text(stringResource(id = R.string.copy_tap)) },
                    modifier = Modifier
                        .clickable { copy(password) }
                )
            }
        }
    )
}