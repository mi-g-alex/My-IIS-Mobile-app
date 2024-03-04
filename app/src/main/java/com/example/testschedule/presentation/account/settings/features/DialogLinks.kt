package com.example.testschedule.presentation.account.settings.features

import android.webkit.URLUtil
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.profile.AccountProfileModel
import com.example.testschedule.domain.model.account.profile.AccountProfileModel.SkillModel

@Composable
fun DialogLinks(
    onSaveClick: (List<AccountProfileModel.ReferenceModel>) -> Unit,
    curLinks: List<AccountProfileModel.ReferenceModel>,
    isLoading: Boolean,
    copy: (text: String) -> Unit,
    toast: (text: String) -> Unit,
    onDismiss: () -> Unit
) {

    var actualList by remember { mutableStateOf(curLinks) }

    LaunchedEffect(actualList.size) {}

    val errText = stringResource(id = R.string.account_settings_info_links_add_err)

    AlertDialog(
        onDismissRequest = { onDismiss(); },
        confirmButton = {
            TextButton(
                onClick = {
                    onSaveClick(actualList)
                }
            ) {
                Text(stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(stringResource(id = R.string.account_settings_info_links_title))
        },
        text = {
            if (isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                AddLinkField { str, str1 ->
                    if (!actualList.any { p -> p.name == str || p.reference == str1 })
                        actualList =
                            actualList.toMutableList().also {
                                it.add(
                                    0,
                                    AccountProfileModel.ReferenceModel(0, str, str1)
                                )
                            }
                    else {
                        toast(errText)
                    }
                }
                LazyColumn {
                    actualList.sortedBy { it.name }.forEach { item ->
                        item {
                            LinkItem(
                                item = item,
                                onRemoveClicked = {
                                    actualList = actualList.toMutableList().also {
                                        it.remove(item)
                                    }
                                },
                                copy = copy,
                                toast = toast
                            )
                        }
                    }
                }
            }
        }
    )

}

@Composable
private fun AddLinkField(
    onAddClicked: (String, String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current


    var newLinkText by remember { mutableStateOf("") }
    var newLinkUrl by remember { mutableStateOf("") }
    val prefix = "https://"

    val isGood: () -> Boolean =
        { newLinkText.length in (1..45) && URLUtil.isValidUrl(prefix + newLinkUrl) }
    Column {
        OutlinedTextField(
            value = newLinkText,
            onValueChange = { newLinkText = it },
            placeholder = {
                Text(stringResource(id = R.string.account_settings_info_links_add_hint1))
            },
            label = {
                Text(stringResource(id = R.string.account_settings_info_links_add_label))
            },
            singleLine = true,
            supportingText = {
                Text(
                    "${newLinkText.length}/45",
                    Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            isError = newLinkText.length > 45,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = newLinkUrl,
            onValueChange = { newLinkUrl = it },
            label = {
                Text(stringResource(id = R.string.account_settings_info_links_add_hint2))
            },
            singleLine = true,
            prefix = { Text(prefix) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onAddClicked(newLinkText, prefix + newLinkUrl)
                        keyboardController?.hide()
                        newLinkText = ""
                        newLinkUrl = ""
                    },
                    enabled = isGood()
                ) {
                    Icon(
                        Icons.Default.Add,
                        stringResource(id = R.string.account_settings_info_links_add_desc)
                    )
                }
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isGood()) {
                        onAddClicked(newLinkText, prefix + newLinkUrl)
                        keyboardController?.hide()
                        newLinkText = ""
                        newLinkUrl = ""
                    }
                }
            )
        )
    }
}

@Composable
private fun LinkItem(
    item: AccountProfileModel.ReferenceModel,
    onRemoveClicked: () -> Unit,
    copy: (text: String) -> Unit,
    toast: (text: String) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val errText = stringResource(id = R.string.account_settings_info_links_error_bad_link)
    val ref = if (item.reference.startsWith("http")) item.reference else "https://" + item.reference
    ListItem(
        headlineContent = { Text(item.name) },
        supportingContent = { Text(ref) },
        trailingContent = {
            Row {
                IconButton(
                    onClick = {
                        try {
                            uriHandler.openUri(ref)
                        } catch (e: Exception) {
                            toast(errText)
                        }
                    }
                ) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.link),
                        stringResource(
                            id = R.string.account_settings_info_links_remove_desc,
                            item.name
                        )
                    )
                }
                IconButton(onClick = { onRemoveClicked() }) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.remove_something_circle),
                        stringResource(
                            id = R.string.account_settings_info_links_remove_desc,
                            item.name
                        )
                    )
                }
            }
        },
        modifier = Modifier.clickable {
            copy(ref)
        }
    )
}