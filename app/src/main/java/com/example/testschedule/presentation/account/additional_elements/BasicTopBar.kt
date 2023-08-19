package com.example.testschedule.presentation.account.additional_elements

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.testschedule.R

@Composable
fun BasicTopBar(
    onBackPressed: () -> Unit,
    title: String,
    enabled: Boolean,
    isOfflineResult: Boolean
) {
    val cnt = LocalContext.current
    val toastText = stringResource(id = R.string.offline_mode_desc)
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackPressed, enabled = enabled) {
                Icon(
                    Icons.Default.ArrowBack,
                    title
                )
            }
        },
        actions = {
            if (isOfflineResult) {
                IconButton(onClick = {
                    Toast.makeText(cnt, toastText, Toast.LENGTH_LONG).show()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.offline_mode),
                        contentDescription = stringResource(id = R.string.offline_mode_desc)
                    )
                }
            }
        }
    )
}