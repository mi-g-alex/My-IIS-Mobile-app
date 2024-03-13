package com.example.testschedule.presentation.account.settings.additional

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.testschedule.R

@Composable
fun ButtonEmailUnconfirmedItem(
    onClicked: () -> Unit,
) {
    OutlinedButton(
        onClick = { onClicked() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            stringResource(id = R.string.account_settings_unconfirmed_email),
            textAlign = TextAlign.Center
        )
    }
}