package com.example.testschedule.presentation.account.additional_elements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListDataSection(
    listOfItems: List<SectionItem>,
    additionTopItem: List<@Composable (() -> Unit)> = emptyList(),
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        additionTopItem.map {
            item { it() }
        }

        listOfItems.forEach { sectionItem ->
            if (sectionItem.title != null) {
                stickyHeader {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                sectionItem.title,
                                style = MaterialTheme.typography.displaySmall
                            )
                            sectionItem.icon()
                        }
                    }
                }
            }

            if (sectionItem.itemList.isEmpty()) {
                item {
                    Box(
                        if (sectionItem.title == null)
                            Modifier.fillMaxSize()
                        else
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyText(sectionItem.emptyText)
                    }
                }
            } else {
                sectionItem.itemList.forEach { element ->
                    item {
                        Box(modifier = Modifier.padding(vertical = 4.dp)) {
                            element()
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyText(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

data class SectionItem(
    val title: String?,
    val emptyText: String,
    val itemList: List<@Composable (() -> Unit)>,
    val icon: @Composable () -> Unit = {}
)