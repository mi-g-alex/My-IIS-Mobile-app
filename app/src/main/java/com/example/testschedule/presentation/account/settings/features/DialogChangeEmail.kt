package com.example.testschedule.presentation.account.settings.features

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.testschedule.R

@Composable
fun DialogChangeEmail(
    onSaveClick: (email: String) -> Unit,
    curEmail: String,
    errorText: String,
    onDismiss: () -> Unit
) {

    val errorSame = stringResource(id = R.string.account_settings_email_update_error_same)

    var emailText by remember { mutableStateOf("") }
    val isOk: () -> Boolean = {
        Regex("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)").matches(emailText) && curEmail != emailText
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onSaveClick(emailText) },
                enabled = isOk()
            ) {
                Text(stringResource(id = R.string.save))
            }
        },
        title = {
            Text(stringResource(id = R.string.account_settings_email_update_title))
        },
        text = {
            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it },
                placeholder = {
                    Text(curEmail)
                },
                label = {
                    Text(stringResource(id = R.string.account_settings_email_update_label))
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isOk()) onSaveClick(emailText)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                supportingText = {
                    if (errorText == "SAME" || curEmail == emailText) Text(errorSame)
                    else if (!isOk() && emailText.isNotEmpty()) Text(stringResource(id = R.string.account_settings_email_update_regex))
                    else Text("")
                },
                isError = errorSame == "SAME"
            )
        }
    )

}