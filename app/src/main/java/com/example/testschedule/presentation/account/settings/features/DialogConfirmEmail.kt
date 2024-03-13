package com.example.testschedule.presentation.account.settings.features

    import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testschedule.R
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.time.Duration.Companion.seconds

@Composable
fun DialogConfirmEmail(
    onGetCodeClicked: () -> Unit,
    onSendCodeClicked: (code: String) -> Unit,
    errorString: String,
    leftTime: Long,
    numberOfAttempt: Int,
    onDismiss: () -> Unit,
) {

    var code by remember { mutableStateOf("") }
    var ticks by remember { mutableLongStateOf(0L) }

    LaunchedEffect(leftTime) {
        ticks = leftTime - Calendar.getInstance().timeInMillis
        ticks /= 1000
    }

    LaunchedEffect(ticks) {
        while (ticks > 0) {
            delay(1.seconds)
            ticks--
        }
    }

    val codeReg = Regex("^(\\d{0,6})$")
    val isOkTime: () -> Boolean = {
        val calTmp = Calendar.getInstance()
        val cal = calTmp.apply {
            GregorianCalendar(
                get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH),
                get(Calendar.HOUR), get(Calendar.MINUTE), get(Calendar.SECOND)
            )
        }
        cal.timeInMillis < leftTime && leftTime != 0L
    }

    val isOkToConfirm: () -> Boolean = {
        isOkTime() && code.length == 6
    }

    val isOKToGetCode: () -> Boolean = {
        !isOkTime() && numberOfAttempt > 0
    }


    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onSendCodeClicked(code) }, enabled = isOkToConfirm()) {
                Text(stringResource(id = R.string.account_settings_email_confirm_button_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(stringResource(id = R.string.account_settings_email_confirm_title))
        },
        text = {
            Column {
                OutlinedButton(
                    onClick = { onGetCodeClicked() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isOKToGetCode()
                ) {
                    Text(
                        if (leftTime > 0 && ticks > 0)
                            stringResource(
                                id = R.string.account_settings_email_confirm_left_time,
                                ticks.toString()
                            )
                        else
                            stringResource(
                                id = R.string.account_settings_email_confirm_get_code,
                                numberOfAttempt
                            )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = code,
                    onValueChange = {
                        if (codeReg.matches(it))
                            code = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        autoCorrect = false,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    label = {
                        Text(stringResource(id = R.string.account_settings_email_confirm_input_code_label))
                    },
                    placeholder = {
                        Text(stringResource(id = R.string.account_settings_email_confirm_input_code_hint))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text(
                            if (errorString == "WRONG_CODE")
                                stringResource(id = R.string.account_settings_email_confirm_error_wrong_code)
                            else ""
                        )
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewEmail() {
    DialogConfirmEmail({}, {}, "", 0L, 1) {}
}