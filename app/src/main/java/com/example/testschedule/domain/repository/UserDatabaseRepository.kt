package com.example.testschedule.domain.repository

import com.example.testschedule.domain.modal.schedule.ScheduleModel

interface UserDatabaseRepository {

    suspend fun getSchedule(id: String) : ScheduleModel?

    suspend fun setSchedule(model : ScheduleModel)
}