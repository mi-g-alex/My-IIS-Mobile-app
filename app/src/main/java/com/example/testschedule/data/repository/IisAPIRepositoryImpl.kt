package com.example.testschedule.data.repository

import com.example.testschedule.data.remote.IisAPI
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel
import com.example.testschedule.domain.model.schedule.scheduleFromDtoToModel
import com.example.testschedule.domain.repository.IisAPIRepository
import javax.inject.Inject

class IisAPIRepositoryImpl @Inject constructor(
    private val api: IisAPI
) : IisAPIRepository {
    override suspend fun getListOfGroups(): List<ListOfGroupsModel> {
        return api.getListOfGroups().map { it.toModel() }
    }

    override suspend fun getListOfEmployees(): List<ListOfEmployeesModel> {
        return api.getListOfEmployees().map { it.toModel() }
    }

    override suspend fun getSchedule(id: String): ScheduleModel {
        return if (id[0] in '0'..'9') {
            scheduleFromDtoToModel(api.getGroupSchedule(id))
        } else {
            scheduleFromDtoToModel(api.getEmployeeSchedule(id))
        }
    }

    override suspend fun getCurrentWeek(): Int = api.getCurrentWeek()
}