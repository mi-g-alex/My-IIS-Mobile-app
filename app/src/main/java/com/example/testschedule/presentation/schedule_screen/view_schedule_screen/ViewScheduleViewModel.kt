package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testschedule.common.Resource
import com.example.testschedule.domain.use_case.schedule.get_schedule.GetScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ViewScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ViewScheduleState())
    val state: State<ViewScheduleState> = _state

    init {
        getSchedule()
    }

    fun getSchedule() {
        getScheduleUseCase("253501").onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ViewScheduleState(schedule = result.data)
                }

                is Resource.Loading -> {
                    _state.value = ViewScheduleState(isLoading = true)
                }

                is Resource.Error -> {
                    _state.value = ViewScheduleState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

}