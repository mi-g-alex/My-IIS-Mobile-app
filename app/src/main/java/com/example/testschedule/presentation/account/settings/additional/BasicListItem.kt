package com.example.testschedule.presentation.account.settings.additional

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun BasicListItem(
    mainText: String,
    descText: String,
    additionalText: String? = null,
    toDo: (() -> Unit)? = null
) {
    ListItem(
        headlineContent = { Text(mainText) },
        overlineContent = {
            additionalText?.let {
                Text(
                    it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        supportingContent = { Text(descText) },
        modifier = Modifier.clickable { toDo?.invoke() },
        trailingContent = {
            toDo?.let {
                Icon(
                    Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    descText,
                    modifier = Modifier.minimumInteractiveComponentSize()
                )
            }
        }
    )
}
