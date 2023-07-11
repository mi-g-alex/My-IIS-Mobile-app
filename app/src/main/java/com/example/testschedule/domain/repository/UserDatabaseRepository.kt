package com.example.testschedule.domain.repository

import com.example.testschedule.data.local.entity.ListOfSavedEntity
import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel

interface UserDatabaseRepository {

    suspend fun getSchedule(id: String): ScheduleModel?

    suspend fun setSchedule(model: ScheduleModel)

    suspend fun deleteSchedule(id: String)


    suspend fun getAllSavedScheduleList(): List<ListOfSavedEntity>

    suspend fun addNewSavedScheduleToList(model: ListOfSavedEntity)

    suspend fun deleteFromScheduleList(id: String)

    suspend fun insertAllGroupsList(groups: List<ListOfGroupsModel>)

    suspend fun insertAllEmployeesList(employees: List<ListOfEmployeesModel>)

    suspend fun getAllGroupsList(): List<ListOfGroupsModel>

    suspend fun getAllEmployeesList(): List<ListOfEmployeesModel>

    suspend fun deleteAllGroupsList()

    suspend fun deleteAllEmployeesList()
}