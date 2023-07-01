package com.example.testschedule.presentation.schedule_screen.view_schedule_screen

import com.example.testschedule.domain.modal.schedule.ScheduleModel

data class ViewScheduleState(
    val isLoading: Boolean = false,
    val schedule: ScheduleModel? = null,
    val error: String? = null
)
