package com.example.testschedule.data.repository

import com.example.testschedule.data.remote.IisAPI
import com.example.testschedule.data.remote.dto.ScheduleDto
import com.example.testschedule.data.remote.dto.ScheduleEmployeeItemDto
import com.example.testschedule.data.remote.dto.ScheduleGroupItemDto
import com.example.testschedule.domain.repository.IisAPIRepository
import javax.inject.Inject

class IisAPIRepositoryImpl @Inject constructor(
    private val api: IisAPI
) : IisAPIRepository {
    override suspend fun getListOfGroups(): List<ScheduleGroupItemDto> {
        return api.getListOfGroups()
    }

    override suspend fun getListOfEmployees(): List<ScheduleEmployeeItemDto> {
        return api.getListOfEmployees()
    }

    override suspend fun getSchedule(id: String): ScheduleDto {
        return if(id[0] in '0'..'9') {
            api.getGroupSchedule(id)
        } else {
            api.getEmployeeSchedule(id)
        }
    }

}