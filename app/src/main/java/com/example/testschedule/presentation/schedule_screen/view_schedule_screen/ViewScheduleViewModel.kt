package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.repository.UserDatabaseRepository
import com.example.testschedule.domain.use_case.schedule.get_schedule.GetScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ViewScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val db: UserDatabaseRepository
) : ViewModel() {

    private val _state = mutableStateOf(ViewScheduleState())
    val state: State<ViewScheduleState> = _state

    fun getSchedule(id: String) {
        getScheduleUseCase(id = id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ViewScheduleState(schedule = result.data)
                    /*result.data?.let {
                        db.setSchedule(it)
                    }*/
                }

                is Resource.Loading -> {
                    val a = db.getSchedule(id)
                    _state.value = ViewScheduleState(isLoading = true, schedule = a)
                }

                is Resource.Error -> {
                    _state.value = ViewScheduleState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

}