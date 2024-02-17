package com.example.testschedule.presentation.account.settings.additional

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WithCheckBoxListItem(
    mainText: String,
    descText: String,
    isChecked: Boolean,
    enabled: Boolean,
    onCheckChange: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(mainText) },
        supportingContent = { Text(descText) },
        modifier = if (enabled) Modifier.clickable { onCheckChange() } else Modifier,
        trailingContent = {
            Checkbox(checked = isChecked, onCheckedChange = { onCheckChange() }, enabled = enabled)
        }
    )
}