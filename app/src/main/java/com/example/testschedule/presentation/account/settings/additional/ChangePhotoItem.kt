package com.example.testschedule.presentation.account.settings.additional

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.testschedule.R

@Composable
fun ChangePhotoItem(
    photoUrl: String,
    toDo: () -> Unit
) {
    ListItem(
        headlineContent = { Text(stringResource(id = R.string.account_settings_avatar)) },
        supportingContent = { Text(stringResource(id = R.string.account_settings_avatar_desc)) },
        modifier = Modifier.clickable { toDo() },
        trailingContent = {
            var showIcon by remember { mutableStateOf(true) }
            if (showIcon) {
                Icon(
                    Icons.Outlined.AccountCircle,
                    null,
                    modifier = Modifier.size(48.dp)
                )
            }
            SubcomposeAsyncImage(
                model = photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                onSuccess = { showIcon = false }
            )
        }
    )
}