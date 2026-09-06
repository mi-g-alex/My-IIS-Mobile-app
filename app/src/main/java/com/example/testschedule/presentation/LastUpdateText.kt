package com.example.testschedule.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.testschedule.R
import com.example.testschedule.domain.repository.UserDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CacheUpdateViewModel @Inject constructor(
    private val repository: UserDatabaseRepository
) : ViewModel() {
    fun observe(cacheKey: String) = repository.observeLastUpdate(cacheKey)
}

@Composable
fun LastUpdateText(
    cacheKey: String?,
    modifier: Modifier = Modifier,
    viewModel: CacheUpdateViewModel = hiltViewModel()
) {
    if (cacheKey == null) return

    val updateFlow = remember(cacheKey) { viewModel.observe(cacheKey) }
    val updatedAt by updateFlow.collectAsState(initial = null)
    updatedAt?.takeIf { it > 0L }?.let { timestamp ->
        val formatted = remember(timestamp) {
            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(timestamp))
        }
        Text(
            text = stringResource(id = R.string.data_updated_at, formatted),
            modifier = modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
