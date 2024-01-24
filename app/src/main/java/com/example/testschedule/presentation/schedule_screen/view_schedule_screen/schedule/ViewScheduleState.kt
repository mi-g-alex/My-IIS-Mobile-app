package com.example.testschedule.presentation.schedule_screen.view_schedule_screen.schedule

import com.example.testschedule.domain.model.schedule.ScheduleModel

data class ViewScheduleState(
    val isLoading: Boolean = false,
    val schedule: ScheduleModel? = null,
    val error: String? = null
)
