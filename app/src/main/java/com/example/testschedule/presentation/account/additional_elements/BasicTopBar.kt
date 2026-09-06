package com.example.testschedule.presentation.account.additional_elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testschedule.presentation.LastUpdateText
import com.example.testschedule.presentation.LoadingTopBar

/**
 * `onBackPressed()` -- функция, которая нажимает при нажатии стрелки назад
 *
 * `title` -- заголовок который отображается
 *
 * `enabled` -- для защиты от двойного нажатия кнопки назад.
 * __**Не забыть в onBackPressed пропусать установку на false**__
 *
 * `additionalButtons` -> доп кнопкм
 */
@Composable
fun BasicTopBar(
    onBackPressed: () -> Unit,
    title: String,
    enabled: Boolean,
    isLoading: Boolean = false,
    additionalButtons: @Composable () -> Unit = {}
) {
    LoadingTopBar(isLoading = isLoading) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = onBackPressed, enabled = enabled) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        title
                    )
                }
            },
            actions = { additionalButtons() }
        )
    }
}

@Composable
fun LastUpdateListItem(cacheKey: String?) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        LastUpdateText(
            cacheKey = cacheKey,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
