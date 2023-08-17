package com.example.testschedule.presentation.schedule_screen.add_schedule_screen

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.example.testschedule.R
import com.example.testschedule.common.Constants

@Composable
fun SelectSubgroupDialog(
    closeDialog: () -> Unit
) {
    var selectedSubgroup by remember { mutableIntStateOf(0) }

    val cnt = LocalContext.current
    val sharedPref = cnt.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE)
    selectedSubgroup = sharedPref.getInt(Constants.SELECTED_SUBGROUP, 0)
    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    sharedPref.edit().putInt(Constants.SELECTED_SUBGROUP, selectedSubgroup).apply()
                    closeDialog()
                }
            ) {
                Text(stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                closeDialog()
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(stringResource(id = R.string.schedule_select_default_text))
        },
        text = {
            LazyColumn {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedSubgroup == 0,
                            onClick = { selectedSubgroup = 0 }
                        )
                        Text(text = stringArrayResource(id = R.array.subgroups)[0])
                    }
                }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedSubgroup == 1,
                            onClick = { selectedSubgroup = 1 }
                        )
                        Text(text = stringArrayResource(id = R.array.subgroups)[1])
                    }
                }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedSubgroup == 2,
                            onClick = { selectedSubgroup = 2 }
                        )
                        Text(text = stringArrayResource(id = R.array.subgroups)[2])
                    }
                }
            }
        }
    )
}