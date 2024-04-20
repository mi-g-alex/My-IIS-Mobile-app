package com.example.testschedule.presentation.account.headman_screen.components.create_omissions

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun AddRemoveHours(
    hours: Int,
    maxHours: Int,
    enabled: Boolean,
    isRespect: Boolean,
    changeHours: (hours: Int) -> Unit
) {

    val enabledRemove: () -> Boolean = { hours > 0 && enabled }
    val enabledAdd: () -> Boolean = { hours < maxHours && enabled }

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { changeHours(hours - 1) }, enabled = enabledRemove()) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "")
        }

        Text(
            hours.toString(),
            style = MaterialTheme.typography.headlineSmall,
            textDecoration = if (isRespect) TextDecoration.Underline else null,
            fontFamily = FontFamily.Monospace
        )

        IconButton(onClick = { changeHours(hours + 1) }, enabled = enabledAdd()) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "")
        }
    }
}