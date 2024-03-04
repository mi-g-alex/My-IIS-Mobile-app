package com.example.testschedule.presentation.account.settings.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.testschedule.R

@Composable
fun DialogPassword(
    onSaveClick: (oldPass: String, newPass: String) -> Unit,
    errorText: String,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {

    var passOldText by remember { mutableStateOf("") }
    var passNewText by remember { mutableStateOf("") }
    var passNewConfText by remember { mutableStateOf("") }

    var showOldPasswordState by remember { mutableStateOf(false) }
    var showNewPasswordState by remember { mutableStateOf(false) }
    var showNewConfPasswordState by remember { mutableStateOf(false) }

    val ko = fun(imeAction: ImeAction) = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = imeAction
    )

    val isGoodNewPassword = fun(): Boolean {
        val reg =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+=|{}.,\\[\\]])([A-Za-z\\d!@#\$%^&*()_+=|{}.,\\[\\]]){8,20}\$")
        return reg.matches(passNewText)
    }

    val isAllGood = fun(): Boolean {
        return passOldText.length > 7 &&
                passNewText.length > 7 &&
                passNewText == passNewConfText &&
                isGoodNewPassword()
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                stringResource(id = R.string.account_settings_password_title)
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSaveClick(passOldText, passNewText) },
                enabled = isAllGood() && !isLoading
            ) {
                Text(stringResource(id = R.string.account_settings_password_save))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(id = R.string.account_settings_password_cancel))
            }
        },
        text = {
            if(isLoading) LinearProgressIndicator()
            Column {
                if(isLoading) LinearProgressIndicator()
                OutlinedTextField(
                    value = passOldText,
                    onValueChange = { passOldText = it },
                    isError = errorText == "WrongOldPassword",
                    supportingText = { if (errorText == "WrongOldPassword") Text(stringResource(id = R.string.account_settings_password_error_old_password)) },
                    visualTransformation = if (!showOldPasswordState) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        PasswordIcon(
                            { showOldPasswordState = !showOldPasswordState },
                            showOldPasswordState
                        )
                    },
                    keyboardOptions = ko(ImeAction.Next)
                )
                OutlinedTextField(
                    value = passNewText,
                    onValueChange = { passNewText = it },
                    modifier = Modifier.padding(vertical = 8.dp),
                    visualTransformation = if (!showNewPasswordState) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        PasswordIcon(
                            { showNewPasswordState = !showNewPasswordState },
                            showNewPasswordState
                        )
                    },
                    isError = !isGoodNewPassword() && passNewText.isNotEmpty(),
                    supportingText = {
                        if (!isGoodNewPassword() && passNewText.isNotEmpty())
                            Text(stringResource(id = R.string.account_settings_password_error_new_password_format))
                    },
                    keyboardOptions = ko(ImeAction.Next)
                )

                OutlinedTextField(
                    value = passNewConfText,
                    onValueChange = { passNewConfText = it },
                    visualTransformation = if (!showNewConfPasswordState) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        PasswordIcon(
                            { showNewConfPasswordState = !showNewConfPasswordState },
                            showNewConfPasswordState
                        )
                    },
                    isError = passNewText != passNewConfText && passNewConfText.isNotEmpty(),
                    supportingText = {
                        if (passNewText != passNewConfText && passNewConfText.isNotEmpty())
                            Text(stringResource(id = R.string.account_settings_password_error_passwords_not_same))
                    },
                    keyboardOptions = ko(ImeAction.Done),
                    keyboardActions =
                    KeyboardActions(
                        onDone = {
                            if (isAllGood()) onSaveClick(passOldText, passNewText)
                        }
                    )

                )
            }
        }
    )
}

@Composable
private fun PasswordIcon(onClick: () -> Unit, value: Boolean) {
    IconButton(onClick = { onClick() }) {
        if (!value) {
            Icon(
                painterResource(id = R.drawable.password_hide),
                stringResource(id = R.string.password_show_title),
            )
        } else {
            Icon(
                painterResource(id = R.drawable.password_show),
                stringResource(id = R.string.password_hide_title),
            )
        }
    }
}
