package com.example.testschedule.presentation.account.settings.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.example.testschedule.R
import com.example.testschedule.domain.model.account.profile.AccountProfileModel.SkillModel

@Composable
fun DialogSkills(
    onSaveClick: (List<SkillModel>) -> Unit,
    curSkills: List<SkillModel>,
    isLoading: Boolean,
    copy: (text: String) -> Unit,
    toast: (text: String) -> Unit,
    onDismiss: () -> Unit
) {

    var actualList by remember { mutableStateOf(curSkills) }

    LaunchedEffect(actualList.size) {}

    val errText = stringResource(id = R.string.account_settings_info_skills_add_err)

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
            Text(stringResource(id = R.string.account_settings_info_skills_title))
        },
        text = {
            if (isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                AddSkillField { str ->
                    if (!actualList.any { p -> p.name == str })
                        actualList =
                            actualList.toMutableList().also { it.add(0, SkillModel(0, str)) }
                    else {
                        toast(errText)
                    }
                }
                LazyColumn {
                    actualList.sortedBy { it.name }.forEach { item ->
                        item {
                            SkillItem(
                                item = item,
                                onRemoveClicked = {
                                    actualList = actualList.toMutableList().also {
                                        it.remove(item)
                                    }
                                },
                                copy = copy
                            )

                        }
                    }
                }
            }
        }
    )

}

@Composable
private fun AddSkillField(
    onAddClicked: (String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current


    var newSkillText by remember { mutableStateOf("") }
    val isGood :() -> Boolean = { newSkillText.length in (1..45) }

        OutlinedTextField(
        value = newSkillText,
        onValueChange = { newSkillText = it },
        label = {
            Text(stringResource(id = R.string.account_settings_info_skills_add_hint))
        },
        singleLine = true,
        supportingText = {
            Text("${newSkillText.length}/45", Modifier.fillMaxWidth(), textAlign = TextAlign.End)
        },
        isError = newSkillText.length > 45,
        trailingIcon = {
            IconButton(
                onClick = {
                    onAddClicked(newSkillText); keyboardController?.hide(); newSkillText = ""
                },
                enabled = isGood()
            ) {
                Icon(
                    Icons.Default.Add,
                    stringResource(id = R.string.account_settings_info_skills_add_desc)
                )
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                if (isGood()) {
                    onAddClicked(newSkillText)
                    keyboardController?.hide()
                    newSkillText = ""
                }
            }
        )

    )
}

@Composable
private fun SkillItem(
    item: SkillModel,
    onRemoveClicked: () -> Unit,
    copy: (text: String) -> Unit,
) {
    ListItem(
        headlineContent = { Text(item.name) },
        trailingContent = {
            IconButton(onClick = { onRemoveClicked() }) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.remove_something_circle),
                    stringResource(
                        id = R.string.account_settings_info_skills_remove_desc,
                        item.name
                    )
                )
            }
        },
        modifier = Modifier
            .clickable { copy(item.name) }
    )
}