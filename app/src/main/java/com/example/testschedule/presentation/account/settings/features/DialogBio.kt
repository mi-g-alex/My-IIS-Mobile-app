package com.example.testschedule.presentation.account.settings.features

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.testschedule.R

@Composable
fun DialogBio(
    onSaveClick: (bio: String) -> Unit,
    text: String,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {

    var bioText by remember { mutableStateOf(text) }


    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onSaveClick(bioText) },
                enabled = !isLoading
            ) {
                Text(stringResource(id = R.string.account_settings_info_bio_save))
            }
        },
        title = {
            Text(stringResource(id = R.string.account_settings_info_bio_title))
        },
        text = {
            TextField(value = bioText, onValueChange = { bioText = it },
                label = {
                    Text(stringResource(id = R.string.account_settings_info_bio_set_bio))
                },
                placeholder = {
                    Text(stringResource(id = R.string.account_settings_info_bio_set_empty))
                }
            )
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
            ) {
                Text(stringResource(id = R.string.account_settings_info_bio_cancel))
            }
        }
    )
}