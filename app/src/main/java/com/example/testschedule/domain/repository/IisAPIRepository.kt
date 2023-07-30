package com.example.testschedule.domain.repository

import com.example.testschedule.domain.model.schedule.ListOfEmployeesModel
import com.example.testschedule.domain.model.schedule.ListOfGroupsModel
import com.example.testschedule.domain.model.schedule.ScheduleModel

interface IisAPIRepository {

    suspend fun getListOfGroups(): List<ListOfGroupsModel>

    suspend fun getListOfEmployees(): List<ListOfEmployeesModel>

    suspend fun getSchedule(id: String): ScheduleModel

    suspend fun getCurrentWeek(): Int

}