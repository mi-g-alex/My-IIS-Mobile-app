package com.example.testschedule.domain.repository

import com.example.testschedule.data.remote.dto.ScheduleDto
import com.example.testschedule.data.remote.dto.ScheduleEmployeeItemDto
import com.example.testschedule.data.remote.dto.ScheduleGroupItemDto
import com.example.testschedule.domain.modal.schedule.ScheduleModel
import retrofit2.http.Path
import retrofit2.http.Query

interface IisAPIRepository {

    suspend fun getListOfGroups() : List<ScheduleGroupItemDto>

    suspend fun getListOfEmployees() : List<ScheduleEmployeeItemDto>

    suspend fun getSchedule(id : String) : ScheduleModel

}